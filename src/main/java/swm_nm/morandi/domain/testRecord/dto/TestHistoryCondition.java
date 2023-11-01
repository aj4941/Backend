package swm_nm.morandi.domain.testRecord.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestHistoryCondition {
    private String testTypename;
    private Long bojProblemId;
    private String bojId;
    private Integer page;
    private Integer size;
}
