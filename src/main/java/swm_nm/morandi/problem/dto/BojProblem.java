package swm_nm.morandi.problem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BojProblem {
    private Long testProblemId;
    private Long problemId; // baekjoon problem id
    private Integer level;
    private String levelToString;
}
