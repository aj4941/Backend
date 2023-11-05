package swm_nm.morandi.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoRequest {
    private String introduceInfo;
    private String bojId;
}
