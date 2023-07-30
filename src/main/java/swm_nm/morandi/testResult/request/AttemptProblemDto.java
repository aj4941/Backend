package swm_nm.morandi.testResult.request;

import lombok.*;
import swm_nm.morandi.testResult.entity.AttemptProblem;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptProblemDto {
    private Long testProblemId;
    private Boolean isSolved;
    private Long executionTime;

    public static AttemptProblemDto getAttemptProblemDto(AttemptProblem attemptProblem) {
        return AttemptProblemDto.builder()
                .isSolved(attemptProblem.getIsSolved())
                .executionTime(attemptProblem.getExecutionTime())
                .build();
    }
}
