package swm_nm.morandi.redis.utils;


import org.springframework.stereotype.Component;
import swm_nm.morandi.global.utils.SecurityUtils;

@Component
public class RedisKeyGenerator {
    public String generateOngoingTestKey() {
        return String.format("testing:memberId:%s", SecurityUtils.getCurrentMemberId());
    }
    public String generateOngoingPracticeProblemKey(Long bojProblemId) {
        return String.format("practicing:memberId:%s:bojProblemId:%s", SecurityUtils.getCurrentMemberId(), bojProblemId);
    }
    public String generateTempCodeKey(Long testId) {
        return String.format("testId:%s", testId);
    }
    public String generatePracticeProblemTempCodeKey(Long practiceProblemId) {
        return String.format("practiceProblemId:%s", practiceProblemId);
    }

}
