package swm_nm.morandi.domain.codeSubmit.dto;


import lombok.Getter;
import lombok.Setter;
import swm_nm.morandi.domain.codeSubmit.constants.SubmitConstants;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.SubmitErrorCode;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SubmitCodeDto {

    @NotNull(message = "문제 번호를 입력해주세요")
    public String bojProblemId;

    @NotNull(message = "언어를 입력해주세요")
    public String language;

    @NotNull(message = "소스코드를 입력해주세요")
    public String sourceCode;

    public String getLanguageId() {
        try {
            return SubmitConstants.valueOf(this.language).getLanguageId();
        } catch (IllegalArgumentException e) {
            throw new MorandiException(SubmitErrorCode.LANGUAGE_CODE_NOT_FOUND);
        }
    }
}
