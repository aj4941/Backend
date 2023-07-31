package swm_nm.morandi.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.member.dto.*;
import swm_nm.morandi.testResult.service.AttemptProblemService;
import swm_nm.morandi.member.service.MemberService;
import swm_nm.morandi.test.dto.TestRatingDto;
import swm_nm.morandi.test.dto.TestRecordDto;
import swm_nm.morandi.test.service.TestService;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AttemptProblemService attemptProblemService;
    private final TestService testService;

    @PostMapping("/register-info")
    public ResponseEntity<RegisterInfoDto> memberInitialize(@RequestBody RegisterInfoDto registerInfoDto) {
        return new ResponseEntity<>(memberService.memberInitialize(registerInfoDto), HttpStatus.OK);
    }
    @GetMapping("/record")
    public ResponseEntity<MemberRecordDto> memberRecord() {
        return new ResponseEntity<>(attemptProblemService.getMemberRecords(), HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<MemberInfoDto> memberInfo() {
        return new ResponseEntity<>(memberService.getMemberInfo(), HttpStatus.OK);
    }

//    @GetMapping("/thumb-info")
//    public ResponseEntity<byte[]> memberThumbInfo() {
//        Long memberId = SecurityUtils.getCurrentMemberId();
//        ThumbURLDto thumbURLDto = memberService.getMemberThumbDto(memberId);
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.IMAGE_JPEG);
//        return new ResponseEntity<>(thumbURLDto.getThumbPhoto(), header, HttpStatus.OK);
//    }

    @PostMapping("/edit")
    public ResponseEntity<MemberInfoDto> editProfile(@RequestBody MemberInfoDto memberInfoDto) throws IOException {
//      String thumbPhoto = memberService.editThumbPhoto(memberId, profileRequestDto.getThumbPhotoFile());
        memberService.editProfile(memberInfoDto.getNickname(), memberInfoDto.getBojId());
        return new ResponseEntity(memberInfoDto, HttpStatus.OK);
    }

    @GetMapping("/test-info/{testId}")
    public ResponseEntity<TestRecordDto> testRecordInfo(@PathVariable Long testId) {
        return new ResponseEntity<>(testService.getTestRecordDtoByTestId(testId), HttpStatus.OK);
    }
}
