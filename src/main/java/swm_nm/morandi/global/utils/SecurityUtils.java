package swm_nm.morandi.global.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.AuthErrorCode;

import java.util.List;

public class SecurityUtils {

    private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    private static final String ROLE_SWAGGER = "ROLE_SWAGGER";
    private static final Long ANONYMOUS_USER_ID = 1L;
    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new MorandiException(AuthErrorCode.SECURITY_CONTEXT_NOT_FOUND);
        }
        // 스웨거 유저일시 익명 유저 취급
        // 익명유저시 userId 0 반환
        if (!authentication.isAuthenticated()
                || CollectionUtils.containsAny(
                authentication.getAuthorities(),
                List.of(new SimpleGrantedAuthority(ROLE_ANONYMOUS),
                        new SimpleGrantedAuthority(ROLE_SWAGGER)))) {
            return ANONYMOUS_USER_ID;
        }

        try {
            return Long.valueOf(authentication.getName());
        } catch (NumberFormatException e) {
            throw new MorandiException(AuthErrorCode.SECURITY_CONTEXT_FAIL_PARSE);
        }
    }
}
