package swm_nm.morandi.domain.testExit.dto;


import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttemptCodeRequest {
    private Long testId;
    private Map<Long,String> submitCode;
}
