package swm_nm.morandi.domain.member.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.domain.member.dto.MemberInfoDto;
import swm_nm.morandi.domain.member.dto.RegisterInfoDto;
import swm_nm.morandi.domain.member.service.MemberEditService;
import swm_nm.morandi.domain.member.service.MemberInfoService;
import swm_nm.morandi.domain.member.service.MemberInitService;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/members")
@Tag(name = "MemberController", description = "사용자 정보와 관련된 컨트롤러")
@RequiredArgsConstructor
public class MemberController {

    private final MemberInitService memberInitService;

    private final MemberInfoService memberInfoService;

    private final MemberEditService memberEditService;
    @GetMapping("/check")
    @Operation(summary = "백준 id 등록되어있는지 확인", description ="백준 id가 등록되어있는지 확인합니다. 200반환 시 정상," +
            "  \"code\": \"BAEKJOON_ID_NULL\" 반환 시 백준 id가 등록되지 않은 상태, 토큰 오류 시 403")
    public ResponseEntity<?> checkMemberInitialized(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/register-info")
    @Operation(summary = "사용자 최초 등록", description = "사용자 최초 등록시 백준 아이디가 필수적으로 필요합니다.")
    public ResponseEntity<RegisterInfoDto> memberInitialize(@RequestBody @Valid RegisterInfoDto registerInfoDto) {
        return new ResponseEntity<>(memberInitService.memberInitialize(registerInfoDto), HttpStatus.OK);
    }
    @GetMapping("/info")
    @Operation(summary = "사용자 정보", description = "사용자의 닉네임과 백준 아이디를 보여줍니다.")
    public ResponseEntity<MemberInfoDto> memberInfo() {
        return new ResponseEntity<>(memberInfoService.getMemberInfo(), HttpStatus.OK);
    }
    @PostMapping("/edit")
    @Operation(summary = "사용자 정보 수정", description = "사용자의 닉네임 또는 백준 아이디를 수정합니다.")
    public ResponseEntity<MemberInfoDto> editProfile(@RequestBody MemberInfoDto memberInfoDto) throws IOException {
//      String thumbPhoto = memberService.editThumbPhoto(memberId, profileRequestDto.getThumbPhotoFile());
        memberEditService.editProfile(memberInfoDto.getIntroduceInfo(), memberInfoDto.getBojId());
        return new ResponseEntity(memberInfoDto, HttpStatus.OK);
    }
}
