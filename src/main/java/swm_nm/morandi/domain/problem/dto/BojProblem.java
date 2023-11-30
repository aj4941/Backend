package swm_nm.morandi.domain.problem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BojProblem {
    private Long testProblemId;
    private Long problemId; // baekjoon problem id (바꾸면 절대 안 됨 ObjectMapper 때문에)
    private String startLevel;
    private String endLevel;
    public static List<BojProblem> initBojProblems(List<DifficultyRange> difficultyRanges) {
        List<BojProblem> bojProblems = new ArrayList<>();
        IntStream.range(0, difficultyRanges.size()).forEach(i -> {
            String startLevel = difficultyRanges.get(i).getStart().getShortName();
            String endLevel = difficultyRanges.get(i).getEnd().getShortName();
            BojProblem bojProblem = BojProblem.builder()
                    .testProblemId((long) i)
                    .problemId(null)
                    .startLevel(startLevel)
                    .endLevel(endLevel)
                    .build();
            bojProblems.add(bojProblem);
        });
        return bojProblems;
    }
}