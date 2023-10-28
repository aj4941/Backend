package swm_nm.morandi.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@RequiredArgsConstructor
@Getter

public enum SubmitErrorCode implements ErrorCode {
    BAEKJOON_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"백준 서버 요청 오류"),
    LANGUAGE_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST,"지원하는 언어 ID를 찾을 수 없습니다."),
    BAEKJOON_LOGIN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"백준 로그인에 실패했습니다. 쿠키를 다시 보내주세요"),
    COOKIE_NOT_EXIST(HttpStatus.BAD_REQUEST,"쿠키가 존재하지 않습니다. 익스텐션을 실행해 주세요"),
    BAEKJOON_INVALID_ID(HttpStatus.BAD_REQUEST,"초기에 등록한 백준 ID로 로그인 해주세요"),
    BAEKJOON_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"알 수 없는 오류가 발생했습니다"),
    INVALID_BOJPROBLEM_NUMBER(HttpStatus.BAD_REQUEST,"유효하지 않은 문제 번호입니다"),
    CANT_FIND_SOLUTION_ID(HttpStatus.BAD_REQUEST,"Solution ID를 찾을 수 없습니다"),
    NULL_POINTER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"백준 Response NullPointerException"),
    TEST_NOT_EXIST(HttpStatus.BAD_REQUEST,"저장하려는 문제의 테스트가 존재하지 않습니다")
    ;

    //TODO
    //크롬익스텐션에서 fetch요청 시 forbidden오면 저장된 쿠키 null로 변경하고, 다시 로그인하라는 메시지 띄우기
    //익스텐션 레벨에서 마지막으로 확인한 시간을 알아내서 너무 오래됐으면 다시 쿠키 보내기

    private final HttpStatus httpStatus;
    private final String message;


}
