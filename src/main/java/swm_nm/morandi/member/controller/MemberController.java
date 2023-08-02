package swm_nm.morandi.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.Graph;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.logging.Logging;
import swm_nm.morandi.member.dto.*;
import swm_nm.morandi.testResult.service.AttemptProblemService;
import swm_nm.morandi.member.service.MemberService;
import swm_nm.morandi.test.dto.TestRatingDto;
import swm_nm.morandi.test.dto.TestRecordDto;
import swm_nm.morandi.test.service.TestService;

import java.io.IOException;
import java.util.*;


@Logging
@RestController
@RequestMapping("/members")
@Tag(name = "MemberController", description = "사용자와 관련된 컨트롤러")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AttemptProblemService attemptProblemService;
    private final TestService testService;

    @GetMapping("/check")
    @Operation(summary = "백준 id 등록되어있는지 확인", description ="백준 id가 등록되어있는지 확인합니다. 200반환 시 정상,  \"code\": \"BAEKJOON_ID_NULL\" 반환 시 백준 id가 등록되지 않은 상태, 토큰 오류 시 403")
    public ResponseEntity<?> checkMemberInitialized(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register-info")
    @Operation(summary = "사용자 최초 등록", description = "사용자 최초 등록시 백준 아이디가 필수적으로 필요합니다.")
    public ResponseEntity<RegisterInfoDto> memberInitialize(@RequestBody RegisterInfoDto registerInfoDto) {
        return new ResponseEntity<>(memberService.memberInitialize(registerInfoDto), HttpStatus.OK);
    }
    @GetMapping("/record-grass")
    @Operation(summary = "사용자 히트맵 분석 데이터", description = "사용자가 테스트를 본 결과를 바탕으로 히트맵 분석 데이터를 보여줍니다.")
    public ResponseEntity<List<GrassDto>> memberRecordGrass() {
        return new ResponseEntity<>(attemptProblemService.getGrassDtos(), HttpStatus.OK);
    }
    @GetMapping("/record-graph")
    @Operation(summary = "사용자 그래프 분석 데이터", description = "사용자가 테스트를 본 결과를 바탕으로 그래프 분석 데이터를 보여줍니다.")
    public ResponseEntity<List<GraphDto>> memberRecordGraph() {
        return new ResponseEntity<>(attemptProblemService.getGraphDtos(), HttpStatus.OK);
    }
    @GetMapping("/record-test-rating")
    @Operation(summary = "사용자 레이팅 분석 데이터", description = "사용자가 테스트를 본 결과를 바탕으로 레이팅 분석 데이터를 보여줍니다.")
    public ResponseEntity<List<TestRatingDto>> memberRecordTestRating() {
        return new ResponseEntity<>(testService.getTestRatingDtos(), HttpStatus.OK);
    }

    @GetMapping("/record-current-rating")
    @Operation(summary = "사용자 현재 레이팅", description = "사용자가 가지고 있는 현재 레이팅을 보여줍니다.")
    public ResponseEntity<CurrentRatingDto> memberCurrentRating() {
        return new ResponseEntity<>(testService.getCurrentRatingDto(), HttpStatus.OK);
    }

    @GetMapping("/info")
    @Operation(summary = "사용자 정보", description = "사용자의 닉네임과 백준 아이디를 보여줍니다.")
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
    @Operation(summary = "사용자 정보 수정", description = "사용자의 닉네임 또는 백준 아이디를 수정합니다.")
    public ResponseEntity<MemberInfoDto> editProfile(@RequestBody MemberInfoDto memberInfoDto) throws IOException {
//      String thumbPhoto = memberService.editThumbPhoto(memberId, profileRequestDto.getThumbPhotoFile());
        memberService.editProfile(memberInfoDto.getNickname(), memberInfoDto.getBojId());
        return new ResponseEntity(memberInfoDto, HttpStatus.OK);
    }

    @GetMapping("/test-info/{testId}")
    @Operation(summary = "테스트 상세 보기", description = "특정 테스트의 상세보기를 클릭한 경우 테스트 상세 기록을 보여줍니다.")
    public ResponseEntity<TestRecordDto> testRecordInfo(@PathVariable Long testId) {
        return new ResponseEntity<>(testService.getTestRecordDtoByTestId(testId), HttpStatus.OK);
    }
}
