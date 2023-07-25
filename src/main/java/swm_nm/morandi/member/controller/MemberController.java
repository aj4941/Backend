package swm_nm.morandi.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import swm_nm.morandi.member.dto.*;
import swm_nm.morandi.member.service.AttemptProblemService;
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
    @GetMapping("/{memberId}/record")
    public ResponseEntity<Map<String, Object>> memberRecord(@PathVariable Long memberId) {
        List<GrassDto> grassDtos =
                attemptProblemService.getGrassDtosByMemberId(memberId);
        List<TestRecordDto> testRecordDtos =
                testService.getTestRecordDtosByMemberId(memberId);
        List<GraphDto> graphDtos =
                attemptProblemService.getGraphDtosByMemberId(memberId);
        List<TestRatingDto> testRatingDtos =
                testService.getTestRatingDtosByMemberId(memberId);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("grassDto", grassDtos);
        responseData.put("testRecordDto", testRecordDtos);
        responseData.put("graphDto", graphDtos);
        responseData.put("testRatingDto", testRatingDtos);

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/{memberId}/info")
    public ResponseEntity<Map<String, String>> memberInfo(@PathVariable Long memberId) {
        MemberInfoDto memberDto = memberService.getMemberInfo(memberId);
        Map<String, String> info = new HashMap<>();
        info.put("nickname", memberDto.getNickname());
        info.put("bojId", memberDto.getBojId());
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @GetMapping("/{memberId}/thumbInfo")
    public ResponseEntity<byte[]> memberThumbInfo(@PathVariable Long memberId) throws IOException {
        MemberInfoDto memberDto = memberService.getMemberInfo(memberId);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(memberDto.getThumbPhoto(), header, HttpStatus.OK);
    }

    @PostMapping(value = "/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity editProfile(@PathVariable Long memberId,
                                      @ModelAttribute ProfileRequestDto profileRequestDto) throws IOException {
        String thumbPhoto = memberService.editThumbPhoto(memberId, profileRequestDto.getThumbPhotoFile());
        memberService.editProfile(memberId, profileRequestDto.getNickname(), profileRequestDto.getBojId(), thumbPhoto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{memberId}/test-info")
    public ResponseEntity<List<TestRecordDto>> testRecordInfo(@PathVariable Long memberId) {
        List<TestRecordDto> testRecordDtos
                = testService.getTestRecordDtosByMemberId(memberId);

        return new ResponseEntity<>(testRecordDtos, HttpStatus.OK);
    }
}
