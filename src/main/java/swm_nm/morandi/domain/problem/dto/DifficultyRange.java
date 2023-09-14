package swm_nm.morandi.domain.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DifficultyRange {
    private DifficultyLevel start;
    private DifficultyLevel end;
}
