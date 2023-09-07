package swm_nm.morandi.auth.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class OAuthConstants {

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    @Value("${oauth.google.redirect-uri}")
    private String googleRedirectUri;

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
    }

    public String GOOGLE_REDIRECT_URL;
    public String GOOGLE_REDIRECT_URL_DEV;
    public static final String GOOGLE_USERINFO_REQUEST_URL="https://www.googleapis.com/userinfo/v2/me";

}
