package swm_nm.morandi.domain.testDuring.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestInputData {
    private String language;
    private String code;
    private String input;
}