package swm_nm.morandi.domain.test.dto;

import lombok.*;
import swm_nm.morandi.domain.problem.dto.DifficultyLevel;
import swm_nm.morandi.domain.testResult.request.AttemptProblemDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TestRecordDto {
    private LocalDateTime testDate;
    private Long testTime;
    private Integer problemCount;
    private DifficultyLevel startDifficulty;
    private DifficultyLevel endDifficulty;
    private String testTypename;
    private Long testRating;
    private List<AttemptProblemDto> attemptProblemDto;
}
