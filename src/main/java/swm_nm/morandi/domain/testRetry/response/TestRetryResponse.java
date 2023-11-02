package swm_nm.morandi.domain.testRetry.response;

import lombok.*;
import swm_nm.morandi.domain.common.Language;
import swm_nm.morandi.domain.testDuring.dto.TempCodeDto;
import swm_nm.morandi.domain.testStart.dto.BojProblemDto;
import swm_nm.morandi.domain.testStart.dto.TestCodeDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRetryResponse {
    private Long testId;
    private List<RetryAttemptProblemResponse> retryAttemptProblems;

}