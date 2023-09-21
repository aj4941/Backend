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
import swm_nm.morandi.domain.testExit.entity.TestType;
import swm_nm.morandi.domain.testExit.service.TestExitService;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testInfo.repository.TestTypeRepository;
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
public class TestStartUseCase {

    private final AddTestService addTestService;
    private final GetProblemsService getProblemsService;
    private final SaveProblemsService saveProblemsService;
    private final TempCodeInitializer tempCodeInitializer;
    private final MemberInfoService memberInfoService;
    private final TestTypeRepository testTypeRepository;
    private final TestProgressCheckService testProgressCheckService;

    private final MemberRepository memberRepository;
    private final AttemptProblemRepository attemptProblemRepository;

    //이미 테스트 중인지 확인


    public TestStartResponseDto getTestStartsData(Long testTypeId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));

        //TODO Redis 이용하여
        //현재 테스트가 진행중인지 확인하도록
        //이미 테스트 중인지 확인
        Tests test = testProgressCheckService.isTestinProgress(member);
        if(test!=null)
        {
            return getTestStartResponseDto(member.getCurrentTestId(), test);
        }


        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        // 현재 진행중인 테스트가 없을 경우 테스트 타입에 맞는 테스트 시작
        test = addTestService.startTestByTestTypeId(testType, member);

        String bojId = memberInfoService.getMemberInfo().getBojId();
        List<BojProblem> bojProblems = new ArrayList<>();

        // 테스트 시작시 문제 가져오기
        getProblemsService.getProblemsByTestType(testType, bojProblems);

        // API로 문제 가져오기
        getProblemsService.getProblemsByApi(testType, bojId, bojProblems);

        // 테스트 시작시 문제 저장
        saveProblemsService.saveAttemptProblems(member, test, bojProblems);

        // 테스트 시작시 코드 캐시 초기화
        tempCodeInitializer.initTempCodeCacheWhenTestStart(test);



        return getTestStartResponseDto(test, bojProblems);
    }

    private static TestStartResponseDto getTestStartResponseDto(Tests test, List<BojProblem> bojProblems) {
        List<Long> bojProblemIds = new ArrayList<>();
        for (BojProblem bojProblem : bojProblems) {
            bojProblemIds.add(bojProblem.getProblemId());
        }

        return TestStartResponseDto.builder()
                .testId(test.getTestId())
                .bojProblemIds(bojProblemIds)
                .remainingTime(test.getRemainingTime())
                .build();
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
}
