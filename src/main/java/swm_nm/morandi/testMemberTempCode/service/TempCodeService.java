package swm_nm.morandi.testMemberTempCode.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import swm_nm.morandi.exception.MorandiException;
import swm_nm.morandi.exception.errorcode.TestErrorCode;
import swm_nm.morandi.member.repository.AttemptProblemRepository;
import swm_nm.morandi.test.entity.Test;
import swm_nm.morandi.testMemberTempCode.dto.TempCodeDto;
import swm_nm.morandi.testMemberTempCode.entity.TempCode;
import swm_nm.morandi.testResult.entity.AttemptProblem;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TempCodeService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final AttemptProblemRepository attemptProblemRepository;

    // key를 생성 후, 시작 시점에 tempCode를 저장하여 TTL을 설정한다
    // 그래서 시험이 끝나는 시간을 보장할 수 있음
    public void initTempCodeCacheWhenTestStart(Test test){
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByTest(test);
        LocalDateTime now = LocalDateTime.now();

        attemptProblems.forEach(attemptProblem-> {
            String key = generateKey(test, attemptProblem);
            //끝나는 시간
            LocalDateTime endTime = now.plusMinutes(test.getTestTime());
            Duration duration = Duration.between(now, endTime);
            long expireTime = duration.toMinutes();

            // tempCode를 저장한다
            redisTemplate.opsForValue().set(key, new TempCode("initialized", endTime));
            // 테스트 남은 시간만큼 TTL을 설정한다
            redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);

        });

    }
    public void saveTempCode(TempCodeDto tempCodeDto) {
        String key = generateKey(tempCodeDto);
        Long remainingTTL = redisTemplate.getExpire(key);

        if (!(remainingTTL == null || remainingTTL <= 0)) {
            TempCode tempCode = Optional.ofNullable((TempCode) redisTemplate.opsForValue().get(key))
                    .orElseThrow(() -> new MorandiException(TestErrorCode.KEY_NOT_FOUND));

            tempCode.setCode(tempCodeDto.code);

            // 테스트 시작 시간으로부터 남은 시간을 계산한다
            Duration duration = Duration.between(LocalDateTime.now(), tempCode.endTime);
            long expireTime = duration.toMinutes();

            // tempCode를 다시 저장한다
            redisTemplate.opsForValue().set(key, tempCode);
            // 테스트 남은 시간만큼 TTL을 설정한다
            redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
        }
        else
        {
            throw new MorandiException(TestErrorCode.TTL_EXPIRED);
        }
    }

    public TempCode getTempCode(String key) {
        return Optional.ofNullable((TempCode) redisTemplate.opsForValue().get(key)).orElseThrow(
                () -> new MorandiException(TestErrorCode.KEY_NOT_FOUND)
        );
    }

    public String generateKey(Test test, AttemptProblem attemptProblem) {
        return String.format("tests:%s:problems:%s",test.getTestId(), attemptProblem.getAttemptProblemId());
    }
    public String generateKey(TempCodeDto tempCodeDto) {
        return String.format("tests:%s:problems:%s", tempCodeDto.testId, tempCodeDto.attemptProblemId);
    }
}
