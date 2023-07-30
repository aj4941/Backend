package swm_nm.morandi.testResult.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.PrimitiveValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import swm_nm.morandi.auth.security.SecurityUtils;
import swm_nm.morandi.exception.MorandiException;
import swm_nm.morandi.exception.errorcode.MemberErrorCode;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.problem.dto.BojProblem;
import swm_nm.morandi.problem.dto.DifficultyLevel;
import swm_nm.morandi.test.domain.TestType;
import swm_nm.morandi.test.repository.TestTypeRepository;
import swm_nm.morandi.testResult.request.AttemptProblemDto;
import swm_nm.morandi.member.repository.MemberRepository;
import swm_nm.morandi.problem.domain.Algorithm;
import swm_nm.morandi.problem.domain.AlgorithmProblemList;
import swm_nm.morandi.problem.domain.Problem;
import swm_nm.morandi.problem.repository.ProblemRepository;
import swm_nm.morandi.test.domain.Test;
import swm_nm.morandi.test.repository.TestRepository;
import swm_nm.morandi.testResult.entity.AttemptProblem;
import swm_nm.morandi.member.dto.GraphDto;
import swm_nm.morandi.member.dto.GrassDto;
import swm_nm.morandi.problem.repository.AlgorithmProblemListRepository;
import swm_nm.morandi.problem.repository.AlgorithmRepository;
import swm_nm.morandi.member.repository.AttemptProblemRepository;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.Math.max;

@Service
@RequiredArgsConstructor
public class AttemptProblemService {
    private final ObjectMapper objectMapper;

    private final AttemptProblemRepository attemptProblemRepository;

    private final AlgorithmProblemListRepository algorithmProblemListRepository;

    private final AlgorithmRepository algorithmRepository;

    private final MemberRepository memberRepository;

    private final TestRepository testRepository;

    private final ProblemRepository problemRepository;

    private final TestTypeRepository testTypeRepository;

    @Transactional
    public List<Long> saveAttemptProblems(Long memberId, Long testId, List<BojProblem> bojProblems) {
        Optional<Member> resultMember = memberRepository.findById(memberId);
        Optional<Test> resultTest = testRepository.findById(testId);
        Member member = resultMember.get();
        Test test = resultTest.get();
        List<Long> attemptProblemIds = new ArrayList<>();
        for (BojProblem bojProblem : bojProblems) {
            Optional<Problem> resultProblem = problemRepository.findProblemByBojProblemId(bojProblem.getBojProblemId());
            Problem problem = resultProblem.get();
            AttemptProblem attemptProblem = AttemptProblem.builder()
                    .isSolved(false)
                    .testDate(LocalDate.now())
                    .executionTime(null)
                    .member(member)
                    .test(test)
                    .problem(problem)
                    .build();
            attemptProblemRepository.save(attemptProblem);
            attemptProblemIds.add(attemptProblem.getAttemptProblemId());
        }
        return attemptProblemIds;
    }

