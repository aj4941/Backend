package swm_nm.morandi.domain.testRetry.request;

import lombok.Getter;
import lombok.Setter;
import swm_nm.morandi.domain.common.Language;

import java.util.List;

@Getter
@Setter
public class RetryRunTestCaseRequest {
    private Language language;
    private String code;
    private List<String> input;
    private List<String> output;
}
