package swm_nm.morandi.domain.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import swm_nm.morandi.global.utils.SecurityUtils;

@RestController
public class SecurityTestController {

    @GetMapping("test")
    public String test(){
        Long id = SecurityUtils.getCurrentMemberId();
        return id.toString();
    }
}