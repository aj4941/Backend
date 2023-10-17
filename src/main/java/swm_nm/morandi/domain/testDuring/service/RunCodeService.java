package swm_nm.morandi.domain.testDuring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testDuring.dto.InputData;
import swm_nm.morandi.domain.testDuring.dto.OutputDto;
import swm_nm.morandi.domain.testDuring.dto.TestInputData;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RunCodeService {

    private final TempCodeService tempCodeService;

    @Value("${compile.url}")
    public String url;

    @Value("${compile.tc-url}")
    public String tcUrl;

    public OutputDto runCode(InputData inputData) throws Exception {

        tempCodeService.saveTempCode(inputData.getTestId(), inputData.getProblemNumber(),
                inputData.getLanguage(), inputData.getCode());

        CloseableHttpClient httpClient = HttpClients.createDefault();

        ObjectMapper objectMapper = new ObjectMapper();
        String inputDataJson = objectMapper.writeValueAsString(inputData);

        // Create POST request
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(inputDataJson));

        // Send POST request
        HttpResponse response = httpClient.execute(httpPost);

        // Check response status code
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String responseJson = EntityUtils.toString(response.getEntity());
            OutputDto outputDto = objectMapper.readValue(responseJson, OutputDto.class);
            return outputDto;
        } else {
            throw new Exception("HTTP request failed with status code: " + statusCode);
        }
    }
    public List<OutputDto> runTestCaseCode(TestInputData testInputData) throws Exception {

        tempCodeService.saveTempCode(testInputData.getTestId(),
                testInputData.getProblemNumber(), testInputData.getLanguage(), testInputData.getCode());

        CloseableHttpClient httpClient = HttpClients.createDefault();

        ObjectMapper objectMapper = new ObjectMapper();
        String inputDataJson = objectMapper.writeValueAsString(testInputData);

        // Create POST request
        HttpPost httpPost = new HttpPost(tcUrl);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(inputDataJson));

        // Send POST request
        HttpResponse response = httpClient.execute(httpPost);

        // Check response status code
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String responseJson = EntityUtils.toString(response.getEntity());
            List<OutputDto> outputDtos = objectMapper.readValue(responseJson, new TypeReference<List<OutputDto>>() {});
            return outputDtos;
        } else {
            throw new Exception("HTTP request failed with status code: " + statusCode);
        }
    }
}
