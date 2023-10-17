package swm_nm.morandi.domain.testStart.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCodeDto {
    private Long problemNumber;
    private String pythonCode;
    private String cppCode;
    private String javaCode;
}
