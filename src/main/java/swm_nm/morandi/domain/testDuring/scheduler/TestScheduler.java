package swm_nm.morandi.domain.testDuring.scheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testStart.service.CheckAttemptProblemService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestScheduler {

    private final TestRepository testRepository;

    private final CheckAttemptProblemService checkAttemptProblemService;

    private final TestMapManager testMapManager;
    public void addTest(TestCheckDto testCheckDto) {
        log.info("Scheduler add testID : " + testCheckDto.getTestId());
        testMapManager.addTest(testCheckDto);
    }
    @Scheduled(fixedRate = 60000)
    public void callApiPeriodically() {
        List<TestCheckDto> deleteList = new ArrayList<>();
        ConcurrentHashMap<Long, TestCheckDto> testMap = testMapManager.getTestMap();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        testMap.forEach((testId, testCheckDto) -> {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        log.info("Scheduler call testID : " + testId);
                        Optional<Tests> result = testRepository.findById(testId);
                        if (result.isEmpty()) {
                            log.info("Scheduler Not Found testID : " + testCheckDto.getTestId());
                            deleteList.add(testCheckDto);
                        } else {
                            Tests test = result.get();
                            if (test.getTestStatus() == TestStatus.COMPLETED) {
                                log.info("Scheduler Completed testID : " + testId);
                                deleteList.add(testCheckDto);
                                return;
                            }
                            Duration duration = Duration.between(test.getTestDate(), LocalDateTime.now());
                            Long minutes = duration.toMinutes();
                            if (minutes > test.getTestTime()) {
                                log.info("Scheduler exceed minutes testID : " + testId);
                                deleteList.add(testCheckDto);
                                return;
                            }
                            checkAttemptProblemService.checkAttemptedProblemResult(test, testCheckDto.getBojId());
                        }
                    }, executorService);
            futures.add(future);
        });

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        deleteList.forEach(testCheckDto ->
                testMap.remove(testCheckDto.getTestId(), testCheckDto));
    }
}
