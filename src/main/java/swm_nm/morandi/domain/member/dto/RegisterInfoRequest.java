package swm_nm.morandi.domain.member.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInfoRequest {
    @NotBlank
    private String bojId;
}
