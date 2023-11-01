package swm_nm.morandi.domain.testRecord.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
<<<<<<< Updated upstream
import org.springframework.transaction.annotation.Transactional;
=======
import swm_nm.morandi.domain.practice.entity.PracticeProblem;
import swm_nm.morandi.domain.practice.repository.PracticeProblemRepository;
>>>>>>> Stashed changes
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testRecord.dto.GrassHeatMapResponse;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GrassHeatMapService {

    private final AttemptProblemRepository attemptProblemRepository;

<<<<<<< Updated upstream
    @Transactional(readOnly = true)
    public List<GrassHeatMapResponse> getGrassHeatMap(){
        Long membeId = SecurityUtils.getCurrentMemberId();
=======
    private final PracticeProblemRepository practiceProblemRepository;
    public List<GrassHeatMapResponse> getGrassHeatMap() {
        Long memberId = SecurityUtils.getCurrentMemberId();
>>>>>>> Stashed changes
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        List<AttemptProblem> attemptProblems =
                attemptProblemRepository.findAllAttemptProblemsByMember_MemberIdAndTestDateAfterAndIsSolved(memberId, oneYearAgo, true);
        List<PracticeProblem> practiceProblems =
                practiceProblemRepository.findAllByMember_MemberIdAndPracticeDateAfterAndIsSolved(memberId, oneYearAgo, true);

        Map<LocalDate, Long> map = new HashMap<>();

        attemptProblems.stream().map(AttemptProblem::getTestDate).forEach(testDate -> {
            Long count = map.getOrDefault(testDate, 0L) + 1;
            map.put(testDate, count);
        });
        practiceProblems.stream().map(PracticeProblem::getPracticeDate).forEach(practiceDate -> {
            Long count = map.getOrDefault(practiceDate, 0L) + 1;
            map.put(practiceDate, count);
        });

        List<GrassHeatMapResponse> grassHeatMapResponses =
                map.entrySet().stream()
                        .map(entry -> GrassHeatMapResponse.getGrassHeatMapResponse(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList());

        return grassHeatMapResponses;
    }
}
