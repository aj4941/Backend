package swm_nm.morandi.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum TestTypeErrorCode implements ErrorCode {
    TEST_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "테스트 타입을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;


}