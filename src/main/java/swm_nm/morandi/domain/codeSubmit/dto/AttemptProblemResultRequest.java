package swm_nm.morandi.domain.codeSubmit.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class AttemptProblemResultRequest {

    @NotNull(message = "testId는 null일 수 없습니다, 정수형")
    private Long testId;

    @NotNull(message = "bojProblemId는 null일 수 없습니다, 정수형")
    private Long bojProblemId;

}
