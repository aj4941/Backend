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
public class UserLockAspect {

    private final StringRedisTemplate redisTemplate;
    private final String USER_LOCK_KEY = "userLock";
    private final Integer USER_LOCK_TTL = 5;

    @Pointcut("@annotation(swm_nm.morandi.aop.annotation.UserLock)")
    public void userLockPointcut() {
    }

    @Around("userLockPointcut()")
    public Object userLock(ProceedingJoinPoint joinPoint)  throws Throwable {
        Long userId = SecurityUtils.getCurrentMemberId();
        String userLockKey = String.format("%s:%d", USER_LOCK_KEY,userId);
        boolean locked = false;
        try {
            if (tryLock(userLockKey)) {
                locked = true;
                return joinPoint.proceed();
            } else {
                throw new MorandiException(LockErrorCode.USER_LOCKED);
            }
        } finally {
            if(locked) {
                unlock(userLockKey);
            }
        }
    }

    private Boolean tryLock(String key) {
        return redisTemplate.opsForValue().setIfAbsent(key, "locked", USER_LOCK_TTL, TimeUnit.SECONDS);
    }

    private void unlock(String key) {
        redisTemplate.delete(key);
    }

}

