package swm_nm.morandi.global.exception.errorcode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@Getter
public enum LockErrorCode implements ErrorCode {
    MEMBER_LOCKED(HttpStatus.CONFLICT, "한 번에 여러 번의 요청을 받을 수 없습니다. 잠시만 기다려주세요.");

    private final HttpStatus httpStatus;
    private final String message;


}