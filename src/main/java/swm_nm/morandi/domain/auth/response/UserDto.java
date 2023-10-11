package swm_nm.morandi.domain.auth.response;

import swm_nm.morandi.domain.member.entity.SocialType;

public interface UserDto {

    SocialType getType();
    String getEmail();
    String getPicture();
    String getIntroduceInfo();

    String getGithubUrl();
}
