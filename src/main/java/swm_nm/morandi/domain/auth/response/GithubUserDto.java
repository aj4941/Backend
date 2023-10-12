package swm_nm.morandi.domain.auth.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import swm_nm.morandi.domain.member.entity.SocialType;

@Setter
@Getter
public class GithubUserDto implements UserDto{
    private String id;
    private String email;
    private String name;

    private String avatar_url;
    private String gravatar_id;
    private String url;
    private String html_url;
    private String followers_url;
    private String following_url;
    private String gists_url;
    private String starred_url;
    private String subscriptions_url;
    private String organizations_url;
    private String repos_url;
    private String events_url;
    private String received_events_url;

    @JsonIgnore
    private SocialType type;
    private Boolean site_admin;



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
        return avatar_url;
    }

    @Override
    public String getIntroduceInfo() { return ""; }

    @Override
    public String getGithubUrl() {
        return html_url;
    }

}
