package swm_nm.morandi.domain.testExit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testExit.dto.TestResultDto;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.TestType;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testInfo.repository.TestTypeRepository;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.testDuring.service.SolvedCheckService;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.AuthErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestTypeErrorCode;
import swm_nm.morandi.global.utils.SecurityUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestExitService {

    private final AttemptProblemRepository attemptProblemRepository;

    private final MemberRepository memberRepository;

    private final TestRepository testRepository;

    private final TestTypeRepository testTypeRepository;

    private final CalculateRatingService calculateRatingService;

    private final SolvedCheckService solvedCheckService;

    //Controller에서 사용되는 것
    public TestResultDto testExit(TestCheckDto testCheckDto)
    {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MorandiException(AuthErrorCode.MEMBER_NOT_FOUND));
        Tests test = testRepository.findById(testCheckDto.getTestId())
                .orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
        TestType testType = testTypeRepository.findTestTypeByTestTypename(test.getTestTypename())
                .orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        return testExit(testCheckDto, member, test, testType);
    }

    @Transactional
    public TestResultDto testExit(TestCheckDto testCheckDto, Member member, Tests test, TestType testType) {
        String bojId = testCheckDto.getBojId();

        solvedCheckService.checkAttemptedProblemResult(test, bojId);
        TestResultDto testResultDto = TestResultDto.builder().build();
        saveTestResult(member, test, testType, testResultDto);

        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(test.getTestId());

        List<AttemptProblemDto> attemptProblemDtos = new ArrayList<>();
        long number = 1;
        for (AttemptProblem attemptProblem : attemptProblems) {
            AttemptProblemDto attemptProblemDto = AttemptProblemDto.getAttemptProblemDto(attemptProblem);
            attemptProblemDto.setTestProblemId(number++);
            attemptProblemDtos.add(attemptProblemDto);
        }

        testResultDto.setAttemptProblemDtos(attemptProblemDtos);

        return testResultDto;
    }

    @Transactional
    public void saveTestResult(Member member, Tests test, TestType testType, TestResultDto testResultDto) {
        test.setTestStatus(TestStatus.COMPLETED);
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByTest_TestId(test.getTestId());
        long correct = attemptProblems.stream()
                .filter(AttemptProblem::getIsSolved)
                .count();
        long total = attemptProblems.size();

        // 문제별 결과 목록 저장 및 변경된 정답률 업데이트
        testType.updateAverageCorrectAnswerRate(correct / total);

        // 테스트 레이팅 저장
        test.setTestRating(calculateRatingService.calculateTestRating(member, test));

        member.setCurrentTestId(-1L);
        testResultDto.setBeforeRating(member.getRating());
        testResultDto.setAfterRating(test.getTestRating());
        testResultDto.setTestDate(test.getTestDate());
        member.setRating(test.getTestRating());

        // 테스트 결과 저장
        testTypeRepository.save(testType);
        testRepository.save(test);
        memberRepository.save(member);
    }
}
