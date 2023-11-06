package swm_nm.morandi.domain.member.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberListResponse {

    private Long rank;
    private String bojId;
    private Long rating;
}
