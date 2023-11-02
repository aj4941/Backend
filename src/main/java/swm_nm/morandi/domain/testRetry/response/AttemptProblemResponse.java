package swm_nm.morandi.domain.testRetry.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import swm_nm.morandi.domain.common.Language;

@Builder
@Getter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AttemptProblemResponse {
    private Long bojProblemId;
    private String pythonCode;
    private String javaCode;
    private String cppCode;
    private Language lastAccessCode;

    public void initialRetryAttemptProblemResponse(String code, Language language, Long bojProblemId) {
        switch (language) {
            case Python -> this.pythonCode = code;
            case Cpp -> this.cppCode = code;
            case Java -> this.javaCode = code;
        }
        this.bojProblemId = bojProblemId;
        this.lastAccessCode = language;
    }
}