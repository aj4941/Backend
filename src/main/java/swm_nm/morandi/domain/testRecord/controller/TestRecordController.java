package swm_nm.morandi.domain.testRecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.domain.testRecord.dto.*;
import swm_nm.morandi.domain.testRecord.service.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tests")
@Tag(name = "MemberController", description = "테스트 기록과 관련된 컨트롤러")
@RequiredArgsConstructor
public class TestRecordController {

    private final GetGraphService getGraphService;

    private final GrassHeatMapService grassHeatMapService;

    private final TestDetailsService testDetailsService;

    private final RatingService ratingService;
    @GetMapping("/heatmaps")
    @Operation(summary = "사용자 히트맵 분석 데이터", description = "사용자가 테스트를 본 결과를 바탕으로 히트맵 분석 데이터를 보여줍니다.")
    public ResponseEntity<List<GrassHeatMapResponse>> memberRecordGrass() {
        return new ResponseEntity<>(grassHeatMapService.getGrassHeatMap(), HttpStatus.OK);
    }
    @GetMapping("/graphs")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "사용자 그래프 분석 데이터", description = "사용자가 테스트를 본 결과를 바탕으로 그래프 분석 데이터를 보여줍니다.")
    public GraphResponse memberRecordGraph() {
       return getGraphService.getMemberGraph();
    }
    @GetMapping("/current-rating")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "사용자 현재 레이팅", description = "사용자가 가지고 있는 현재 레이팅을 보여줍니다.")
    public CurrentRatingResponse memberCurrentRating() {
        return ratingService.getCurrentRatingDto();
    }

    @GetMapping("/rating-graph")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "사용자 그래프 분석 데이터", description = "1년 동안의 사용자 테스트 레이팅 결과 데이터를 최근 날짜부터 내림차순으로 보여줍니다.")
    public List<RatingGraphResponse> memberRatingGraph(){
        return ratingService.getRatingGraphSinceOneYear();
    }

    @GetMapping("/{testId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "사용자 테스트 기록 상세 보기", description = "사용자가 진행한 특정 테스트의 상세보기를 클릭한 경우 테스트 상세 기록을 보여줍니다.")
    public TestRecordResponse testRecordInfo(@PathVariable Long testId) {
        return testDetailsService.getTestRecordDtoByTestId(testId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "전체 테스트 기록 보기", description = "전체 사용자가 진행한 테스트 기록을 보여줍니다.")
    public AllTestHistoryResponse testRecordInfo(@Valid TestHistoryCondition testHistoryCondition) {
        return testDetailsService.findAllTestStatusByCondition(testHistoryCondition);
    }
}
