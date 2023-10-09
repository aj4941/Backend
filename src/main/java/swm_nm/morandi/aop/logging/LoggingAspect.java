package swm_nm.morandi.aop.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sentry.Scope;
import io.sentry.Sentry;
import io.sentry.protocol.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import swm_nm.morandi.config.filter.CachedBodyHttpServletWrapper;
import swm_nm.morandi.global.utils.SecurityUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.net.InetAddress;
import java.net.UnknownHostException;


@Aspect
@Component
@Slf4j
public class LoggingAspect {
    private ObjectMapper mapper = new ObjectMapper();
    private String host;
    private String ip;

    @Autowired
    private EntityManager em;

    @PostConstruct
    public void init() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        this.host = addr.getHostName();
        this.ip = addr.getHostAddress();
    }

    //SQL 성능 측정 시 해당 메서드에 @Logging 어노테이션 붙여서 사용
    @Pointcut("@annotation(swm_nm.morandi.aop.annotation.Logging)")
    public void loggingPointcut() {
    }

    @Around("loggingPointcut()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        // 영속 컨텍스트 초기화
        em.clear();
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println(joinPoint.getSignature() + " 실행 시간 " + elapsedTime + "ms");
        return result;
    }

    @Around("bean(*Controller)")
    public Object controllerAroundLogging(ProceedingJoinPoint pjp) throws Throwable {
       // HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletRequest originalRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        CachedBodyHttpServletWrapper request = new CachedBodyHttpServletWrapper(originalRequest);
        //Sentry 사용자 식별을 위한 User 인스턴스 생성
        User user = new User();
        //로그인 이전의 Controller에서도 이 함수에 들어오기 때문에 Security Context에 있는 MemberId를 가져올 수 없다.
        try {
            user.setUsername(SecurityUtils.getCurrentMemberId().toString());
        } catch (Exception e) {
            user.setUsername("before Login User");
        }
        user.setIpAddress(request.getRemoteAddr());

        //Sentry 식별 사용자 정보 등록
        Sentry.setUser(user);

        String callFunction = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
        String msg = String.format("[hostname]: %s, [hostIP]: %s, [clientIP]: %s, [clientURL]: %s ",
                host, ip,
                request.getRemoteAddr(),
                request.getRequestURL().toString()
        );


        //GET 요청이 아닌 경우 Request Body를 로깅하기 위해 HttpServletRequestWrapper를 사용한다.
        //이 때, HttpServletRequestWrapper는 Request Body를 한 번 읽으면 다시 읽을 수 없기 때문에
        //읽은 내용을 저장해두고 다시 읽을 수 있도록 한다.
        //Content-Type이 application/json인 경우에만 Request Body를 로깅한다.
       if (!request.getMethod().equalsIgnoreCase("GET")&&request.getContentType() != null && request.getContentType().contains("application/json")) {

           StringBuilder requestBody = new StringBuilder();
           String line;
           BufferedReader reader = request.getReader();

           //BufferedReader를 이용해 Request Body를 읽는다.
           while ((line = reader.readLine()) != null)
               requestBody.append(line);

           System.out.println("requestBody = " + requestBody);
           log.info("[REQUEST], [{}]: {}, [RequestBody]: {},[Parameter]: {},  {}", callFunction,request.getMethod(),
                   requestBody
                   , mapper.writeValueAsString(request.getParameterMap())
                   , msg);
       }
       //GET 요청의 경우 Parameter를 로깅한다.
         else {
           log.info("[REQUEST], [callFunction]: {}, [parameter]: {}, {}", callFunction
                   , mapper.writeValueAsString(request.getParameterMap())
                   , msg);
       }
        Object result = "";
        try {
            result = pjp.proceed();
            return result;
        }
        finally {
            log.info("[RESPONSE] [callFunction]: {}, [parameter]: {}, {}", callFunction,
                    mapper.writeValueAsString(result),
                    msg);

            Sentry.configureScope(Scope::clear);

        }

    }

//
//
//    @Before("pointCut()")
//    public void before(JoinPoint jp)
//    {
//
//
//        log.info("Calling method: [{}] in class: [{}]", jp.getSignature().getName(), jp.getTarget().getClass().toString());
//
//    }
//
//    @After("pointCut()")
//    public void after(JoinPoint jp)
//    {
//        log.info("Exiting method: [{}] in class: [{}]", jp.getSignature().getName(), jp.getTarget().getClass().toString());
//
//    }
}



