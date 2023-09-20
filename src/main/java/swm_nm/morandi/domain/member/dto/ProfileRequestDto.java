package swm_nm.morandi.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProfileRequestDto {
    private String nickname;
    private String bojId;
    // private MultipartFile thumbPhotoFile;
}
