package swm_nm.morandi.domain.test.scheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import swm_nm.morandi.domain.test.dto.TestCheckDto;
import swm_nm.morandi.domain.test.dto.TestStatus;
import swm_nm.morandi.domain.test.entity.Test;
import swm_nm.morandi.domain.test.repository.TestRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;
import swm_nm.morandi.domain.testResult.service.TestResultService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestScheduler {

    private final TestRepository testRepository;

    private final TestResultService testResultService;
    private final Set<TestCheckDto> testSet = new HashSet<>();
    public void addTest(TestCheckDto testCheckDto) {
        log.info("Scheduler add testID : " + testCheckDto.getTestId());
        testSet.add(testCheckDto);
    }
    @Scheduled(fixedRate = 60000)
    public void callApiPeriodically() {
        List<TestCheckDto> deleteList = new ArrayList<>();
        testSet.forEach(testCheckDto -> {
            log.info("Scheduler call testID : " + testCheckDto.getTestId());
            Long testId = testCheckDto.getTestId();
            Test test = testRepository.findById(testId)
                    .orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
            if (test.getTestStatus() == TestStatus.COMPLETED) {
                log.info("Scheduler Completed testID : " + testCheckDto.getTestId());
                deleteList.add(testCheckDto);
                return;
            }
            Duration duration = Duration.between(test.getTestDate(), LocalDateTime.now());
            Long minutes = duration.toMinutes();
            if (minutes > test.getTestTime()) {
                log.info("Scheduler exceed minutes testID : " + testCheckDto.getTestId());
                deleteList.add(testCheckDto);
                return;
            }
            testResultService.checkAttemptedProblemResult(testCheckDto.getTestId(), testCheckDto.getBojId());
        });

        deleteList.forEach(testSet::remove);
    }
}
