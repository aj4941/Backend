package swm_nm.morandi.domain.testStart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testDuring.service.SolvedCheckService;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckAttemptProblemService {

    private final TestRepository testRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    private final SolvedCheckService solvedCheckService;
    @Transactional
    public void checkAttemptedProblemResult(Long testId, String bojId) {
        Tests test = testRepository.findById(testId).orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(testId);

        attemptProblems.stream()
                .filter(attemptProblem -> !attemptProblem.getIsSolved())
                .filter(attemptProblem -> solvedCheckService.isSolvedProblem(attemptProblem, bojId))
                .forEach(attemptProblem -> {
                    Duration duration = Duration.between(test.getTestDate(), LocalDateTime.now());
                    Long minutes = duration.toMinutes();
                    if (minutes <= test.getTestTime()) {
                        attemptProblem.setIsSolved(true);
                        attemptProblem.setExecutionTime(minutes);
                    }
                });
    }
}
