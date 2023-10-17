package swm_nm.morandi.domain.testStart.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestStartResponseDto {
    private Long testId;
    private List<Long> bojProblemIds = new ArrayList<>();
    private List<TestCodeDto> testCodeDtos = new ArrayList<>();
    private Long remainingTime;
}
