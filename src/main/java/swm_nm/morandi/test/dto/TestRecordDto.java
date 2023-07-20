package swm_nm.morandi.test.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRecordDto {
    private LocalDateTime testDate;
    private Long testTime;
    private Integer problemCount;
    private Integer startDifficulty;
    private Integer endDifficulty;
    private String testTypename;
    private Long testRating;
    private Map<Integer, Boolean> solvedInfo = new HashMap<>();
}
