package swm_nm.morandi.domain.testDuring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.testDuring.dto.OutputDto;
import swm_nm.morandi.domain.testDuring.dto.TestInputData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RunCodeServiceTest {

    @Autowired
    private RunCodeService runCodeService;
    @Test
    @Transactional
    void pythonRunCodeTest() throws Exception {
        // given
        TestInputData testInputData = new TestInputData();
        testInputData.setCode(readFileContent("temp.py"));
        testInputData.setInput(null);
        testInputData.setLanguage("Python");

        // when
        OutputDto outputDto = runCodeService.runCode(testInputData);

        // then
        assertThat(outputDto.getOutput()).isEqualTo("168\n");
    }

    @Test
    @Transactional
    void cppRunCodeTest() throws Exception {
        // given
        TestInputData testInputData = new TestInputData();
        testInputData.setCode(readFileContent("temp.cpp"));
        testInputData.setInput("1 2");
        testInputData.setLanguage("Cpp");

        // when
        OutputDto outputDto = runCodeService.runCode(testInputData);

        // then
        assertThat(outputDto.getOutput()).isEqualTo("3\n");
    }

    @Test
    @Transactional
    void javaRunCodeTest() throws Exception {
        // given
        TestInputData testInputData = new TestInputData();
        testInputData.setCode(readFileContent("Main.java"));
        testInputData.setInput("1 2");
        testInputData.setLanguage("Java");

        // when
        OutputDto outputDto = runCodeService.runCode(testInputData);

        // then
        assertThat(outputDto.getOutput()).isEqualTo("3\n");
    }

    private String readFileContent(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }
}