package swm_nm.morandi.domain.testDuring.dto;

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
public class TempCodeDto {
    private String pythonCode;
    private String javaCode;
    private String cppCode;

    private Language lastAccessCode;

    public void writeCode(String code, Language language) {
        switch (language) {
            case Python -> this.pythonCode = code;
            case Cpp -> this.cppCode = code;
            case Java -> this.javaCode = code;
        }
        log.error("writeCode"+code);
        this.lastAccessCode = language;
    }


}
