package swm_nm.morandi.domain.testDuring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testDuring.dto.TempCode;
import swm_nm.morandi.domain.testDuring.dto.TempCodeDto;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempCodeService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveTempCode(TempCodeDto tempCodeDto) {
        String key = generateKey(tempCodeDto);
        Long remainingTTL = redisTemplate.getExpire(key);

        if (remainingTTL != null && remainingTTL > 0) {
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
        else {
            throw new MorandiException(TestErrorCode.TTL_EXPIRED);
        }
    }
    public String generateKey(TempCodeDto tempCodeDto) {
        return String.format("testId:%s:problemNumber:%s", tempCodeDto.testId, tempCodeDto.getProblemNumber());
    }

    public TempCode getTempCode(String key) {
        return Optional.ofNullable((TempCode) redisTemplate.opsForValue().get(key)).orElseThrow(
                () -> new MorandiException(TestErrorCode.KEY_NOT_FOUND)
        );
    }
}
