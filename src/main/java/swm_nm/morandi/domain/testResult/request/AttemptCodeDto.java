package swm_nm.morandi.domain.testResult.request;


import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttemptCodeDto {
    private Long testId;
    private Map<Long,String> submitCode;
}
