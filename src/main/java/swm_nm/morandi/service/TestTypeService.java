package swm_nm.morandi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import swm_nm.morandi.domain.TestType;
import swm_nm.morandi.dto.BojProblem;
import swm_nm.morandi.dto.DifficultyLevel;
import swm_nm.morandi.dto.DifficultyRange;
import swm_nm.morandi.dto.TestTypeDto;
import swm_nm.morandi.mapper.TestTypeMapper;
import swm_nm.morandi.repository.TestTypeRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestTypeService {

    private final TestTypeRepository testTypeRepository;

    public List<TestTypeDto> getTestTypeDtos() {
        List<TestType> testTypes = testTypeRepository.findAll();
        List<TestTypeDto> testTypeDtos = new ArrayList<>();
        for (TestType testType : testTypes) {
            TestTypeDto testTypeDto = TestTypeMapper.convertToDto(testType);
            testTypeDtos.add(testTypeDto);
        }
        return testTypeDtos;
    }

    public TestTypeDto getTestTypeDto(Long testTypeId) {
        Optional<TestType> res = testTypeRepository.findById(testTypeId);
        TestType testType = res.get();
        TestTypeDto testTypeDto = TestTypeMapper.convertToDto(testType);
        return testTypeDto;
    }

    public List<BojProblem> getBojProblems(Long testTypeId, String bojId) throws JsonProcessingException {
        List<BojProblem> bojProblems = new ArrayList<>();
        Optional<TestType> result = testTypeRepository.findById(testTypeId);
        TestType testType = result.get();
        List<DifficultyRange> difficultyRanges = testType.getDifficultyRanges();
        long index = 1;
        for (DifficultyRange difficultyRange : difficultyRanges) {
            String start = difficultyRange.getStart().getShortName();
            String end = difficultyRange.getEnd().getShortName();
            String apiUrl = "https://solved.ac/api/v3/search/problem";
            String query = String.format("tier:%s..%s ~solved_by:%s", start, end, bojId);
            WebClient webClient = WebClient.builder().build();
            String jsonString = webClient.get()
                    .uri(apiUrl + "?query=" + query + "&page=1" + "&sort=random")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonString);
            JsonNode itemsArray = rootNode.get("items");
            if (itemsArray != null && itemsArray.isArray() && itemsArray.size() > 0) {
                JsonNode firstProblem = itemsArray.get(0);
                BojProblem bojProblem = mapper.treeToValue(firstProblem, BojProblem.class); // 문제 번호, 난이도
                bojProblem.setTestProblemId(index++);
                bojProblem.setLevelToString(DifficultyLevel.getValueByLevel(bojProblem.getLevel()));
                bojProblems.add(bojProblem);
            }
        }

        return bojProblems;
    }
}
