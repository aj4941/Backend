package swm_nm.morandi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.dto.*;
import swm_nm.morandi.service.AttemptProblemService;
import swm_nm.morandi.service.MemberService;
import swm_nm.morandi.service.TestService;

import java.util.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AttemptProblemService attemptProblemService;
    private final TestService testService;
    @GetMapping("/{memberId}")
    public ResponseEntity<Map<String, Object>> memberInfo(@PathVariable Long memberId) {
        List<GrassDto> grassDtos =
                attemptProblemService.getGrassDtosByMemberId(memberId);
        List<TestRecordDto> testRecordDtos
                = testService.getTestRecordDtosByMemberId(memberId);
        List<GraphDto> graphDtos =
                attemptProblemService.getGraphDtosByMemberId(memberId);
        List<TestRatingDto> testRatingDtos
                = testService.getTestRatingDtosByMemberId(memberId);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("grassDto", grassDtos);
        responseData.put("testRecordDto", testRecordDtos);
        responseData.put("graphDto", graphDtos);
        responseData.put("testRatingDto", testRatingDtos);

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity editProfile(@PathVariable Long memberId,
                                      @RequestBody MemberDto memberDto) {
        memberService.editProfile(memberId, memberDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{memberId}/test-info")
    public ResponseEntity<List<TestRecordDto>> testRecordInfo(@PathVariable Long memberId) {
        List<TestRecordDto> testRecordDtos
                = testService.getTestRecordDtosByMemberId(memberId);

        return new ResponseEntity<>(testRecordDtos, HttpStatus.OK);
    }
}
