package swm_nm.morandi.domain.testStart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testDuring.dto.TempCode;
import swm_nm.morandi.domain.testExit.entity.AttemptProblem;
import swm_nm.morandi.domain.testExit.entity.Tests;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempCodeInitializer {

    private final RedisTemplate<String, Object> redisTemplate;
    private final AttemptProblemRepository attemptProblemRepository;

    public String generateKey(Tests test, int problemNumber) {
        return String.format("testId:%s:problemNumber:%s", test.getTestId(), problemNumber);
    }
    public void initTempCodeCacheWhenTestStart(Tests test) {
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByTestOrderByAttemptProblemIdAsc(test);
        LocalDateTime now = LocalDateTime.now();
        AtomicInteger i = new AtomicInteger(1);

        attemptProblems.forEach(attemptProblem -> {
            String key = generateKey(test, i.getAndIncrement());
            // 끝나는 시간
            LocalDateTime endTime = now.plusMinutes(test.getTestTime());
            Duration duration = Duration.between(now, endTime);
            long expireTime = duration.toMinutes();

            // tempCode를 저장
            redisTemplate.opsForValue().set(key, new TempCode("initialized", endTime));

            // 테스트 남은 시간만큼 TTL을 설정한다
            redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
        });
    }
}
