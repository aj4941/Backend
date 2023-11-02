package swm_nm.morandi.domain.testDuring.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PracticeProblemCodeDto {
    private String pythonCode;
    private String cppCode;
    private String javaCode;
    public static PracticeProblemCodeDto getPracticeProblemCodeDto(TempCodeDto tempCodeDto) {
        return PracticeProblemCodeDto.builder()
                .pythonCode(tempCodeDto.getPythonCode())
                .cppCode(tempCodeDto.getCppCode())
                .javaCode(tempCodeDto.getJavaCode())
                .build();
    }
}
