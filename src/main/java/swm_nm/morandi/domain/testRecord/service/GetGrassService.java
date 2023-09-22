package swm_nm.morandi.domain.testRecord.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.member.dto.GrassDto;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetGrassService {

    private final AttemptProblemRepository attemptProblemRepository;

    public List<GrassDto> getGrassDtos() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        java.util.List<GrassDto> grassDtos = new ArrayList<>();
        java.util.List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByMember_MemberId(memberId);
        if (!attemptProblems.isEmpty()) {
            Map<LocalDate, Integer> grassMap = new HashMap<>();
            attemptProblems.forEach(attemptProblem -> {
                LocalDate testDate = attemptProblem.getTestDate();
                if (attemptProblem.getIsSolved()) {
                    Integer currentValue = grassMap.getOrDefault(testDate, 0);
                    Integer newValue = currentValue + 1;
                    grassMap.put(testDate, newValue);
                }
            });
            grassMap.entrySet().forEach(entry -> {
                LocalDate testDate = entry.getKey();
                Integer solvedCount = entry.getValue();
                GrassDto grassDto = GrassDto.builder()
                        .testDate(testDate)
                        .solvedCount(solvedCount)
                        .build();
                grassDtos.add(grassDto);
            });
        }

        if (grassDtos.size() == 0) {
            GrassDto grassDto = GrassDto.builder()
                    .testDate(null)
                    .solvedCount(null)
                    .build();
            grassDtos.add(grassDto);
        }
        return grassDtos;
    }
}
