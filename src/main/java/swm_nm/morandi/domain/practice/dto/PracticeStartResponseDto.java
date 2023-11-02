package swm_nm.morandi.domain.practice.dto;

import lombok.*;
import swm_nm.morandi.domain.testDuring.dto.PracticeProblemCodeDto;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PracticeStartResponseDto {
    private Long practiceProblemId;
    private String pythonCode;
    private String javaCode;
    private String cppCode;
    public static PracticeStartResponseDto getPracticeStartResponseDto
            (Long practiceProblemId, PracticeProblemCodeDto practiceProblemCodeDto) {
        return PracticeStartResponseDto.builder()
                .practiceProblemId(practiceProblemId)
                .pythonCode(practiceProblemCodeDto.getPythonCode())
                .javaCode(practiceProblemCodeDto.getJavaCode())
                .cppCode(practiceProblemCodeDto.getCppCode())
                .build();
    }
}
