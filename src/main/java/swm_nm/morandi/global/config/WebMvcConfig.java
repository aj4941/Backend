package swm_nm.morandi.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import swm_nm.morandi.global.resolver.CurrentMemberArgumentResolver;
import swm_nm.morandi.interceptor.BaekjoonLoginInterceptor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final BaekjoonLoginInterceptor baekjoonLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baekjoonLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(List.of("/submit/cookie","/swagger-ui/**","/v3/api-docs","/swagger-resources/**","/oauths/**"));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentMemberArgumentResolver());
    }


}

