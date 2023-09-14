package swm_nm.morandi.domain.problem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BojProblem {
    private Long testProblemId;
    private Long problemId; // baekjoon problem id (바꾸면 절대 안 됨 ObjectMapper 때문에)
    private Integer level;
    private String levelToString;
}
