package swm_nm.morandi.domain.customTest.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomTestResponse {
    private Long customTestId;
    private String testTypename;
    private String bojId;
    private Long memberId;
}
