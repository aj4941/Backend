package swm_nm.morandi.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Server;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import swm_nm.morandi.global.annotations.CurrentMember;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    Server serverLocal = new Server("local", "http://localhost:8080", "for local usages", Collections.emptyList(), Collections.emptyList());
    Server testServer = new Server("test", "https://api.morandi.co.kr", "for testing", Collections.emptyList(), Collections.emptyList());

    private Docket createDocket(String groupName, String basePackage) {
        return new Docket(DocumentationType.OAS_30)
                .servers(serverLocal, testServer)
                .groupName(groupName)
                .useDefaultResponseMessages(false)
                .ignoredParameterTypes(CurrentMember.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    // 공통으로 사용할 ApiInfo 정의
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Practice Swagger")
                .description("practice swagger config")
                .version("1.0")
                .build();
    }

    @Bean
    public Docket memberApi() {
        return createDocket("member-api", "swm_nm.morandi.domain.member.controller");
    }

    @Bean
    public Docket testDuringApi() {
        return createDocket("test-during-api", "swm_nm.morandi.domain.testDuring.controller");
    }

    @Bean
    public Docket testExitApi() {
        return createDocket("test-exit-api", "swm_nm.morandi.domain.testExit.controller");
    }

    @Bean
    public Docket testInfoApi() {
        return createDocket("test-info-api", "swm_nm.morandi.domain.testInfo.controller");
    }

    @Bean
    public Docket testRecordApi() {
        return createDocket("test-record-api", "swm_nm.morandi.domain.testRecord.controller");
    }

    @Bean
    public Docket testStartApi() {
        return createDocket("test-start-api", "swm_nm.morandi.domain.testStart.controller");
    }

    @Bean
    public Docket codeSubmitApi() {
        return createDocket("code-submit-api", "swm_nm.morandi.domain.codeSubmit.controller");
    }

    @Bean
    public Docket practiceProblemApi() {
        return createDocket("practice-api", "swm_nm.morandi.domain.practice.controller");
    }

    @Bean
    public Docket testRetryApi() {
        return createDocket("test-retry-api", "swm_nm.morandi.domain.testRetry.controller");
    }

}
