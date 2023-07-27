package swm_nm.morandi.exception.errorcode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {
    MEMBER_DUPLICATED(HttpStatus.CONFLICT, "이미 가입된 사용자입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"유효하지 않은 인증 정보입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"인증 시간이 만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰입니다."),
    UNKNOWN_ERROR(HttpStatus.FORBIDDEN,"알 수 없는 오류" );


    private final HttpStatus httpStatus;
    private final String message;


}