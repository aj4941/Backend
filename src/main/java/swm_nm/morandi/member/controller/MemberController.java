package swm_nm.morandi.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import swm_nm.morandi.auth.security.SecurityUtils;
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
        return ResponseEntity.ok(memberService.memberInitialize(registerInfoDto));
    }
    @GetMapping("/record")
    public ResponseEntity<Map<String, Object>> memberRecord() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        List<GrassDto> grassDtos =
                attemptProblemService.getGrassDtosByMemberId(memberId);
        List<GraphDto> graphDtos =
                attemptProblemService.getGraphDtosByMemberId(memberId);
        List<TestRatingDto> testRatingDtos =
                testService.getTestRatingDtosByMemberId(memberId);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("grassDto", grassDtos);
        responseData.put("graphDto", graphDtos);
        responseData.put("testRatingDto", testRatingDtos);

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> memberInfo() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        MemberInfoDto memberDto = memberService.getMemberInfo(memberId);
        Map<String, String> info = new HashMap<>();
        info.put("nickname", memberDto.getNickname());
        info.put("bojId", memberDto.getBojId());
        return new ResponseEntity<>(info, HttpStatus.OK);
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
        Long memberId = SecurityUtils.getCurrentMemberId();
//        String thumbPhoto = memberService.editThumbPhoto(memberId, profileRequestDto.getThumbPhotoFile());
        memberService.editProfile(memberId, memberInfoDto.getNickname(), memberInfoDto.getBojId());
        return new ResponseEntity(memberInfoDto, HttpStatus.OK);
    }

    @GetMapping("/test-info/{testId}")
    public ResponseEntity<TestRecordDto> testRecordInfo(@PathVariable Long testId) {
        TestRecordDto testRecordDto = testService.getTestRecordDtoByTestId(testId);
        return new ResponseEntity<>(testRecordDto, HttpStatus.OK);
    }
}
