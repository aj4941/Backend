package swm_nm.morandi.domain.testRecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm_nm.morandi.domain.member.dto.CurrentRatingDto;
import swm_nm.morandi.domain.member.dto.GraphDto;
import swm_nm.morandi.domain.member.dto.GrassDto;
import swm_nm.morandi.domain.testRecord.dto.TestRatingDto;
import swm_nm.morandi.domain.testRecord.dto.TestRecordDto;
import swm_nm.morandi.domain.testRecord.service.*;

import java.util.List;

@RestController
@RequestMapping("/record")
@Tag(name = "MemberController", description = "테스트 기록과 관련된 컨트롤러")
@RequiredArgsConstructor
public class TestRecordController {

    private final GetGrassService getGrassService;

    private final GetGraphService getGraphService;

    private final GetTestRatingService getTestRatingService;

    private final GetCurrentRatingService getCurrentRatingService;

    private final TestDetailsService testDetailsService;
    @GetMapping("/grass")
    @Operation(summary = "사용자 히트맵 분석 데이터", description = "사용자가 테스트를 본 결과를 바탕으로 히트맵 분석 데이터를 보여줍니다.")
    public ResponseEntity<List<GrassDto>> memberRecordGrass() {
        return new ResponseEntity<>(getGrassService.getGrassDtos(), HttpStatus.OK);
    }
    @GetMapping("/graph")
    @Operation(summary = "사용자 그래프 분석 데이터", description = "사용자가 테스트를 본 결과를 바탕으로 그래프 분석 데이터를 보여줍니다.")
    public ResponseEntity<List<GraphDto>> memberRecordGraph() {
        return new ResponseEntity<>(getGraphService.getGraphDtos(), HttpStatus.OK);
    }
    @GetMapping("/test-rating")
    @Operation(summary = "사용자 레이팅 분석 데이터", description = "사용자가 테스트를 본 결과를 바탕으로 레이팅 분석 데이터를 보여줍니다.")
    public ResponseEntity<List<TestRatingDto>> memberRecordTestRating() {
        return new ResponseEntity<>(getTestRatingService.getTestRatingDtos(), HttpStatus.OK);
    }
    @GetMapping("/current-rating")
    @Operation(summary = "사용자 현재 레이팅", description = "사용자가 가지고 있는 현재 레이팅을 보여줍니다.")
    public ResponseEntity<CurrentRatingDto> memberCurrentRating() {
        return new ResponseEntity<>(getCurrentRatingService.getCurrentRatingDto(), HttpStatus.OK);
    }

    @GetMapping("/test-info/{testId}")
    @Operation(summary = "사용자 테스트 기록 상세 보기", description = "사용자가 진행한 특정 테스트의 상세보기를 클릭한 경우 테스트 상세 기록을 보여줍니다.")
    public ResponseEntity<TestRecordDto> testRecordInfo(@PathVariable Long testId) {
        return new ResponseEntity<>(testDetailsService.getTestRecordDtoByTestId(testId), HttpStatus.OK);
    }
}
