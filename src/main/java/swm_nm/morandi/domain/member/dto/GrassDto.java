package swm_nm.morandi.domain.member.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrassDto {

    private LocalDate testDate;
    private Integer solvedCount;
}
