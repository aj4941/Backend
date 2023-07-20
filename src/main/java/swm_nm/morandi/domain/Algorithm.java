package swm_nm.morandi.domain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Algorithm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long algorithmId;
    private String algorithmName;
}
