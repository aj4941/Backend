package swm_nm.morandi.domain.auth.response;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class GithubEmailDto {
    private String email;
    private Boolean primary;
    private Boolean verified;
    private String visibility;

}
