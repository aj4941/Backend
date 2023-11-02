package swm_nm.morandi.domain.testRetry.request;


import lombok.Getter;
import lombok.Setter;
import swm_nm.morandi.domain.common.Language;

@Getter @Setter
public class RetrySubmitRequest {
    private Long testId;
    private Long bojProblemId;
    private String sourceCode;
    private Language language;
}
