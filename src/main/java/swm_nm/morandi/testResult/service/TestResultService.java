package swm_nm.morandi.testResult.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.auth.security.SecurityUtils;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.member.repository.AttemptProblemRepository;
import swm_nm.morandi.member.repository.MemberRepository;
import swm_nm.morandi.problem.domain.Problem;
import swm_nm.morandi.problem.dto.DifficultyLevel;
import swm_nm.morandi.test.domain.Test;
import swm_nm.morandi.test.domain.TestType;
import swm_nm.morandi.test.dto.TestStatus;
import swm_nm.morandi.test.repository.TestRepository;
import swm_nm.morandi.test.repository.TestTypeRepository;
import swm_nm.morandi.testResult.entity.AttemptProblem;
import swm_nm.morandi.testResult.request.TestResultDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.max;

@Service
@RequiredArgsConstructor
public class TestResultService {
    private final MemberRepository memberRepository;

    private final TestTypeRepository testTypeRepository;

    private final TestRepository testRepository;

    private final AttemptProblemService attemptProblemService;

    private final AttemptProblemRepository attemptProblemRepository;

    @Transactional
    public void saveTestResult(Long testId, Long testTypeId){
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        Test test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("테스트를 찾을 수 없습니다."));
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new RuntimeException("테스트 유형을 찾을 수 없습니다."));
        test.setTestStatus(TestStatus.COMPLETED);
        List<AttemptProblem> attemptProblems = new ArrayList<>();
        long correct = attemptProblems.stream()
                .filter(AttemptProblem::getIsSolved)
                .count();
        long total = attemptProblems.size();
        //문제별 결과 목록 저장 및 변경된 정답률 업데이트
        testType.updateAverageCorrectAnswerRate((double)correct / (double)total);

        //테스트 레이팅 저장
        test.setTestRating(calculateTestRating(member, testId));



    }

    @Transactional
    public Long calculateTestRating(Member member, Long testId) {
        Optional<List<AttemptProblem>> resAttemptProblems
                = attemptProblemRepository.findAttemptProblemsByTest_TestId(testId);

        Long memberRating = member.getRating();
        long rating = 0L;
        boolean allSolved = true;
        if (resAttemptProblems.isPresent()) {

            List<AttemptProblem> attemptProblems = resAttemptProblems.get();


            System.out.println("attemptProblems.size() = " + attemptProblems.size());

            for (AttemptProblem attemptProblem : attemptProblems) {
                System.out.println("attemptProblem.toString() = " + attemptProblem.toString());
            }



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
}

