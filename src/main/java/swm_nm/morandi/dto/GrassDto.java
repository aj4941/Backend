package swm_nm.morandi.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrassDto {

    private LocalDate testDate;
    private Integer solvedCount;
}
