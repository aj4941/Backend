package swm_nm.morandi.domain.testExit.dto;

import lombok.*;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptProblemDto {
    private Long testProblemId;
    private Long bojProblemId;
    private Boolean isSolved;
    private String executionTime;

    public static AttemptProblemDto getAttemptProblemDto(AttemptProblem attemptProblem) {
        Long attemptProblemExecutionTime = attemptProblem.getExecutionTime();
        String executionTime = (attemptProblemExecutionTime == null)
                ? null : String.format("%d:%02d", attemptProblemExecutionTime / 60, attemptProblemExecutionTime % 60);
        return AttemptProblemDto.builder()
                .bojProblemId(attemptProblem.getProblem().getBojProblemId())
                .isSolved(attemptProblem.getIsSolved())
                .executionTime(executionTime)
                .build();
    }

    public static List<AttemptProblemDto> getAttemptProblemDtoList(List<AttemptProblem> attemptProblems) {
        return attemptProblems.stream().map(AttemptProblemDto::getAttemptProblemDto
        ).toList();

    }
}
