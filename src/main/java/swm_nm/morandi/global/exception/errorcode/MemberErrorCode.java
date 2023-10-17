package swm_nm.morandi.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.UNAUTHORIZED,"토큰에 해당하는 사용자를 찾을 수 없습니다"),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"알 수 없는 오류" ),
    BAEKJOONID_NOT_FOUND(HttpStatus.UNAUTHORIZED,"백준 아이디를 찾을 수 없습니다"),
    EXTENSION_MEMBER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR,"사용자가 아직 가입하지 않았습니다."),
    DUPLICATED_BOJ_ID(HttpStatus.CONFLICT,"이미 등록된 백준 아이디입니다.");


    private final HttpStatus httpStatus;
    private final String message;


}