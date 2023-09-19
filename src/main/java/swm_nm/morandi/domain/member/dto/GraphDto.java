package swm_nm.morandi.domain.member.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GraphDto {
    private String algorithmName;
    private Long solvedRate;
}
