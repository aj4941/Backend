package swm_nm.morandi.global.exception.errorcode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AttemptProblemErrorCode implements ErrorCode {
    ATTEMPT_PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 시도 문제를 찾을 수 없습니다."),
    ATTEMPT_PROBLEM_NOT_FOUND_DURING_TEST(HttpStatus.NOT_FOUND, "해당 시도 문제를 찾을 수 없습니다. 테스트 중인 문제가 아닙니다."),
    ATTEMPT_PROBLEM_ALREADY_SOLVED(HttpStatus.ALREADY_REPORTED, "이미 풀어낸 문제입니다.")
    ;


    private final HttpStatus httpStatus;
    private final String message;


}