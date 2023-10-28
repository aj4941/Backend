package swm_nm.morandi.config.configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import swm_nm.morandi.domain.common.Language;
import swm_nm.morandi.domain.testDuring.dto.TempCodeDto;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@Configuration
@Getter
@Slf4j
public class InitialCodeConfig {

    private HashMap<Language, String> initialCode;

    @PostConstruct
    public void initialize(){
        initialCode = new HashMap<>();
        String pythonCode = readCodeFromFile("codes/temp.py");
        String cppCode = readCodeFromFile("codes/temp.cpp");
        String javaCode = readCodeFromFile("codes/temp.java");
        initialCode.put(Language.Python, pythonCode);
        initialCode.put(Language.Cpp, cppCode);
        initialCode.put(Language.Java, javaCode);
    }


    private String readCodeFromFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            log.error("Error reading code from file: " + filePath, e);
            return "";
        }
    }


}
