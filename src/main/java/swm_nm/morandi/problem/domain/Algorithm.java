package swm_nm.morandi.problem.domain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.test.domain.TestType;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Algorithm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long algorithmId;
    private String algorithmName;

    @ManyToOne
    @JoinColumn(name = "TEST_TYPE_ID")
    private TestType testType;
}
