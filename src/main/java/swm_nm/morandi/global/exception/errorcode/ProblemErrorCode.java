package swm_nm.morandi.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ProblemErrorCode implements ErrorCode {
    PROBLEM_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "문제를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;


}