package swm_nm.morandi.config.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000",
                "http://morandi.co.kr",
                "https://morandi.co.kr",
                "http://api.morandi.co.kr",
                "https://api.morandi.co.kr",
                "chrome-extension://ljkmahbkojffhjdjkghaljooajocajnf",//배포된 크롬 익스텐션
                "chrome-extension://cmblaiddbfchipealeopkbbnboifeedc",
                "chrome-extension://ckepgfjakcdkjpabldbamcfcjhcdojih"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}