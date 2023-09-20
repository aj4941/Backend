package swm_nm.morandi.domain.testStart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import swm_nm.morandi.domain.problem.dto.BojProblem;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.testExit.service.TestExitService;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testStart.dto.TestStartResponseDto;
import swm_nm.morandi.domain.testExit.entity.AttemptProblem;
import swm_nm.morandi.domain.testExit.entity.Tests;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.member.service.MemberInfoService;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.*;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestStartService {

    private final AddTestService addTestService;

    private final GetProblemsService getProblemsService;

    private final SaveProblemsService saveProblemsService;

    private final TempCodeService tempCodeService;

    private final MemberRepository memberRepository;

    private final TestRepository testRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    private final ObjectMapper objectMapper;

    private final MemberInfoService memberInfoService;

    private final TestExitService testExitService;

    public TestStartResponseDto getTestStartsData(Long testTypeId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 현재 테스트가 진행중인지 확인
        if (member.getCurrentTestId() == null)
            member.setCurrentTestId(-1L);

        // 현재 테스트가 진행중이라면
        if (member.getCurrentTestId() != -1) {
            Long currentTestId = member.getCurrentTestId();
            Tests test = testRepository.findById(currentTestId).orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));

            // 테스트 시작 시간과 현재 시간을 비교하여 남은 시간 계산
            extracted(test);

            if (test.getRemainingTime() > 0) { // 시간이 남았을경우 진행중인 테스트 반환
                return getTestStartResponseDto(currentTestId, test);
            }
            else { // 시간이 마감된 테스트 종료
                extracted(testTypeId, member, test);
            }
        }
        // 현재 진행중인 테스트가 없을 경우 아래 로직 진행
        Tests test = addTestService.startTestByTestTypeId(testTypeId, memberId);
        Long testId = test.getTestId();

        String bojId = memberInfoService.getMemberInfo().getBojId();
        List<BojProblem> bojProblems = new ArrayList<>();

        getProblemsService.getProblemsByTestType(testTypeId, bojProblems);
        getProblemsService.getProblemsByApi(testTypeId, bojId, bojProblems);

        saveProblemsService.saveAttemptProblems(memberId, testId, bojProblems);

        // 테스트 시작시 코드 캐시 초기화
        tempCodeService.initTempCodeCacheWhenTestStart(test);

        // TODO
        // 테스트 시작에 대한 ResponseDto 반환시
        // 백준의 ID가 아니라 attemptProblemID도 함께 반환하도록
        // 이후 주기적으로 Redis에 코드를 저장할 때 attemptProblemId를 반환하게

        TestStartResponseDto testStartResponseDto = getTestStartResponseDto(test, testId, bojProblems);

        return testStartResponseDto;
    }

    private static TestStartResponseDto getTestStartResponseDto(Tests test, Long testId, List<BojProblem> bojProblems) {
        List<Long> bojProblemIds = new ArrayList<>();
        for (BojProblem bojProblem : bojProblems) {
            bojProblemIds.add(bojProblem.getProblemId());
        }

        TestStartResponseDto testStartResponseDto
                = TestStartResponseDto.builder()
                .testId(testId)
                .bojProblemIds(bojProblemIds)
                .remainingTime(test.getRemainingTime())
                .build();
        return testStartResponseDto;
    }

    private static void extracted(Tests test) {
        Duration duration = Duration.between(test.getTestDate(), LocalDateTime.now());
        test.setRemainingTime(test.getTestTime() * 60 - duration.getSeconds());
    }

    private void extracted(Long testTypeId, Member member, Tests test) {
        TestCheckDto testCheckDto = TestCheckDto.builder()
                .testId(test.getTestId())
                .testTypeId(testTypeId)
                .bojId(member.getBojId())
                .build();
        testExitService.testExit(testCheckDto);
    }
    private TestStartResponseDto getTestStartResponseDto(Long currentTestId, Tests test) {
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(currentTestId);
        List<Long> bojProblemIds = attemptProblems.stream().map(AttemptProblem::getProblem)
                .map(Problem::getBojProblemId).collect(Collectors.toList());
        TestStartResponseDto testStartResponseDto
                = TestStartResponseDto.builder()
                .testId(currentTestId)
                .bojProblemIds(bojProblemIds)
                .remainingTime(test.getRemainingTime())
                .build();

        // 테스트 시작에 대한 ResponseDto 반환
        return testStartResponseDto;
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
            return checkNode.asLong() == 1L;

        } catch (NullPointerException e) {
            throw new RuntimeException("Node null 반환했습니다.");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json 파싱에 실패했습니다.");
        }
    }
}
