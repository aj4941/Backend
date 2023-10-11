package swm_nm.morandi.domain.testStart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testDuring.dto.TempCode;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempCodeInitializer {

    private final RedisTemplate<String, Object> redisTemplate;
    private final AttemptProblemRepository attemptProblemRepository;
    public String generateKey(Tests test, int problemNumber, String language) {
        return String.format("testId:%s:problemNumber:%s:language:%s", test.getTestId(), problemNumber, language);
    }
    private List<String> languages;
    private List<String> codeLists;
    @PostConstruct
    public void initTempCodeInitializer() {
        languages = List.of("Python", "Cpp", "Java");
        String pythonCode = readCodeFromFile("codes/temp.py");
        String cppCode = readCodeFromFile("codes/temp.cpp");
        String javaCode = readCodeFromFile("codes/Main.java");
        codeLists = List.of(pythonCode, cppCode, javaCode);
    }

    public void initTempCodeCacheWhenTestStart(Tests test) {
        LocalDateTime now = LocalDateTime.now();
        // 끝나는 시간
        // tempCode를 저장
        // 테스트 남은 시간만큼 TTL을 설정한다
        int size = test.getAttemptProblems().size();
        IntStream.rangeClosed(1, size).forEach(i ->
                languages.forEach(language -> {
                    String key = generateKey(test, i, language);
                    LocalDateTime endTime = now.plusMinutes(test.getTestTime());
                    Duration duration = Duration.between(now, endTime);
                    long expireTime = duration.toMinutes();
                    redisTemplate.opsForValue().set(key, new TempCode(codeLists.get(languages.indexOf(language)), endTime));
                    redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
            })
        );
    }
    private String readCodeFromFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            log.error("Error reading code from file: " + filePath, e);
            return "";
        }
    }
}
