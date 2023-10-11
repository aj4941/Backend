package swm_nm.morandi.domain.testExit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.problem.dto.DifficultyLevel;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;

import javax.transaction.Transactional;
import java.util.List;

import static java.lang.Math.max;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculateRatingService {

    private final AttemptProblemRepository attemptProblemRepository;

    @Transactional
    public Long calculateTestRating(Member member, Tests test) {
        List<AttemptProblem> attemptProblems
                = attemptProblemRepository.findAttemptProblemsByTest_TestId(test.getTestId());
        Integer problemCount = test.getProblemCount();
        long rating = getRating(problemCount);
        long addRating = 0L;
        Long memberRating = member.getRating();
        boolean allSolved = true;
        if (!attemptProblems.isEmpty()) {
            for (AttemptProblem attemptProblem : attemptProblems) {
                if (attemptProblem.getIsSolved()) {
                    Problem problem = attemptProblem.getProblem();
                    DifficultyLevel problemDifficulty = problem.getProblemDifficulty();
                    long value = DifficultyLevel.getRatingByValue(problemDifficulty);
                    value -= attemptProblem.getExecutionTime() / 2;
                    value = max(value, 20L);
                    addRating += value;
                }
                else
                    allSolved = false;
            }
        }
        rating = (addRating == 0) ? 500L : rating + addRating;
        long resultRating = (memberRating * 4 + rating) / 5; // 사용자 현재 레이팅 반영
        if (allSolved) memberRating = max(memberRating, resultRating);
        else memberRating = resultRating;
        member.setRating(memberRating);
        test.setOriginRating(rating); // 순수 테스트 레이팅 결과
        return memberRating;
    }
    private static long getRating(Integer problemCount) {
        long rating = 0L;
        if (problemCount == 2) rating = 1460L;
        if (problemCount == 3) rating = 1390L;
        if (problemCount == 4) rating = 1320L;
        if (problemCount == 5) rating = 1250L;
        if (problemCount == 6) rating = 1180L;
        if (problemCount == 7) rating = 1110L;
        return rating;
    }
}
