package swm_nm.morandi.domain.test.postTest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.global.utils.SecurityUtils;
import swm_nm.morandi.domain.member.dto.GraphDto;
import swm_nm.morandi.domain.member.dto.GrassDto;
import swm_nm.morandi.domain.test.repository.AttemptProblemRepository;
import swm_nm.morandi.domain.problem.entity.Algorithm;
import swm_nm.morandi.domain.problem.entity.AlgorithmProblemList;
import swm_nm.morandi.domain.problem.repository.AlgorithmProblemListRepository;
import swm_nm.morandi.domain.problem.repository.AlgorithmRepository;
import swm_nm.morandi.domain.test.entity.AttemptProblem;

import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AttemptProblemService {

    private final AttemptProblemRepository attemptProblemRepository;

    private final AlgorithmProblemListRepository algorithmProblemListRepository;

    private final AlgorithmRepository algorithmRepository;

    public List<GrassDto> getGrassDtos() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        List<GrassDto> grassDtos = new ArrayList<>();
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByMember_MemberId(memberId);
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

    public List<GraphDto> getGraphDtos() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Map<String, Long> totalCount = new HashMap<>();
        Map<String, Long> Count = new HashMap<>();
        List<Algorithm> algorithms = algorithmRepository.findAll();

        algorithms.stream().map(Algorithm::getAlgorithmName).forEach(algorithmName -> {
            totalCount.put(algorithmName, 0L);
            Count.put(algorithmName, 0L);
        });

        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByMember_MemberId(memberId);

        List<GraphDto> graphDtos = new ArrayList<>();
        if (!attemptProblems.isEmpty()) {
            attemptProblems.forEach(attemptProblem -> {
                Long problemId = attemptProblem.getProblem().getProblemId();
                List<AlgorithmProblemList> algorithmProblemLists =
                        algorithmProblemListRepository.findByProblem_ProblemId(problemId);
                algorithmProblemLists.stream().map(algorithmProblemList ->
                        algorithmProblemList.getAlgorithm().getAlgorithmName()).forEach(algorithmName -> {
                    Long currentTotalCount = totalCount.getOrDefault(algorithmName, 0L);
                    Long currentCount = Count.getOrDefault(algorithmName, 0L);
                    totalCount.put(algorithmName, currentTotalCount + 1L);
                    if (attemptProblem.getIsSolved())
                        Count.put(algorithmName, currentCount + 1L);
                });
            });

            algorithms.stream().map(Algorithm::getAlgorithmName).forEach(algorithmName -> {
                Long solvedRate = totalCount.get(algorithmName) == 0 ? 0L :
                        Count.get(algorithmName) * 100L / totalCount.get(algorithmName);
                GraphDto graphDto = GraphDto.builder()
                        .algorithmName(algorithmName)
                        .solvedRate(solvedRate)
                        .build();
                graphDtos.add(graphDto);
            });
        }

        return graphDtos;
    }
}
