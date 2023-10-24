package swm_nm.morandi.domain.testStart.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class BojProblemDto {
    public Long bojProblemId;
    public Boolean isSolved;
}
