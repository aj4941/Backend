package swm_nm.morandi.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.auth.response.TokenDto;
import swm_nm.morandi.auth.service.LoginService;


@RestController
@RequestMapping("/oauths")
@RequiredArgsConstructor
public class OAuthController {


    private final LoginService loginService;

    //Authorization Code도 모두 가져오는 경우에 사용하는 callback api
    @GetMapping("/{type}/callback")
    public TokenDto googleLogin(@PathVariable String type, @RequestParam String code) {
        return loginService.login(type, code);


    }

    //만약 이미 access Toekn을 가지고 있는 경우에는 이 api를 사용한다.
    @GetMapping("/{type}/login")
    public TokenDto login(@PathVariable String type, @RequestParam String accessToken) {
        return loginService.OAuthJoinOrLogin(type, accessToken);
    }


}
