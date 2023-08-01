package swm_nm.morandi.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import swm_nm.morandi.auth.constants.OAuthConstants;

@Controller
@RequestMapping("/oauths")
public class OAuthURIController {

    @Value("${oauth.google.client-id}")
    private String google_client_id;

    @Value("${oauth.google.redirect-uri}")
    private String google_redirect_uri;


    @ResponseBody
    @GetMapping("/google")
    public String googleRedirect() {

        //이것도 service로 빼기
        return OAuthConstants.GOOGLE_REDIRECT_URL;

    }

    //백엔드 개발 시 사용하는 API
    @GetMapping("/google/dev")
    public String googleRedirectforDevelop() {

        return "redirect:" + OAuthConstants.GOOGLE_REDIRECT_URL_DEV;

    }



}