package swm_nm.morandi.testResult.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.testResult.request.AttemptProblemDto;
import swm_nm.morandi.testResult.service.AttemptProblemService;

import java.util.List;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestResultController {


    private final AttemptProblemService attemptProblemService;

    //진행한 테스트를 문제별로 저장
    @PostMapping("/{test-id}/attempt-problems")
    public ResponseEntity<List<AttemptProblemDto>> saveAttemptedProblemResult
            (@PathVariable("test-id") Long testId, @RequestBody List<AttemptProblemDto> attemptProblemDtos) {
        attemptProblemService.saveAttemptedProblemResult(testId, attemptProblemDtos);
        return new ResponseEntity<>(attemptProblemDtos, HttpStatus.OK);
    }

    //문제별 정답여부 확인하는 API
    @GetMapping("/{problem-id}/attempt-problems/{boj-id}")
    public ResponseEntity<Boolean> checkAttemptedProblemResult (@PathVariable("problem-id") Long problemId, @PathVariable("boj-id") String bojId){
        Boolean isSolved = attemptProblemService.checkAttemptedProblemResult(bojId,problemId);
        return new ResponseEntity<>(isSolved, HttpStatus.OK);
    }



}
