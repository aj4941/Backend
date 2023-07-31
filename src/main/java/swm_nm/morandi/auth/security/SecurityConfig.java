package swm_nm.morandi.auth.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import swm_nm.morandi.auth.filter.FilterConfig;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final FilterConfig filterConfig;
    //private final JwtAuthException jwtAuthException;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

     http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()

                .authorizeRequests()
                .antMatchers("/oauths/**").permitAll()
                .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                  .and()
                  .apply(filterConfig);

        return http.build();
    }
}
