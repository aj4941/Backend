package swm_nm.morandi.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCheckDto {
    private Long testId;
    private String bojId;
    private Long testTypeId;
}
