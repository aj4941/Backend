package swm_nm.morandi.domain.codeSubmit.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AttemptProblemResultDto {

    @NotNull(message = "testId는 null일 수 없습니다, 정수형")
    public Long testId;

    @NotNull(message = "bojProblemId는 null일 수 없습니다, 정수형")
    public Long bojProblemId;

    @NotNull(message = "executionTime은 null일 수 없습니다, 정수형")
    public Long executionTime;

    @NotNull(message = "isSolved는 null일 수 없습니다, 불린형")
    public Boolean isSolved;
}
