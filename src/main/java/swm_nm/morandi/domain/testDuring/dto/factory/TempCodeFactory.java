package swm_nm.morandi.domain.testDuring.dto.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.config.configuration.InitialCodeConfig;
import swm_nm.morandi.domain.common.Language;
import swm_nm.morandi.domain.testDuring.dto.TempCodeDto;
import swm_nm.morandi.domain.testRetry.response.AttemptProblemResponse;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class TempCodeFactory {
    private final InitialCodeConfig initialCodeConfig;

    public TempCodeDto getTempCodeDto() {
        HashMap<Language, String> initialCode = initialCodeConfig.getInitialCode();

        return TempCodeDto.builder()
                .pythonCode(initialCode.get(Language.Python))
                .cppCode(initialCode.get(Language.Cpp))
                .javaCode(initialCode.get(Language.Java))
                .lastAccessCode(Language.Cpp)
                .build();
    }

    public AttemptProblemResponse getRetryAttemptProblemResponse() {
        HashMap<Language, String> initialCode = initialCodeConfig.getInitialCode();

        return AttemptProblemResponse.builder()
                .pythonCode(initialCode.get(Language.Python))
                .cppCode(initialCode.get(Language.Cpp))
                .javaCode(initialCode.get(Language.Java))
                .lastAccessCode(Language.Cpp)
                .build();
    }
}
