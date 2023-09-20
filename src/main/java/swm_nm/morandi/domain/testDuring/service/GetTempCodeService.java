package swm_nm.morandi.domain.testDuring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testDuring.dto.TempCode;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetTempCodeService {

    private final RedisTemplate redisTemplate;
    public TempCode getTempCode(String key) {
        return Optional.ofNullable((TempCode) redisTemplate.opsForValue().get(key)).orElseThrow(
                () -> new MorandiException(TestErrorCode.KEY_NOT_FOUND)
        );
    }
}
