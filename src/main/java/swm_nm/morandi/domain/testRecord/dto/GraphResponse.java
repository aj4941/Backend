package swm_nm.morandi.domain.testRecord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import java.util.HashMap;


@Getter
@Builder
@AllArgsConstructor
public class GraphResponse {
    public HashMap<String,Long> solvedRates;


    public GraphResponse() {
        this.solvedRates = new HashMap<>();
        solvedRates.put("구현", 0L);
        solvedRates.put("그래프", 0L);
        solvedRates.put("그리디", 0L);
        solvedRates.put("문자열", 0L);
        solvedRates.put("이분 탐색", 0L);
        solvedRates.put("자료 구조", 0L);
        solvedRates.put("정렬", 0L);
        solvedRates.put("최단 경로", 0L);
        solvedRates.put("DFS와 BFS", 0L);
        solvedRates.put("DP", 0L);
    }
}
