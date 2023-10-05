package swm_nm.morandi.domain.testDuring.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutputDto {
    private String result; // 실행 성공 여부
    private String output; // 실행 결과물
    private Double executeTime;
}
