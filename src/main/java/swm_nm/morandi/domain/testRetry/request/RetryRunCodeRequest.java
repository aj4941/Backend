package swm_nm.morandi.domain.testRetry.request;

import lombok.*;
import swm_nm.morandi.domain.common.Language;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetryRunCodeRequest {
    private Language language;
    private String code;
    private String input;
}
