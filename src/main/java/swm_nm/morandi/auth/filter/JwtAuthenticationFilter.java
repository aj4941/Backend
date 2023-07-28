package swm_nm.morandi.auth.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import swm_nm.morandi.auth.security.AuthDetails;
import swm_nm.morandi.auth.security.JwtProvider;
import swm_nm.morandi.auth.service.AuthUserDetailService;
import swm_nm.morandi.exception.MorandiException;
import swm_nm.morandi.exception.errorcode.AuthErrorCode;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final AuthUserDetailService authUserDetailService;

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
        return null;



    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);

        if(StringUtils.hasText(token) && jwtProvider.validateToken(token))
        {
            Long userId = jwtProvider.getUserIdfromToken(token);

            AuthDetails userDetails = authUserDetailService.loadUserByUsername(userId.toString());

            //초기 정보인 백준 ID가 아직 등록되지 않았으면 예외처리 (초기값 설정 유도)
            if(userDetails.getBojId()==null)
                throw new MorandiException(AuthErrorCode.BAEKJOON_ID_NULL);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request,response);


    }
}
