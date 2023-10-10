package swm_nm.morandi.domain.testRecord.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testRecord.dto.GrassHeatMapResponse;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GrassHeatMapService {

    private final AttemptProblemRepository attemptProblemRepository;

    public List<GrassHeatMapResponse> getGrassHeatMap(){
        Long membeId = SecurityUtils.getCurrentMemberId();
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        List<AttemptProblem> attemptProblems =
                attemptProblemRepository.findAllAttemptProblemsByMember_MemberIdAndAndTestDateAfterAndIsSolved(membeId, oneYearAgo, true);

        Map<LocalDate, List<AttemptProblem>> map =
                attemptProblems.stream().collect(Collectors.groupingBy(AttemptProblem::getTestDate));

        return map.keySet().stream()
                .map(LocalDate ->
                        GrassHeatMapResponse.builder()
                            .testDate(LocalDate)
                            .solvedCount((long) map.get(LocalDate).size())
                            .build())
                    .collect(Collectors.toList());

    }
}
