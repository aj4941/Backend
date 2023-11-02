package swm_nm.morandi.domain.testDuring.dto;

import lombok.*;
import swm_nm.morandi.domain.common.Language;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputData {
    private Long practiceProblemId;
    private Long testId;
    private Integer problemNumber;
    private Language language;
    private String code;
    private String input;
}
