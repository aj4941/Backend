package swm_nm.morandi.domain.testRetry.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRetryResponse {
    private Long testId;
    private List<AttemptProblemResponse> retryAttemptProblems;

}