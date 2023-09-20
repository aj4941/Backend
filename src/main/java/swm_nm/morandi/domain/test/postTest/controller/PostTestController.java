package swm_nm.morandi.domain.test.postTest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.domain.test.postTest.service.PostTestService;
import swm_nm.morandi.domain.test.dto.TestCheckDto;
import swm_nm.morandi.domain.test.postTest.request.AttemptCodeDto;
import swm_nm.morandi.domain.test.postTest.request.AttemptProblemDto;

import java.util.List;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Tag(name = "PostTestController", description = "테스트 결과와 관련된 컨트롤러")
public class PostTestController {

    private final PostTestService postTestService;

    // 진행한 테스트 저장, 테스트 해당하는 문제별로 저장
    @PostMapping("/exit")
    @Operation(summary = "테스트 종료하기", description = "테스트를 종료할 경우 문제별 정답 여부와 소요 시간을 제공합니다.")
    public ResponseEntity<List<AttemptProblemDto>> saveAttemptedProblemResult(@RequestBody TestCheckDto testCheckDto) {
        return new ResponseEntity<>(postTestService.testExit(testCheckDto), HttpStatus.OK);
    }
    @PostMapping("/submit")
    @Operation(summary = "테스트 종료 시점 코드 저장", description = "테스트 종료 시점에서 종료하기 직전 API를 호출하여 사용자가 풀어둔 코드를 저장해야 합니다.")
    public ResponseEntity<?> saveCode(@RequestBody AttemptCodeDto attemptCodeDto) {
        postTestService.saveEachCodeinAttemptProblems(attemptCodeDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
