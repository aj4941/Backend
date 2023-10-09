package swm_nm.morandi.domain.codeSubmit.dto;


import lombok.Getter;
import lombok.Setter;
import swm_nm.morandi.domain.codeSubmit.constants.SubmitConstants;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.SubmitErrorCode;

@Getter
@Setter
public class SubmitCodeDto {
    public String cookie;
    public String problemId;
    public String language;
    public String sourceCode;

    public String getLanguageId() {
        try {
            return SubmitConstants.valueOf(this.language).getLanguageId();
        } catch (IllegalArgumentException e) {
            throw new MorandiException(SubmitErrorCode.LANGUAGE_CODE_NOT_FOUND);
        }
    }
}
