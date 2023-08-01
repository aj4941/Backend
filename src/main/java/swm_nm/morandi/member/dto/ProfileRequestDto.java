package swm_nm.morandi.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@NoArgsConstructor
public class ProfileRequestDto {
    private String nickname;
    private String bojId;
    // private MultipartFile thumbPhotoFile;
}
