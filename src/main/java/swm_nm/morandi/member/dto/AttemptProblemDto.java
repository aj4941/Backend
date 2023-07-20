package swm_nm.morandi.member.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptProblemDto {
    private Boolean isSolved;
    private LocalDate testDate;
}
