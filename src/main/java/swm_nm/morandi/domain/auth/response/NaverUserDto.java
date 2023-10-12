package swm_nm.morandi.domain.auth.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import swm_nm.morandi.domain.member.entity.SocialType;

@Getter
@Setter
@Builder
public class NaverUserDto implements UserDto {
    private String id;
    private String email;
    private String profileImage;
    private SocialType type;
    @Override
    public SocialType getType() {
        return type;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPicture() {
        return profileImage;
    }

    @Override
    public String getIntroduceInfo() {
        return null;
    }
    @Override
    public String getGithubUrl() {
        return null;
    }
}
