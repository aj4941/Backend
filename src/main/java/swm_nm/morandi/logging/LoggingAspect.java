package swm_nm.morandi.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("within(@swm_nm.morandi.logging.Logging *)")
    public void pointCut(){
    }
    @Before("pointCut()")
    public void before(JoinPoint jp)
    {
        log.info("Calling method: [{}] in class: [{}]", jp.getSignature().getName(), jp.getTarget().getClass().toString());

    }

    @After("pointCut()")
    public void after(JoinPoint jp)
    {
        log.info("Exiting method: [{}] in class: [{}]", jp.getSignature().getName(), jp.getTarget().getClass().toString());

    }
}



