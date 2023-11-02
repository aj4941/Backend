package swm_nm.morandi.domain.practice.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PracticeProblemCode {
    private String pythonCode;
    private String cppCode;
    private String javaCode;
}
