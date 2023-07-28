package swm_nm.morandi.testResult.request;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptProblemDto {
    private Long problemId;
    private LocalDate testDate;
    private Integer solvedTime;
}
