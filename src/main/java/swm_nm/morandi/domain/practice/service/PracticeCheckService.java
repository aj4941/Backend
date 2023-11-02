package swm_nm.morandi.domain.practice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.practice.dto.PracticeProblemInfo;
import swm_nm.morandi.domain.practice.entity.PracticeProblem;
import swm_nm.morandi.domain.practice.repository.PracticeProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.PracticeProblemErrorCode;
import swm_nm.morandi.redis.utils.RedisKeyGenerator;

@Service
@RequiredArgsConstructor
@Slf4j
public class PracticeCheckService {

    private final RedisKeyGenerator redisKeyGenerator;

    private final RedisTemplate<String, Object> redisTemplate;
    public PracticeProblemInfo getPracticeProblemInfo() {
        String ongoingPracticeProblemKey = redisKeyGenerator.generateOngoingPracticeProblemKey();
        PracticeProblemInfo practiceProblemInfo
                = (PracticeProblemInfo) redisTemplate.opsForValue().get(ongoingPracticeProblemKey);
        return practiceProblemInfo;
    }
}
