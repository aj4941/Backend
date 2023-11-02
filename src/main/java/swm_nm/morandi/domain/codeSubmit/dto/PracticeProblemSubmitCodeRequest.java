package swm_nm.morandi.domain.codeSubmit.dto;

import lombok.Getter;
import lombok.Setter;
import swm_nm.morandi.domain.common.Language;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class PracticeProblemSubmitCodeRequest {

    @NotNull(message = "연습 문제 기본키를 입력해주세요.")
    private Long practiceProblemId;
    @NotNull(message = "백준 문제 번호를 입력해주세요")
    private String bojProblemId;

    @NotNull(message = "언어를 입력해주세요")
    private Language language;

    @NotNull(message = "소스코드를 입력해주세요")
    private String sourceCode;

}
