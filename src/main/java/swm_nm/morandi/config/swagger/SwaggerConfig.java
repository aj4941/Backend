package swm_nm.morandi.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Server;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    Server serverLocal = new Server("local", "http://localhost:8080", "for local usages", Collections.emptyList(), Collections.emptyList());
    Server testServer = new Server("test", "https://api.morandi.co.kr", "for testing", Collections.emptyList(), Collections.emptyList());
    @Bean
    public Docket memberApi() {
        return new Docket(DocumentationType.OAS_30)
                .servers(serverLocal, testServer)
                .groupName("member-api")
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("swm_nm.morandi.domain.member.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    public Docket testDuringApi() {
        return new Docket(DocumentationType.OAS_30)
                .servers(serverLocal, testServer)
                .groupName("test-during-api")
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("swm_nm.morandi.domain.testDuring.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    public Docket testExitApi() {
        return new Docket(DocumentationType.OAS_30)
                .servers(serverLocal, testServer)
                .groupName("test-exit-api")
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("swm_nm.morandi.domain.testExit.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    public Docket testInfoApi() {
        return new Docket(DocumentationType.OAS_30)
                .servers(serverLocal, testServer)
                .groupName("test-info-api")
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("swm_nm.morandi.domain.testInfo.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }
    @Bean
    public Docket testRecordApi() {
        return new Docket(DocumentationType.OAS_30)
                .servers(serverLocal, testServer)
                .groupName("test-record-api")
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("swm_nm.morandi.domain.testRecord.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }
    @Bean
    public Docket testStartApi() {
        return new Docket(DocumentationType.OAS_30)
                .servers(serverLocal, testServer)
                .groupName("test-start-api")
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("swm_nm.morandi.domain.testStart.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Practice Swagger")
                .description("practice swagger config")
                .version("1.0")
                .build();
    }
}
