package swm_nm.morandi.domain.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swm_nm.morandi.global.utils.SecurityUtils;
import swm_nm.morandi.domain.member.dto.CurrentRatingDto;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.AttemptProblemRepository;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.member.service.MemberService;
import swm_nm.morandi.domain.problem.dto.BojProblem;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.problem.repository.ProblemRepository;
import swm_nm.morandi.domain.test.dto.*;
import swm_nm.morandi.domain.test.entity.Test;
import swm_nm.morandi.domain.test.entity.TestType;
import swm_nm.morandi.domain.test.mapper.TestRecordMapper;
import swm_nm.morandi.domain.test.repository.TestRepository;
import swm_nm.morandi.domain.test.repository.TestTypeRepository;
import swm_nm.morandi.domain.test.scheduler.TestScheduler;
import swm_nm.morandi.domain.testMemberTempCode.service.TempCodeService;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.MemberErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestTypeErrorCode;

import swm_nm.morandi.domain.testResult.entity.AttemptProblem;
import swm_nm.morandi.domain.testResult.request.AttemptProblemDto;
import swm_nm.morandi.domain.testResult.service.TestResultService;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class TestService {

    private final MemberRepository memberRepository;

    private final TestRepository testRepository;

    private final TestTypeRepository testTypeRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    private final ProblemRepository problemRepository;

    private final MemberService memberService;

    private final TestTypeService testTypeService;

    private final TestResultService testResultService;

    private final TempCodeService tempCodeService;

    private final TestScheduler testScheduler;

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
            Test test = testRepository.findById(currentTestId).orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));

            // 테스트 시작 시간과 현재 시간을 비교하여 남은 시간 계산
            Duration duration = Duration.between(test.getTestDate(), LocalDateTime.now());
            test.setRemainingTime(test.getTestTime() * 60 - duration.getSeconds());

            if (test.getRemainingTime() > 0) { // 시간이 남았을경우 진행중인 테스트 반환
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
            else { // 시간이 마감된 테스트 종료
                TestCheckDto testCheckDto = TestCheckDto.builder()
                        .testId(test.getTestId())
                        .testTypeId(testTypeId)
                        .bojId(member.getBojId())
                        .build();
                testResultService.testExit(testCheckDto);
            }
        }
        // 현재 진행중인 테스트가 없을 경우 아래 로직 진행
        Test test = startTestByTestTypeId(testTypeId, memberId);
        Long testId = test.getTestId();

        TestCheckDto testCheckDto = TestCheckDto.builder()
                .testId(testId)
                .testTypeId(testTypeId)
                .bojId(member.getBojId())
                .build();

        testScheduler.addTest(testCheckDto);
        member.setCurrentTestId(testId);

        String bojId = memberService.getBojId(memberId);
        List<BojProblem> bojProblems = new ArrayList<>();
        testTypeService.getProblemsByTestType(testTypeId, bojProblems);
        testTypeService.getProblemsByApi(testTypeId, bojId, bojProblems);



        List<Long> bojProblemIds = testResultService.saveAttemptProblems(memberId, testId, bojProblems);

        // 테스트 시작시 코드 캐시 초기화
        tempCodeService.initTempCodeCacheWhenTestStart(test);

        //TODO
        // 테스트 시작에 대한 ResponseDto 반환시
        // 백준의 ID가 아니라 attemptProblemID도 함께 반환하도록
        // 이후 주기적으로 Redis에 코드를 저장할 때 attemptProblemId를 반환하게
        TestStartResponseDto testStartResponseDto
                = TestStartResponseDto.builder()
                .testId(testId)
                .bojProblemIds(bojProblemIds)
                .remainingTime(test.getRemainingTime())
                .build();

        return testStartResponseDto;
    }


    @Transactional
    public Test startTestByTestTypeId(Long testTypeId, Long memberId) {
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(()-> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));

        Test test = Test.builder()
                .testDate(LocalDateTime.now()) // 테스트가 시작된 시간
                .testTime(testType.getTestTime())
                .problemCount(testType.getProblemCount())
                .remainingTime(testType.getTestTime() * 60L)
                .startDifficulty(testType.getStartDifficulty())
                .endDifficulty(testType.getEndDifficulty())
                .testTypename(testType.getTestTypename())
                .testRating(null)
                .testStatus(TestStatus.IN_PROGRESS)
                .member(member)
                .build();

        testRepository.save(test);

        return test;
    }
    public List<TestRecordDto> getTestRecordDtosLatest() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Pageable pageable = PageRequest.of(0, 5);
        List<Test> recentTests = testRepository.findAllByMember_MemberIdOrderByTestDateDesc(memberId, pageable);
        List<TestRecordDto> testRecordDtos = recentTests.stream().map(TestRecordMapper::convertToDto).collect(Collectors.toList());
        return testRecordDtos;
    }

    public CurrentRatingDto getCurrentRatingDto() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));
        CurrentRatingDto currentRatingDto = CurrentRatingDto.builder()
                .rating(member.getRating())
                .build();
        return currentRatingDto;
    }

    public TestRecordDto getTestRecordDtoByTestId(Long testId) {
        Test test = testRepository.findById(testId).orElseThrow(()-> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
        TestRecordDto testRecordDto = TestRecordMapper.convertToDto(test);
        List<AttemptProblem> attemptProblems
                = attemptProblemRepository.findAllByTest_TestId(testId);
        if (!attemptProblems.isEmpty()) {
            long index = 1;
            for (AttemptProblem attemptProblem : attemptProblems) {
                AttemptProblemDto attemptProblemDto =
                        AttemptProblemDto.builder()
                                .testProblemId(index++)
                                .bojProblemId(attemptProblem.getProblem().getBojProblemId())
                                .isSolved(attemptProblem.getIsSolved())
                                .executionTime(attemptProblem.getExecutionTime())
                                .build();
                testRecordDto.getAttemptProblemDto().add(attemptProblemDto);
            }
        }
        return testRecordDto;
    }

    public List<TestRatingDto> getTestRatingDtos() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        List<Test> tests = testRepository.findAllByMember_MemberId(memberId);
        List<TestRatingDto> testRatingDtos = new ArrayList<>();
        tests.stream()
                .filter(test -> test.getTestStatus() != TestStatus.IN_PROGRESS)
                .forEach(test -> {
            LocalDateTime testDate = test.getTestDate();
            String testTypename = test.getTestTypename();
            Long testRating = test.getTestRating();
            Long testId = test.getTestId();
            TestRatingDto testRatingDto = TestRatingDto.builder()
                    .testId(testId)
                    .testTypeName(testTypename)
                    .testDate(testDate)
                    .testRating(testRating)
                    .build();
            testRatingDtos.add(testRatingDto);
        });

        if (testRatingDtos.size() == 0) {
            TestRatingDto testRatingDto = TestRatingDto.builder()
                    .testId(null)
                    .testTypeName(null)
                    .testDate(null)
                    .testRating(null)
                    .build();
            testRatingDtos.add(testRatingDto);
        }

        return testRatingDtos;
    }

}
