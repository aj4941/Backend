package swm_nm.morandi.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import swm_nm.morandi.exception.MorandiException;
import swm_nm.morandi.exception.errorcode.TestTypeErrorCode;
import swm_nm.morandi.problem.domain.Algorithm;
import swm_nm.morandi.problem.domain.AlgorithmProblemList;
import swm_nm.morandi.problem.domain.Problem;
import swm_nm.morandi.problem.domain.TypeProblemList;
import swm_nm.morandi.problem.dto.OutputDto;
import swm_nm.morandi.problem.repository.AlgorithmProblemListRepository;
import swm_nm.morandi.problem.repository.TypeProblemListRepository;
import swm_nm.morandi.test.domain.TestType;
import swm_nm.morandi.problem.dto.BojProblem;
import swm_nm.morandi.problem.dto.DifficultyLevel;
import swm_nm.morandi.problem.dto.DifficultyRange;
import swm_nm.morandi.test.dto.TestInputData;
import swm_nm.morandi.test.dto.TestTypeDto;
import swm_nm.morandi.test.mapper.TestTypeMapper;
import swm_nm.morandi.test.repository.TestTypeRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestTypeService {

    private final TestTypeRepository testTypeRepository;

    private final TypeProblemListRepository typeProblemListRepository;

    private final AlgorithmProblemListRepository algorithmProblemListRepository;

    public List<TestTypeDto> getTestTypeDtos() {
        List<TestType> testTypes = testTypeRepository.findAll();
        List<TestTypeDto> testTypeDtos = testTypes.stream().map(TestTypeMapper::convertToDto).collect(Collectors.toList());
        return testTypeDtos;
    }

    public TestTypeDto getTestTypeDto(Long testTypeId) {
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        TestTypeDto testTypeDto = TestTypeMapper.convertToDto(testType);
        return testTypeDto;
    }

    public void getProblemsByTestType(Long testTypeId, List<BojProblem> bojProblems) {
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        List<TypeProblemList> typeProblemLists = typeProblemListRepository.findByTestType_TestTypeId(testTypeId);
        List<Problem> problems = typeProblemLists.stream().map(TypeProblemList::getProblem).collect(Collectors.toList());
        List<DifficultyRange> difficultyRanges = testType.getDifficultyRanges();
        Random random = new Random();
        long randomNumber = random.nextInt(10);
        long index = 1;
        for (DifficultyRange difficultyRange : difficultyRanges) {
            int start = DifficultyLevel.getLevelByValue(difficultyRange.getStart());
            int end = DifficultyLevel.getLevelByValue(difficultyRange.getEnd());
            boolean flag = false;
            for (Problem problem : problems) {
                int problemLevel = DifficultyLevel.getLevelByValue(problem.getProblemDifficulty());
                List<AlgorithmProblemList> algorithmProblemLists
                        = algorithmProblemListRepository.findByProblem_ProblemId(problem.getProblemId());
                List<Algorithm> algorithms = algorithmProblemLists.stream().map(AlgorithmProblemList::getAlgorithm).collect(Collectors.toList());
                if (start <= problemLevel && problemLevel <= end) {
                    long testTypeAlgorithmId = randomNumber + 1;
                    for (Algorithm algorithm : algorithms) {
                        if (algorithm.getAlgorithmId() == testTypeAlgorithmId)
                            flag = true;
                    }
                    if (flag) {
                        BojProblem bojProblem = BojProblem.builder()
                                .testProblemId(index++)
                                .bojProblemId(problem.getBojProblemId())
                                .level(DifficultyLevel.getLevelByValue(problem.getProblemDifficulty()))
                                .levelToString(problem.getProblemDifficulty().getFullName()).build();
                        bojProblems.add(bojProblem);
                        randomNumber = (randomNumber + 1) % 10;
                        break;
                    }
                }
            }
            if (!flag) {
                BojProblem bojProblem = BojProblem.builder()
                        .testProblemId(index++)
                        .bojProblemId(0L)
                        .build();
                bojProblems.add(bojProblem);
            }
        }
    }

    public void getProblemsByApi(Long testTypeId, String bojId, List<BojProblem> bojProblems)
            throws JsonProcessingException {
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        List<DifficultyRange> difficultyRanges = testType.getDifficultyRanges();
        long index = 1;
        for (DifficultyRange difficultyRange : difficultyRanges) {
            if (bojProblems.get((int) (index - 1)).getBojProblemId() != 0) {
                index++;
                continue;
            }
            String start = difficultyRange.getStart().getShortName();
            String end = difficultyRange.getEnd().getShortName();
            String apiUrl = "https://solved.ac/api/v3/search/problem";
            String query = String.format("tier:%s..%s ~solved_by:%s solved:1000..", start, end, bojId);
            WebClient webClient = WebClient.builder().build();
            String jsonString = webClient.get()
                    .uri(apiUrl + "?query=" + query + "&page=1" + "&sort=random")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonString);
            JsonNode itemsArray = rootNode.get("items");
            if (itemsArray != null && itemsArray.isArray() && itemsArray.size() > 0) {
                JsonNode firstProblem = itemsArray.get(0);
                BojProblem apiProblem = mapper.treeToValue(firstProblem, BojProblem.class);
                BojProblem bojProblem = bojProblems.get((int) (index - 1));
                bojProblem.setBojProblemId(apiProblem.getBojProblemId());
                bojProblem.setLevel(apiProblem.getLevel());
                bojProblem.setTestProblemId(index++);
                bojProblem.setLevelToString(DifficultyLevel.getValueByLevel(bojProblem.getLevel()));
            }
        }
    }

    public OutputDto runCode(TestInputData testInputData) throws IOException, InterruptedException {
        String language = testInputData.getLanguage();
        String code = testInputData.getCode();
        String input = testInputData.getInput();
        OutputDto outputDto = new OutputDto();
        if (language.equals("Python"))
            outputDto.setOutput(runPython(code, input));
        else if (language.equals("Cpp"))
            outputDto.setOutput(runCpp(code, input));
        else if (language.equals("Java"))
            outputDto.setOutput(runJava(code, input));

        return outputDto;
    }
    public String runPython(String code, String input)
            throws InterruptedException, IOException {

        try {
//          File codeFile = new File("temp.py");
//          code = new String(Files.readAllBytes(codeFile.toPath()), StandardCharsets.UTF_8);

            ProcessBuilder pb = new ProcessBuilder("python3", "-c", code);
            pb.redirectErrorStream(true);

            Process p = pb.start();

//          File inputFile = new File("input.txt");
//          input = new String(Files.readAllBytes(inputFile.toPath()), StandardCharsets.UTF_8);

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
            saveCodeToFile(tempFileName, code); // file 입력 시 주석 처리

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

//            String inputFileName = "input.txt";
//            StringBuilder inputText = new StringBuilder();
//            try (BufferedReader fileReader = new BufferedReader(new FileReader(inputFileName))) {
//                String line;
//                while ((line = fileReader.readLine()) != null) {
//                    inputText.append(line).append("\n");
//                }
//            }

            if (input != null) { // File 사용시 inputText.toString()
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
                writer.write(input);
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

    public String runJava(String code, String input) throws IOException {
        try {
            File javaFile = new File("Main.java");
//          code = new String(Files.readAllBytes(javaFile.toPath()), StandardCharsets.UTF_8);
            Files.write(javaFile.toPath(), code.getBytes(StandardCharsets.UTF_8));

            // 컴파일된 클래스 파일 생성
            ProcessBuilder compilePb = new ProcessBuilder("javac", javaFile.getName());
            compilePb.redirectErrorStream(true);
            Process compileProcess = compilePb.start();
            compileProcess.waitFor();

            // 컴파일 에러가 있는지 확인
            BufferedReader compileReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
            StringBuilder compileOutput = new StringBuilder();
            String compileLine;
            while ((compileLine = compileReader.readLine()) != null) {
                compileOutput.append(compileLine).append("\n");
            }
            if (compileOutput.length() > 0) {
                return "Compile Error:\n" + compileOutput.toString();
            }

            // 실행
            ProcessBuilder pb = new ProcessBuilder("java", "Main");
            pb.redirectErrorStream(true);

            Process p = pb.start();

            File inputFile = new File("input.txt");
            Files.write(inputFile.toPath(), input.getBytes(StandardCharsets.UTF_8));
//          input = new String(Files.readAllBytes(inputFile.toPath()), StandardCharsets.UTF_8);

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
                    return "Time Limit Exceeded";
                }
            }

            boolean processFinished = p.waitFor(10, TimeUnit.SECONDS);
            if (!processFinished) {
                p.destroy();
                return "Execution Time Exceeded";
            }

            return output.toString();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error running Java process: " + e.getMessage(), e);
        }
    }
}
