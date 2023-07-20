package swm_nm.morandi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.Algorithm;
import swm_nm.morandi.domain.AlgorithmProblemList;
import swm_nm.morandi.domain.AttemptProblem;
import swm_nm.morandi.dto.GraphDto;
import swm_nm.morandi.dto.GrassDto;
import swm_nm.morandi.repository.AlgorithmProblemListRepository;
import swm_nm.morandi.repository.AlgorithmRepository;
import swm_nm.morandi.repository.AttemptProblemRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttemptProblemService {

    private final AttemptProblemRepository attemptProblemRepository;

    private final AlgorithmProblemListRepository algorithmProblemListRepository;

    private final AlgorithmRepository algorithmRepository;

    public List<GrassDto> getGrassDtosByMemberId(Long memberId) {
        List<AttemptProblem> attemptProblems
                = attemptProblemRepository.findAllByMember_MemberId(memberId);
        Map<LocalDate, Integer> grassMap = new HashMap<>();
        for (AttemptProblem attemptProblem : attemptProblems) {
            LocalDate testDate = attemptProblem.getTestDate();
            if (attemptProblem.getIsSolved()) {
                Integer currentValue = grassMap.getOrDefault(testDate, 0);
                Integer newValue = currentValue + 1;
                grassMap.put(testDate, newValue);
            }
        }

        List<GrassDto> grassDtos = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : grassMap.entrySet()) {
            LocalDate testDate = entry.getKey();
            Integer solvedCount = entry.getValue();
            GrassDto grassDto = GrassDto.builder()
                    .testDate(testDate)
                    .solvedCount(solvedCount)
                    .build();
            grassDtos.add(grassDto);
        }
        return grassDtos;
    }

    public List<GraphDto> getGraphDtosByMemberId(Long memberId) {
        Map<String, Integer> TotalCount = new HashMap<>();
        Map<String, Integer> Count = new HashMap<>();
        List<Algorithm> algorithms = algorithmRepository.findAll();

        for (Algorithm algorithm : algorithms) {
            String algorithmName = algorithm.getAlgorithmName();
            TotalCount.put(algorithmName, 0);
            Count.put(algorithmName, 0);
        }

        List<AttemptProblem> attemptProblems
                = attemptProblemRepository.findAllByMember_MemberId(memberId);
        for (AttemptProblem attemptProblem : attemptProblems) {
            Long problemId = attemptProblem.getProblem().getProblemId();
            List<AlgorithmProblemList> algorithmProblemLists =
                    algorithmProblemListRepository.findByProblem_ProblemId(problemId);
            for (AlgorithmProblemList algorithmProblemList : algorithmProblemLists) {
                String algorithmName = algorithmProblemList.getAlgorithm().getAlgorithmName();
                int currentTotalCount = TotalCount.getOrDefault(algorithmName, 0);
                int currentCount = Count.getOrDefault(algorithmName, 0);
                TotalCount.put(algorithmName, currentTotalCount + 1);
                if (attemptProblem.getIsSolved())
                    Count.put(algorithmName, currentCount + 1);
            }
        }

        List<GraphDto> graphDtos = new ArrayList<>();

        for (Algorithm algorithm : algorithms) {
            String algorithmName = algorithm.getAlgorithmName();
            Double solvedRate = (double) Count.get(algorithmName) / (double) TotalCount.get(algorithmName) * 100;
            GraphDto graphDto = GraphDto.builder()
                    .algorithmName(algorithmName)
                    .solvedRate(solvedRate)
                    .build();

            graphDtos.add(graphDto);
        }

        return graphDtos;
    }
}
