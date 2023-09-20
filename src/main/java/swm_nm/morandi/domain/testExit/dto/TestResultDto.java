package swm_nm.morandi.domain.testExit.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultDto {
    private LocalDateTime testDate;
    private List<AttemptProblemDto> attemptProblemDtos;
}
