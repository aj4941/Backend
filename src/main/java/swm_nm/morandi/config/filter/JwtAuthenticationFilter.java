package swm_nm.morandi.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import swm_nm.morandi.config.security.AuthDetails;
import swm_nm.morandi.config.security.JwtProvider;
import swm_nm.morandi.domain.auth.service.AuthUserDetailService;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.AuthErrorCode;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final AuthUserDetailService authUserDetailService;

    private final ObjectMapper mapper = new ObjectMapper();

    private String getJwtFromRequest(HttpServletRequest request) {
        // 쿠키로 넘어오는 방식
        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
        if (accessTokenCookie != null) {
            return accessTokenCookie.getValue();
        }
        // Header에 있는 방식
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        //토큰이 없으면 예외처리
        throw new MorandiException(AuthErrorCode.TOKEN_NOT_FOUND, "토큰이 없습니다.");

    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //초기 크롬 익스텐션 백준 아이디 설정은 통과하도록
        if (request.getRequestURI().startsWith("/submit/cookie")) {
            filterChain.doFilter(request,response);
            return;
        }
        //스웨거
        if(request.getRequestURI().startsWith("/swagger-ui/") || request.getRequestURI().startsWith("/v3/api-docs") || request.getRequestURI().startsWith("/swagger-resources") ){
            filterChain.doFilter(request,response);
            return;
        }
        //가입 관련 요청 통과하게
        if(request.getRequestURI().startsWith("/oauths")){
            filterChain.doFilter(request,response);
            return;
        }

        String token = getJwtFromRequest(request);

        if(StringUtils.hasText(token)&&jwtProvider.validateToken(token))
        {
            Long memberId = jwtProvider.getUserIdfromToken(token);

            AuthDetails userDetails = authUserDetailService.loadUserByUsername(memberId.toString());

            //초기 정보인 백준 ID가 아직 등록되지 않았으면 예외처리 (초기값 설정 유도)
            if (!(request.getRequestURI().equals("/members/register-info")) && userDetails.getBojId() == null) {
                String msg = String.format("[clientIP]: %s, [clientURL]: %s,",
                        request.getRemoteAddr(),
                        request.getRequestURL().toString()
                );
                log.error("[REQUEST] memberId={} 의 백준 ID가 등록되지 않았습니다. 크롬 익스텐션을 설치해주세요.  {}, [parameter]: {}",memberId, msg, mapper.writeValueAsString(request.getParameterMap()));
                throw new MorandiException(AuthErrorCode.BAEKJOON_ID_NULL);
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request,response);


    }
}
