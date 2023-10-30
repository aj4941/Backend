package swm_nm.morandi.aop.lock;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.LockErrorCode;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class MemberLockAspect {

    private final StringRedisTemplate redisTemplate;
    private final String MEMBER_LOCK_KEY = "memberLock";
    private final Integer MEMBER_LOCK_TTL = 5;

    @Pointcut("@annotation(swm_nm.morandi.aop.annotation.MemberLock)")
    public void memberLockPointcut() {
    }

    @Around("memberLockPointcut()")
    public Object MEMBERLock(ProceedingJoinPoint joinPoint)  throws Throwable {
        Long memberId = SecurityUtils.getCurrentMemberId();
        String memberLockKey = String.format("%s:%d", MEMBER_LOCK_KEY,memberId);
        boolean locked = false;
        try {
            if (tryLock(memberLockKey)) {
                locked = true;
                return joinPoint.proceed();
            } else {
                throw new MorandiException(LockErrorCode.MEMBER_LOCKED);
            }
        } finally {
            if(locked) {
                unlock(memberLockKey);
            }
        }
    }

    private Boolean tryLock(String key) {
        return redisTemplate.opsForValue().setIfAbsent(key, "locked", MEMBER_LOCK_TTL, TimeUnit.SECONDS);
    }

    private void unlock(String key) {
        redisTemplate.delete(key);
    }

}

