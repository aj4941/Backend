package swm_nm.morandi.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"토큰에 해당하는 사용자를 찾을 수 없습니다"),
    UNKNOWN_ERROR(HttpStatus.FORBIDDEN,"알 수 없는 오류" ),
    BAEKJOONID_NOT_FOUND(HttpStatus.NOT_FOUND,"백준 아이디를 찾을 수 없습니다");


    private final HttpStatus httpStatus;
    private final String message;


}