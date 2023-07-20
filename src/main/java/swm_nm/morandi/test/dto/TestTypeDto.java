package swm_nm.morandi.test.dto;

import lombok.*;
import swm_nm.morandi.problem.dto.DifficultyLevel;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestTypeDto {
    private String testTypename;
    private Integer testTime;
    private Integer problemCount;
    private DifficultyLevel startDifficulty;
    private DifficultyLevel endDifficulty;
}
