package swm_nm.morandi.domain.testExit.dto;

import lombok.*;
import swm_nm.morandi.domain.testExit.entity.AttemptProblem;

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
