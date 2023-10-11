package swm_nm.morandi.domain.testDuring.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TempCodeDto {
    private String testId;
    private String problemNumber;
    private String language;
    private String code;
}
