package swm_nm.morandi.domain.codeSubmit.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BaekjoonUserDto {
    @NotNull(message = "백준 아이디를 입력해주세요")
    public String bojId;

    @NotNull(message = "쿠키를 입력해주세요")
    public String cookie;
}
