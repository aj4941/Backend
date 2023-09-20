package swm_nm.morandi.domain.testExit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testExit.entity.AttemptProblem;
import swm_nm.morandi.domain.testExit.entity.TestType;
import swm_nm.morandi.domain.testExit.entity.Tests;
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
    @Transactional
    public List<AttemptProblemDto> testExit(TestCheckDto testCheckDto) {
        Long testId = testCheckDto.getTestId();
        String bojId = testCheckDto.getBojId();
        Long testTypeId = testCheckDto.getTestTypeId();

        solvedCheckService.checkAttemptedProblemResult(testId, bojId);
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

    @Transactional
    public void saveTestResult(Long testId, Long testTypeId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MorandiException(AuthErrorCode.MEMBER_NOT_FOUND));
        Tests test = testRepository.findById(testId).orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        test.setTestStatus(TestStatus.COMPLETED);
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByTest_TestId(testId);
        long correct = attemptProblems.stream()
                .filter(AttemptProblem::getIsSolved)
                .count();
        long total = attemptProblems.size();

        // 문제별 결과 목록 저장 및 변경된 정답률 업데이트
        testType.updateAverageCorrectAnswerRate(correct / total);

        // 테스트 레이팅 저장
        test.setTestRating(calculateRatingService.calculateTestRating(member, testId));

        member.setCurrentTestId(-1L);
    }
}