    @Transactional
    public void checkAttemptedProblemResult(Long testId, String bojId) {
        Optional<Test> resultTest = testRepository.findById(testId);
        Optional<List<AttemptProblem>> resultAttemptProblem = attemptProblemRepository.findAttemptProblemsByTest_TestId(testId);
        Test test = resultTest.get();
        List<AttemptProblem> attemptProblems = resultAttemptProblem.get();

        for (AttemptProblem attemptProblem : attemptProblems) {
            if (attemptProblem.getIsSolved())
                continue;

            Problem problem = attemptProblem.getProblem();
            Long bojProblemId = problem.getBojProblemId();

            String apiURL = "https://solved.ac/api/v3/search/problem";
            String query = apiURL + "/?query=" + "id:" + bojProblemId.toString() + "%26@" + bojId;

            URI uri = URI.create(query);

            WebClient webClient = WebClient.builder().build();
            String jsonString = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            try {
                JsonNode jsonNode = objectMapper.readTree(jsonString);
                JsonNode checkNode = jsonNode.get("count");

                if (checkNode == null) {
                    throw new RuntimeException("json 파싱에 실패했습니다.");
                }
                // 정답을 맞춘 경우
                if (checkNode.asLong() == 1L) {
                    Duration duration = Duration.between(test.getTestDate(), LocalDateTime.now());
                    Long minutes = duration.toMinutes();
                    attemptProblem.setIsSolved(true);
                    attemptProblem.setExecutionTime(minutes);
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException("json 파싱에 실패했습니다.");
            }
        }
    }

    public List<AttemptProblemDto> getAttemptProblemDtosByTestId(Long testId) {
        Optional<Test> resultTest = testRepository.findById(testId);
        Optional<List<AttemptProblem>> resultAttemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(testId);
        Test test = resultTest.get();
        List<AttemptProblem> attemptProblems = resultAttemptProblems.get();
        List<AttemptProblemDto> attemptProblemDtos = new ArrayList<>();
        long number = 1;
        for (AttemptProblem attemptProblem : attemptProblems) {
            AttemptProblemDto attemptProblemDto = AttemptProblemDto.getAttemptProblemDto(attemptProblem);
            attemptProblemDto.setTestProblemId(number++);
            attemptProblemDtos.add(attemptProblemDto);
        }
        return attemptProblemDtos;
    }
    public List<GrassDto> getGrassDtosByMemberId(Long memberId) {
        List<GrassDto> grassDtos = new ArrayList<>();
        Optional<List<AttemptProblem>> result = attemptProblemRepository.findAllByMember_MemberId(memberId);
        if (result.isPresent()) {
            List<AttemptProblem> attemptProblems = result.get();
            Map<LocalDate, Integer> grassMap = new HashMap<>();
            for (AttemptProblem attemptProblem : attemptProblems) {
                LocalDate testDate = attemptProblem.getTestDate();
                if (attemptProblem.getIsSolved()) {
                    Integer currentValue = grassMap.getOrDefault(testDate, 0);
                    Integer newValue = currentValue + 1;
                    grassMap.put(testDate, newValue);
                }
            }
            for (Map.Entry<LocalDate, Integer> entry : grassMap.entrySet()) {
                LocalDate testDate = entry.getKey();
                Integer solvedCount = entry.getValue();
                GrassDto grassDto = GrassDto.builder()
                        .testDate(testDate)
                        .solvedCount(solvedCount)
                        .build();
                grassDtos.add(grassDto);
            }
        }
        return grassDtos;
    }

    public List<GraphDto> getGraphDtosByMemberId(Long memberId) {
        Map<String, Integer> TotalCount = new HashMap<>();
        Map<String, Integer> Count = new HashMap<>();
        List<Algorithm> algorithms = algorithmRepository.findAll();

        for (Algorithm algorithm : algorithms) {
            String algorithmName = algorithm.getAlgorithmName();
            TotalCount.put(algorithmName, 0);
            Count.put(algorithmName, 0);
        }

        Optional<List<AttemptProblem>> result = attemptProblemRepository.findAllByMember_MemberId(memberId);

        List<GraphDto> graphDtos = new ArrayList<>();
        if (result.isPresent()) {
            List<AttemptProblem> attemptProblems = result.get();
            for (AttemptProblem attemptProblem : attemptProblems) {
                Long problemId = attemptProblem.getProblem().getProblemId();
                List<AlgorithmProblemList> algorithmProblemLists =
                        algorithmProblemListRepository.findByProblem_ProblemId(problemId);
                for (AlgorithmProblemList algorithmProblemList : algorithmProblemLists) {
                    String algorithmName = algorithmProblemList.getAlgorithm().getAlgorithmName();
                    int currentTotalCount = TotalCount.getOrDefault(algorithmName, 0);
                    int currentCount = Count.getOrDefault(algorithmName, 0);
                    TotalCount.put(algorithmName, currentTotalCount + 1);
                    if (attemptProblem.getIsSolved())
                        Count.put(algorithmName, currentCount + 1);
                }
            }

            for (Algorithm algorithm : algorithms) {
                String algorithmName = algorithm.getAlgorithmName();
                Double solvedRate = (double) Count.get(algorithmName) / (double) TotalCount.get(algorithmName) * 100;
                GraphDto graphDto = GraphDto.builder()
                        .algorithmName(algorithmName)
                        .solvedRate(solvedRate)
                        .build();

                graphDtos.add(graphDto);
            }
        }

        return graphDtos;
    }
}
