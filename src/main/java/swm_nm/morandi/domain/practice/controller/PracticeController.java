package swm_nm.morandi.domain.practice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm_nm.morandi.domain.practice.dto.PracticeExitDto;
import swm_nm.morandi.domain.practice.dto.PracticeResultDto;
import swm_nm.morandi.domain.practice.dto.PracticeStartDto;
import swm_nm.morandi.domain.practice.service.PracticeProblemService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/practices")
public class PracticeController {

    private final PracticeProblemService practiceProblemService;
    @PostMapping
    @Operation(summary = "연습 문제 풀기", description = "사용자가 임의로 백준 문제 번호를 골라서 문제를 풀려고 시도할 때 테이블을 구성합니다.")
    public ResponseEntity practiceStart(PracticeStartDto practiceStartDto) {
        practiceProblemService.savePraticeProblems(practiceStartDto.getBojProblemId());
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/exit")
    @Operation(summary = "연습 문제 종료하기", description = "사용자가 시도한 연습문제를 종료할 경우 결과 데이터를 반환합니다.")
    public ResponseEntity<PracticeResultDto> practiceExit(PracticeExitDto practiceExitDto) {
        PracticeResultDto practiceResultDto = practiceProblemService.practiceResult(practiceExitDto.getPracticeProblemId());
        return new ResponseEntity<>(practiceResultDto, HttpStatus.OK);
    }
}
