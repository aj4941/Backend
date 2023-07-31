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
    private Long bojProblemId;
    private Boolean isSolved;
    private Long executionTime;

    public static AttemptProblemDto getAttemptProblemDto(AttemptProblem attemptProblem) {
        return AttemptProblemDto.builder()
                .bojProblemId(attemptProblem.getProblem().getBojProblemId())
                .isSolved(attemptProblem.getIsSolved())
                .executionTime(attemptProblem.getExecutionTime())
                .build();
    }
}
