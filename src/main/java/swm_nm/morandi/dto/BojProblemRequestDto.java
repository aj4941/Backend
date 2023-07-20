package swm_nm.morandi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BojProblemRequestDto {
    private Long testTypeId;
    private Long memberId;
}
