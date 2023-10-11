package swm_nm.morandi.domain.auth.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class OAuthConstants {

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    @Value("${oauth.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${oauth.github.client-id}")
    private String githubClientId;

    @PostConstruct
    public void init()
    {
        GOOGLE_REDIRECT_URL= "https://accounts.google.com/o/oauth2/v2/auth?"
                + "scope=https://www.googleapis.com/auth/userinfo.email&"
                + "access_type=offline&"
                + "include_granted_scopes=true&"
                + "response_type=code&"
                + "state=state_parameter_passthrough_value&"
                +"redirect_uri="+googleRedirectUri+"&"
                +"client_id="+googleClientId;

        GOOGLE_REDIRECT_URL_DEV = "https://accounts.google.com/o/oauth2/v2/auth?"
                + "scope=https://www.googleapis.com/auth/userinfo.email&"
                + "access_type=offline&"
                + "include_granted_scopes=true&"
                + "response_type=code&"
                + "state=state_parameter_passthrough_value&"
                +"redirect_uri="+googleRedirectUri+"/dev&"
                +"client_id="+googleClientId;

        GITHUB_REDIRECT_URL = "https://github.com/login/oauth/authorize?client_id="+githubClientId+"&scope=user:email";

        GITHUB_REDIRECT_URL_DEV = "https://github.com/login/oauth/authorize?client_id="+githubClientId+"&scope=user:email";
        ;
    }

    public String GITHUB_REDIRECT_URL;
    public String GITHUB_REDIRECT_URL_DEV;

    public String NAVER_REDIRECT_URL;
    public String NAVER_REDIRECT_URL_DEV;
    public String GOOGLE_REDIRECT_URL;
    public String GOOGLE_REDIRECT_URL_DEV;
    public static final String GOOGLE_USERINFO_REQUEST_URL="https://www.googleapis.com/userinfo/v2/me";

    public static final String GITHUB_USERINFO_REQUEST_URL="https://api.github.com/user";
    public static final String GITHUB_EMAIL_REQUEST_URL="https://api.github.com/user/emails";
    public static final String NAVER_USERINFO_REQUEST_URL="https://openapi.naver.com/v1/nid/me";
}
