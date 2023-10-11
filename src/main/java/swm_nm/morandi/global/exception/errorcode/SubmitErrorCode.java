package swm_nm.morandi.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@RequiredArgsConstructor
@Getter

public enum SubmitErrorCode implements ErrorCode {
    BAEKJOON_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"백준 서버 요청 오류"),
    LANGUAGE_CODE_NOT_FOUND(HttpStatus.FORBIDDEN,"지원하는 언어 ID를 찾을 수 없습니다."),
    BAEKJOON_LOGIN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"백준 로그인에 실패했습니다."),
    COOKIE_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR,"쿠키가 존재하지 않습니다."),;

    private final HttpStatus httpStatus;
    private final String message;


}
