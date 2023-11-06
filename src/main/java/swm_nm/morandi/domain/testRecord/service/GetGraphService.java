package swm_nm.morandi.domain.testRecord.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.testRecord.dto.GraphResponse;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetGraphService {

    //private final AlgorithmRepository algorithmRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    //private final AlgorithmProblemListRepository algorithmProblemListRepository;


    @Transactional(readOnly = true)
    public GraphResponse getMemberGraph(){
        Long memberId = SecurityUtils.getCurrentMemberId();
        List<Object[]> result = attemptProblemRepository.getAttemptStatisticsCollectByAlgorithm(memberId);

        GraphResponse graphResponseDto = new GraphResponse();

        result.forEach(objects ->
                        graphResponseDto.solvedRates
                                .replace((String) objects[0],
                                        (((Long) objects[2] * 100) / (Long) objects[1])));

        return graphResponseDto;

    }
//
//    public List<GraphDto> getGraphDtos() {
//        Long memberId = SecurityUtils.getCurrentMemberId();
//        Map<String, Long> totalCount = new HashMap<>();
//        Map<String, Long> Count = new HashMap<>();
//        List<Algorithm> algorithms = algorithmRepository.findAll();
//
//        algorithms.stream().map(Algorithm::getAlgorithmName).forEach(algorithmName -> {
//            totalCount.put(algorithmName, 0L);
//            Count.put(algorithmName, 0L);
//        });
//
//        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByMember_MemberId(memberId);
//
//        List<GraphDto> graphDtos = new ArrayList<>();
//        if (!attemptProblems.isEmpty()) {
//            attemptProblems.forEach(attemptProblem -> {
//                Long problemId = attemptProblem.getProblem().getProblemId();
//                List<AlgorithmProblemList> algorithmProblemLists =
//                        algorithmProblemListRepository.findByProblem_ProblemId(problemId);
//                algorithmProblemLists.stream().map(algorithmProblemList ->
//                        algorithmProblemList.getAlgorithm().getAlgorithmName()).forEach(algorithmName -> {
//                    Long currentTotalCount = totalCount.getOrDefault(algorithmName, 0L);
//                    Long currentCount = Count.getOrDefault(algorithmName, 0L);
//                    totalCount.put(algorithmName, currentTotalCount + 1L);
//                    if (attemptProblem.getIsSolved())
//                        Count.put(algorithmName, currentCount + 1L);
//                });
//            });
//
//            algorithms.stream().map(Algorithm::getAlgorithmName).forEach(algorithmName -> {
//                Long solvedRate = totalCount.get(algorithmName) == 0 ? 0L :
//                        Count.get(algorithmName) * 100L / totalCount.get(algorithmName);
//                GraphDto graphDto = GraphDto.builder()
//                        .algorithmName(algorithmName)
//                        .solvedRate(solvedRate)
//                        .build();
//                graphDtos.add(graphDto);
//            });
//        }
//
//        return graphDtos;
//    }
}
