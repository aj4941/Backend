package swm_nm.morandi.domain.testDuring.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestInputData {
    private String language;
    private String code;
    private List<String> input;
    private List<String> output;
}