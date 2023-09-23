package swm_nm.morandi.domain.testRecord.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GraphDto {
    private String algorithmName;
    private Long solvedRate;
}
