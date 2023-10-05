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
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testDuring.dto.OutputDto;
import swm_nm.morandi.domain.testDuring.dto.TestInputData;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RunCodeService {
    public List<OutputDto> runCode(TestInputData testInputData) throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "http://10.0.102.184:8080";

        ObjectMapper objectMapper = new ObjectMapper();
        String inputDataJson = objectMapper.writeValueAsString(testInputData);

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
            List<OutputDto> outputDtos = objectMapper.readValue(responseJson, new TypeReference<List<OutputDto>>() {});
            return outputDtos;
        } else {
            throw new Exception("HTTP request failed with status code: " + statusCode);
        }
    }
}
