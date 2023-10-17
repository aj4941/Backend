package swm_nm.morandi.global.exception.errorcode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {
    MEMBER_DUPLICATED(HttpStatus.CONFLICT, "이미 가입된 사용자입니다."),
    MEMBER_NOT_FOUND(HttpStatus.UNAUTHORIZED,"사용자를 찾을 수 없습니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"유효하지 않은 인증 정보입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"인증 시간이 만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰을 찾을 수 없습니다"),
    UNKNOWN_ERROR(HttpStatus.FORBIDDEN,"알 수 없는 오류" ),
    SSO_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,"SSO의 Access Token을 가져올 수 없습니다." ),
    SSO_USERINFO(HttpStatus.UNAUTHORIZED,"SSO에서 사용자 정보를 가져올 수 없습니다" ),
    SSO_SERVER_ERROR(HttpStatus.UNAUTHORIZED,"SSO서버와 통신할 수 없습니다." ),
    BAEKJOON_ID_NULL(HttpStatus.FORBIDDEN,"백준 ID는 크롬 익스텐션을 통해 필수로 등록해야합니다. " ),
    INVALID_SOCIAL_TYPE(HttpStatus.UNAUTHORIZED,"지원되지 않는 OAuth provider 입니다.");


    private final HttpStatus httpStatus;
    private final String message;


}