package swm_nm.morandi.domain.codeSubmit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.domain.codeSubmit.dto.*;
import swm_nm.morandi.domain.codeSubmit.service.BaekjoonSubmitService;
import swm_nm.morandi.domain.codeSubmit.service.SaveSubmitResultService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/submit")
@Tag(name = "CodeSubmitController", description = "코드 제출과 관련된 컨트롤러")
public class CodeSubmitController {

    private final BaekjoonSubmitService submitService;

    private final SaveSubmitResultService saveSubmitResultService;
    @PostMapping("/baekjoon")
    @Operation(summary = "문제 번호, 언어이름, 소스코드를 백준에 제출하는 컨트롤러 ", description = "사용자가 테스트 중 코드를 제출하는 경우 백준에 제출하는 컨트롤러입니다.")
    public ResponseEntity<SolutionIdResponse> submit(@RequestBody @Valid SubmitCodeDto submitCodeDto) {
        return submitService.submit(submitCodeDto);
    }
    @PostMapping("/baekjoon/practice")
    @Operation(summary = "문제 번호, 언어 이름, 소스코드를 백준에 제출하는 컨트롤러", description = "사용자가 연습 문제를 제출하는 경우 백준에 제출하는 컨트롤러입니다.")
    public ResponseEntity<SolutionIdResponse> submit(@RequestBody @Valid PracticeProblemSubmitCodeRequest practiceProblemSubmitCodeRequest) {
        return submitService.submit(practiceProblemSubmitCodeRequest);
    }
    @PostMapping("/cookie")
    public String loginForSubmit(@RequestBody @Valid BaekjoonUserDto userDto) {
        return submitService.saveBaekjoonInfo(userDto);
    }
    @PostMapping("/result")
    @Operation(summary = "testId, bojProblemId, 정답여부를 전달하는 컨트롤러", description = "제출 후 채점 결과에 대한 내용을 백엔드에 전달하는 컨트롤러입니다.")
    public String saveSubmitResult(@RequestBody @Valid AttemptProblemResultDto attemptProblemResultDto){
        return saveSubmitResultService.saveSubmitResult(attemptProblemResultDto);
    }
    @PostMapping("/result/practice")
    @Operation(summary = "정답여부를 전달하는 컨트롤러", description = "제출 후 연습 문제에 대한 내용을 백엔드에 전달하는 컨트롤러입니다.")
    public String savePracticeProblemSubmitResult(@RequestBody @Valid PracticeProblemResultDto practiceProblemResultDto) {
        return saveSubmitResultService.savePracticeProblemSubmit(practiceProblemResultDto);
    }
}
