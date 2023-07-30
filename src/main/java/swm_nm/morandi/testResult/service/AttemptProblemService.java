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
import swm_nm.morandi.exception.errorcode.ProblemErrorCode;
import swm_nm.morandi.exception.errorcode.TestErrorCode;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.problem.dto.BojProblem;
import swm_nm.morandi.problem.dto.DifficultyLevel;
import swm_nm.morandi.test.domain.TestType;
import swm_nm.morandi.test.dto.TestCheckDto;
import swm_nm.morandi.test.dto.TestRatingDto;
import swm_nm.morandi.test.repository.TestTypeRepository;
import swm_nm.morandi.test.service.TestService;
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

import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AttemptProblemService {
    private final ObjectMapper objectMapper;

    private final AttemptProblemRepository attemptProblemRepository;

    private final AlgorithmProblemListRepository algorithmProblemListRepository;

    private final AlgorithmRepository algorithmRepository;


    private final TestRepository testRepository;



    private final TestService testService;



    public Map<String, Object> getMemberRecords() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        List<GrassDto> grassDtos = getGrassDtosByMemberId(memberId);
        List<GraphDto> graphDtos = getGraphDtosByMemberId(memberId);
        List<TestRatingDto> testRatingDtos = testService.getTestRatingDtosByMemberId(memberId);
        Map<String, Object> responseData = new HashMap<>();
            responseData.put("grassDto", grassDtos);
            responseData.put("graphDto", graphDtos);
            responseData.put("testRatingDto", testRatingDtos);
        return responseData;
    }

    public List<GrassDto> getGrassDtosByMemberId(Long memberId) {
        List<GrassDto> grassDtos = new ArrayList<>();
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByMember_MemberId(memberId);
        if (!attemptProblems.isEmpty()) {
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

        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByMember_MemberId(memberId);

        List<GraphDto> graphDtos = new ArrayList<>();
        if (!attemptProblems.isEmpty()) {
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
