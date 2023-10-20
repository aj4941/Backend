package swm_nm.morandi.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum TestErrorCode implements ErrorCode {
    TEST_NOT_FOUND(HttpStatus.NO_CONTENT, "테스트를 찾을 수 없습니다."),
    CODE_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "제출 코드 유형을 찾을 수 없습니다."),
    JSON_PARSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 데이터 파싱에 실패했습니다."),
    TTL_EXPIRED(HttpStatus.NO_CONTENT, "확인하려는 코드의 TTL이 만료되었습니다."),
    KEY_NOT_FOUND(HttpStatus.NO_CONTENT, "키를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;


}