package swm_nm.morandi.domain.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
public class MemberListRequestDto {
    private Integer page = 1;
    private Integer size = 15;
}
