package swm_nm.morandi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BojProblem {
    private Long testProblemId;
    private Long problemId; // baekjoon problem id
    private Integer level;
    private String levelToString;
}
