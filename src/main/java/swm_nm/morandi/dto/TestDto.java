package swm_nm.morandi.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestDto {
    private Long testId;
    private LocalDateTime testDate;
    private Long testTime;
    private Integer problemCount;
    private Integer startDifficulty;
    private Integer endDifficulty;
    private String testTypename;
    private Long testRating;
}
