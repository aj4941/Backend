package swm_nm.morandi.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import swm_nm.morandi.test.domain.TestType;
import swm_nm.morandi.problem.dto.BojProblem;
import swm_nm.morandi.problem.dto.DifficultyLevel;
import swm_nm.morandi.problem.dto.DifficultyRange;
import swm_nm.morandi.test.dto.TestTypeDto;
import swm_nm.morandi.test.mapper.TestTypeMapper;
import swm_nm.morandi.test.repository.TestTypeRepository;

import java.io.*;
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

    public String runCode(String language, String code, String input) throws IOException, InterruptedException {
        if (language.equals("Python")) {
            return runPython(code, input);
        }
        else if (language.equals("Cpp")) {
            return runCpp(code, input);
        }
        else if (language.equals("Java")) {
            return runPython(code, input);
        }
        return "Error";
    }
    public String runPython(String code, String input)
            throws InterruptedException, IOException {
        try {
            ProcessBuilder pb = new ProcessBuilder("python3", "-c", code);
            pb.redirectErrorStream(true);

            Process p = pb.start();

            if (input != null) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                writer.write(input);
                writer.newLine();
                writer.flush();
                writer.close();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            long startTime = System.currentTimeMillis();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                if (System.currentTimeMillis() - startTime >= 12000) {
                    return "Time Limit Exceed";
                }
            }

            return output.toString();

        } catch (IOException e) {
            throw new RuntimeException("Error running Python process: " + e.getMessage(), e);
        }
    }

    public String runCpp(String code, String input) throws InterruptedException, IOException {
        try {
            String tempFileName = "temp.cpp";
//            saveCodeToFile(tempFileName, code);

            String executableFileName = "temp.out";
            String compileCommand = "g++ -std=c++17 " + tempFileName + " -o " + executableFileName;
            Process compileProcess = Runtime.getRuntime().exec(compileCommand);
            compileProcess.waitFor();

            if (compileProcess.exitValue() != 0) {
                StringBuilder errorMessage = new StringBuilder();
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()))) {
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorMessage.append(line).append("\n");
                    }
                }
                return errorMessage.toString();
            }

            String runCommand = "./" + executableFileName;
            Process runProcess = Runtime.getRuntime().exec(runCommand);

            String inputFileName = "input.txt";
            StringBuilder inputText = new StringBuilder();
            try (BufferedReader fileReader = new BufferedReader(new FileReader(inputFileName))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    inputText.append(line).append("\n");
                }
            }

            if (inputText.toString() != null) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
                writer.write(inputText.toString());
                writer.newLine();
                writer.flush();
                writer.close();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            long startTime = System.currentTimeMillis();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                if (System.currentTimeMillis() - startTime >= 12000) {
                    return "Time Limit Exceed";
                }
            }

            return output.toString();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error running C++ process: " + e.getMessage(), e);
        }
    }

    private void saveCodeToFile(String fileName, String code) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
