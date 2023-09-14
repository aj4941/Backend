package swm_nm.morandi.global.exception;


import lombok.Getter;
import swm_nm.morandi.global.exception.errorcode.ErrorCode;

@Getter
public class MorandiException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String message;

    public MorandiException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public MorandiException(ErrorCode errorCode,String message) {
        this.errorCode = errorCode;
        this.message=message;
    }
}
