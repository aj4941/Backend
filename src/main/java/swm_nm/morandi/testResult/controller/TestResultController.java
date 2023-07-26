package swm_nm.morandi.testResult.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.testResult.request.AttemptProblemDto;
import swm_nm.morandi.testResult.request.TestResultDto;
import swm_nm.morandi.testResult.service.AttemptProblemService;
import swm_nm.morandi.testResult.service.TestResultService;

import java.util.List;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestResultController {


    private final AttemptProblemService attemptProblemService;
    private final TestResultService testResultService;

    //진행한 테스트 저장
    //테스트 해당하는 문제별로 저장
    @PostMapping("/{test-type-id}/attempt-problems")
    public ResponseEntity<List<AttemptProblemDto>> saveAttemptedProblemResult
            (@PathVariable("test-type-id") Long testTypeId, @RequestBody TestResultDto testResultDto) {
        testResultService.saveTestResult(testTypeId,testResultDto);

        return new ResponseEntity<>(testResultDto.getAttemptProblemDtos(), HttpStatus.OK);
    }

    //문제별 정답여부 확인하는 API
    @GetMapping("/{problem-id}/attempt-problems/{boj-id}")
    public ResponseEntity<Boolean> checkAttemptedProblemResult (@PathVariable("problem-id") Long problemId, @PathVariable("boj-id") String bojId){
        Boolean isSolved = attemptProblemService.checkAttemptedProblemResult(bojId,problemId);
        return new ResponseEntity<>(isSolved, HttpStatus.OK);
    }



}
