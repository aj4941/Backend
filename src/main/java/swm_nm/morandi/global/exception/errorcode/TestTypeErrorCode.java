package swm_nm.morandi.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum TestTypeErrorCode implements ErrorCode {
    TEST_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "테스트 타입을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;


}