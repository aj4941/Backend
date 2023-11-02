package swm_nm.morandi.domain.testDuring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.common.Language;
import swm_nm.morandi.domain.testDuring.dto.PracticeProblemCodeDto;
import swm_nm.morandi.domain.testDuring.dto.TempCodeDto;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testStart.dto.TestCodeDto;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;
import swm_nm.morandi.redis.utils.RedisKeyGenerator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TempCodeService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final RedisKeyGenerator redisKeyGenerator;

    public void saveTempCode(Long testId, Integer problemNumber, Language language, String code) {
        String tempCodeKey = redisKeyGenerator.generateTempCodeKey(testId);

        HashOperations<String, String, TempCodeDto> hashOps = redisTemplate.opsForHash();

        Long remainingTTL = redisTemplate.getExpire(tempCodeKey);

        if (remainingTTL != null && remainingTTL > 0) {
            TempCodeDto tempCodeDto = Optional.ofNullable(hashOps.get(tempCodeKey, String.valueOf(problemNumber)))
                    .orElseThrow(() -> new MorandiException(TestErrorCode.KEY_NOT_FOUND));


            tempCodeDto.writeCode(code,language);


            // 수정된 tempCodeDto를 Redis에 다시 저장합니다.
            hashOps.put(tempCodeKey, String.valueOf(problemNumber), tempCodeDto);

            // TTL을 다시 설정합니다. (이전 TTL값을 유지)
            redisTemplate.expire(tempCodeKey, remainingTTL, TimeUnit.SECONDS);

        }
        else {
            throw new MorandiException(TestErrorCode.TTL_EXPIRED);
        }
    }

    public List<TestCodeDto> getTempCode(Tests test)
    {
        HashOperations<String, String, TempCodeDto> hashOps = redisTemplate.opsForHash();
        String tempCodeKey = redisKeyGenerator.generateTempCodeKey(test.getTestId());

        Map<String, TempCodeDto> tempCodes = hashOps.entries(tempCodeKey);

        return tempCodes.keySet().stream().map(problemNumber-> TestCodeDto.builder()
                .cppCode(tempCodes.get(problemNumber).getCppCode())
                .pythonCode(tempCodes.get(problemNumber).getPythonCode())
                .javaCode(tempCodes.get(problemNumber).getJavaCode())
                .problemNumber(Integer.parseInt(problemNumber))
                .build()).collect(Collectors.toList());
    }

    public PracticeProblemCodeDto getPracticeProblemTempCode(Long practiceProblemId) {
        String practiceProblemTempCodeKey = redisKeyGenerator.generatePracticeProblemTempCodeKey(practiceProblemId);
        TempCodeDto tempCodeDto = (TempCodeDto) redisTemplate.opsForValue().get(practiceProblemTempCodeKey);
        PracticeProblemCodeDto practiceProblemCodeDto = PracticeProblemCodeDto.getPracticeProblemCodeDto(tempCodeDto);
        return practiceProblemCodeDto;
    }
}
