package swm_nm.morandi.test.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRatingDto {
    private LocalDate testDate;
    private String testTypeName;
    private Long testRating;
}
