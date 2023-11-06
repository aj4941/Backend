package swm_nm.morandi.domain.testStart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.decorate.cbor.CborJsonFactoryDecorator;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.practice.dto.PracticeProblemInfo;
import swm_nm.morandi.domain.practice.entity.PracticeProblem;
import swm_nm.morandi.domain.testDuring.dto.TempCodeDto;
import swm_nm.morandi.domain.testDuring.dto.TestInfo;
import swm_nm.morandi.domain.testDuring.dto.factory.TempCodeFactory;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.redis.utils.RedisKeyGenerator;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempCodeInitializer {

    private final RedisTemplate<String, Object> redisTemplate;

    private final TempCodeFactory tempCodeFactory;

    private final RedisKeyGenerator redisKeyGenerator;

    @Transactional
    public TempCodeDto initializeTempCodeCache(Tests test) {
        log.debug("Initializing temp code cache for test with ID: {}", test.getTestId());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.plusMinutes(test.getTestTime());
        Duration duration = Duration.between(now, endTime);
        long expireTime = duration.toMinutes();

        // 테스트 정보를 저장한다
        String ongoingTestKey = redisKeyGenerator.generateOngoingTestKey();
        TestInfo testInfo = TestInfo.builder()
                .testId(test.getTestId())// 테스트 아이디
                .endTime(endTime)// 테스트가 끝나는 시간
                .build();
        redisTemplate.opsForValue().set(ongoingTestKey, testInfo);//, expireTime, TimeUnit.MINUTES);
        //테스트 정보를 만료시켜버리면 테스트가 끝나버리면 key가 사라져서 테스트 정보를 몰라버리니깐 만료시키지 않는다.

        log.debug("Test info cache initialized for test with ID: {}", test.getTestId());

        // 테스트 문제별 코드를 저장한다
        String tempCodeKey = redisKeyGenerator.generateTempCodeKey(test.getTestId());
        HashOperations<String, String, TempCodeDto> hashOps = redisTemplate.opsForHash();

        TempCodeDto initialTempCode = tempCodeFactory.getTempCodeDto();

        int problemCount = test.getAttemptProblems().size();

        IntStream.rangeClosed(1, problemCount).forEach(problemNumber ->
                hashOps.put(tempCodeKey, String.valueOf(problemNumber), initialTempCode)
        );
        redisTemplate.expire(tempCodeKey, expireTime, TimeUnit.MINUTES);

        // 로깅: 초기화 완료
        log.debug("Initialization of temp code cache completed for test with ID: {}", test.getTestId());

        return initialTempCode;
    }

    @Transactional
    public void initializePracticeTempCodeCache(PracticeProblem practiceProblem) {
        log.debug("Initializing practice problem temp code cache for test with ID: {}", practiceProblem.getPracticeProblemId());

        String ongoingPracticeProblemKey = redisKeyGenerator.generateOngoingPracticeProblemKey(practiceProblem.getProblem().getBojProblemId());
        PracticeProblemInfo practiceProblemInfo = PracticeProblemInfo.builder()
                .practiceProblemId(practiceProblem.getPracticeProblemId())
                .build();
        redisTemplate.opsForValue().set(ongoingPracticeProblemKey, practiceProblemInfo);

        log.debug("practice problem info cache initialized for test with ID: {}", practiceProblemInfo.getPracticeProblemId());

        String practiceProblemTempCodeKey = redisKeyGenerator.generatePracticeProblemTempCodeKey(practiceProblemInfo.getPracticeProblemId());
        TempCodeDto initialTempCode = tempCodeFactory.getTempCodeDto();
        redisTemplate.opsForValue().set(practiceProblemTempCodeKey, initialTempCode);

        log.debug("Initialization of practice problem code cache completed for test with ID: {}", practiceProblemInfo.getPracticeProblemId());
    }
}
