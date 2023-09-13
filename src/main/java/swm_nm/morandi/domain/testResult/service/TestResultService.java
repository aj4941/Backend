package swm_nm.morandi.domain.testResult.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import swm_nm.morandi.global.utils.SecurityUtils;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.AttemptProblemRepository;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.problem.dto.BojProblem;
import swm_nm.morandi.domain.problem.dto.DifficultyLevel;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.problem.repository.ProblemRepository;
import swm_nm.morandi.domain.test.dto.TestCheckDto;
import swm_nm.morandi.domain.test.dto.TestStatus;
import swm_nm.morandi.domain.test.entity.Test;
import swm_nm.morandi.domain.test.entity.TestType;
import swm_nm.morandi.domain.test.repository.TestRepository;
import swm_nm.morandi.domain.test.repository.TestTypeRepository;
import swm_nm.morandi.domain.testResult.request.AttemptCodeDto;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.domain.testResult.entity.AttemptProblem;
import swm_nm.morandi.domain.testResult.request.AttemptProblemDto;
import swm_nm.morandi.global.exception.errorcode.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.max;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestResultService {
    private final MemberRepository memberRepository;

    private final TestTypeRepository testTypeRepository;

    private final TestRepository testRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    private final ProblemRepository problemRepository;

    private final ObjectMapper objectMapper;

    @Transactional
    public List<Long> saveAttemptProblems(Long memberId, Long testId, List<BojProblem> bojProblems) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));
        Test test = testRepository.findById(testId).orElseThrow(()-> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
        List<Long> bojProblemIds = new ArrayList<>();
        for (BojProblem bojProblem : bojProblems) {
            Problem problem = problemRepository.findProblemByBojProblemId(bojProblem.getProblemId())
                    .orElseThrow(()-> {
                                log.error("저장하려는 문제 정보를 찾지 못했습니다. 문제 번호: {}", bojProblem.getProblemId());
                               return new MorandiException(ProblemErrorCode.PROBLEM_NOT_FOUND);
                    });

            bojProblemIds.add(bojProblem.getProblemId());

            AttemptProblem attemptProblem = AttemptProblem.builder()
                    .isSolved(false)
                    .testDate(LocalDate.now())
                    .executionTime(null)
                    .member(member)
                    .test(test)
                    .problem(problem)
                    .build();

            attemptProblemRepository.save(attemptProblem);
        }
        return bojProblemIds;
    }

    //TODO
    //정답률, 테스트 레이팅 계산 시
    //굳이 맞춘 문제 개수를 다시 DB에서 읽어와서 계산할 필요가 있을까?
    //한 메서드를 진행하는데 같은 데이터를 DB에서 여러 번 읽어오는 것이 비효율적임


    @Transactional
    public void saveTestResult(Long testId, Long testTypeId){
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new MorandiException(AuthErrorCode.MEMBER_NOT_FOUND));
        Test test = testRepository.findById(testId).orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        test.setTestStatus(TestStatus.COMPLETED);
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByTest_TestId(testId);
        long correct = attemptProblems.stream()
                .filter(AttemptProblem::getIsSolved)
                .count();
        long total = attemptProblems.size();

        //문제별 결과 목록 저장 및 변경된 정답률 업데이트
        testType.updateAverageCorrectAnswerRate(correct / total);

        //테스트 레이팅 저장
        test.setTestRating(calculateTestRating(member, testId));

        member.setCurrentTestId(-1L);
    }

    private boolean isSolvedProblem(AttemptProblem attemptProblem, String bojId) {
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
            return checkNode.asLong()==1L;

        }
        catch(NullPointerException e){
            throw new RuntimeException("Node null 반환했습니다.");
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("json 파싱에 실패했습니다.");
        }


    }
    @Transactional
    public void checkAttemptedProblemResult(Long testId, String bojId) {
        Test test = testRepository.findById(testId).orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(testId);

        attemptProblems.stream()
                .filter(attemptProblem -> !attemptProblem.getIsSolved())
                .filter(attemptProblem -> isSolvedProblem(attemptProblem, bojId))
                .forEach(attemptProblem -> {
                    Duration duration = Duration.between(test.getTestDate(), LocalDateTime.now());
                    Long minutes = duration.toMinutes();
                    if (minutes <= test.getTestTime()) {
                        attemptProblem.setIsSolved(true);
                        attemptProblem.setExecutionTime(minutes);
                    }
                });
    }
    @Transactional
    public List<AttemptProblemDto> testExit(TestCheckDto testCheckDto) {
        Long testId = testCheckDto.getTestId();
        String bojId = testCheckDto.getBojId();
        Long testTypeId = testCheckDto.getTestTypeId();

        checkAttemptedProblemResult(testId, bojId);
        saveTestResult(testId, testTypeId);

        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(testId);

        List<AttemptProblemDto> attemptProblemDtos = new ArrayList<>();
        long number = 1;
        for (AttemptProblem attemptProblem : attemptProblems) {
            AttemptProblemDto attemptProblemDto = AttemptProblemDto.getAttemptProblemDto(attemptProblem);
            attemptProblemDto.setTestProblemId(number++);
            attemptProblemDtos.add(attemptProblemDto);
        }
        return attemptProblemDtos;
    }

    public List<AttemptProblemDto> isSolvedCheck(TestCheckDto testCheckDto) {
        Long testId = testCheckDto.getTestId();
        String bojId = testCheckDto.getBojId();
        checkAttemptedProblemResult(testId, bojId);
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(testId);
        List<AttemptProblemDto> attemptProblemDtos = new ArrayList<>();
        long number = 1;
        for (AttemptProblem attemptProblem : attemptProblems) {
            AttemptProblemDto attemptProblemDto = AttemptProblemDto.getAttemptProblemDto(attemptProblem);
            attemptProblemDto.setTestProblemId(number++);
            attemptProblemDtos.add(attemptProblemDto);
        }
        return attemptProblemDtos;
    }

    @Transactional
    public Long calculateTestRating(Member member, Long testId) {
        List<AttemptProblem> attemptProblems
                = attemptProblemRepository.findAttemptProblemsByTest_TestId(testId);

        Long memberRating = member.getRating();
        long rating = 0L;
        boolean allSolved = true;
        if (!attemptProblems.isEmpty()) {
            for (AttemptProblem attemptProblem : attemptProblems) {
                if (attemptProblem.getIsSolved()) {
                    Problem problem = attemptProblem.getProblem();
                    DifficultyLevel problemDifficulty = problem.getProblemDifficulty();
                    long value = DifficultyLevel.getRatingByValue(problemDifficulty);
                    value -= attemptProblem.getExecutionTime();
                    value = max(value, 50);
                    rating += value;
                }
                else
                    allSolved = false;
            }
        }
        long resultRating = (memberRating * 4 + rating) / 5;
        if (allSolved) memberRating = max(memberRating, resultRating);
        else memberRating = resultRating;
        member.setRating(memberRating);
        return memberRating;
    }

    public void saveEachCodeinAttemptProblems(AttemptCodeDto attemptCodeDto) {
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(attemptCodeDto.getTestId());
        if(attemptProblems.isEmpty()) {
            log.error("attemptCodeDto TestId = {}, ", attemptCodeDto.getTestId());
            throw new MorandiException(AttemptProblemErrorCode.ATTEMPT_PROBLEM_NOT_FOUND);
        }
        attemptProblems.forEach(attemptProblem -> {
            Long attemptProblemId = attemptProblem.getAttemptProblemId();
            attemptProblem.setSubmitCode(
                    Optional.of(attemptCodeDto.getSubmitCode().get(attemptProblemId))
                            .orElseThrow(() -> {
                                log.error("attemptProblem = {}, attemptCodeDto = {}", attemptProblem, attemptCodeDto);
                                return new MorandiException(AttemptProblemErrorCode.ATTEMPT_PROBLEM_NOT_FOUND);
                            }));


        });
        attemptProblemRepository.saveAll(attemptProblems);
        log.info("테스트 Id에 해당하는 푼 문제들의 코드를 모두 저장했습니다: {}", attemptCodeDto.getTestId());

    }
}

