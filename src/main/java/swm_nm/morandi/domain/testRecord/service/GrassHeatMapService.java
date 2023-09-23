package swm_nm.morandi.domain.testRecord.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testRecord.dto.GrassHeatMapResponse;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GrassHeatMapService {

    private final AttemptProblemRepository attemptProblemRepository;

    public List<GrassHeatMapResponse> getGrassHeatMap() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        List<Object[]> grassHeatMap = attemptProblemRepository.getHeatMapDataSinceOneYear(memberId, oneYearAgo);

        return grassHeatMap.stream().map(objects ->
                GrassHeatMapResponse.builder()
                        .testDate((LocalDate) objects[0])
                        .solvedCount((Long) objects[1])
                        .build()).collect(Collectors.toList());

    }
}
