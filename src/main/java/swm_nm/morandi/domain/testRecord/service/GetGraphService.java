package swm_nm.morandi.domain.testRecord.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.member.dto.GraphDto;
import swm_nm.morandi.domain.problem.entity.Algorithm;
import swm_nm.morandi.domain.problem.entity.AlgorithmProblemList;
import swm_nm.morandi.domain.problem.repository.AlgorithmProblemListRepository;
import swm_nm.morandi.domain.problem.repository.AlgorithmRepository;
import swm_nm.morandi.domain.testExit.entity.AttemptProblem;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetGraphService {

    private final AlgorithmRepository algorithmRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    private final AlgorithmProblemListRepository algorithmProblemListRepository;
    public java.util.List<GraphDto> getGraphDtos() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Map<String, Long> totalCount = new HashMap<>();
        Map<String, Long> Count = new HashMap<>();
        java.util.List<Algorithm> algorithms = algorithmRepository.findAll();

        algorithms.stream().map(Algorithm::getAlgorithmName).forEach(algorithmName -> {
            totalCount.put(algorithmName, 0L);
            Count.put(algorithmName, 0L);
        });

        java.util.List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByMember_MemberId(memberId);

        java.util.List<GraphDto> graphDtos = new ArrayList<>();
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
