package com.bsktpay.dfs.config;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.api-key}")
    private String expectedApiKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeRequests()
            .anyRequest().permitAll()
            .and()
            .addFilterBefore(new ApiKeyFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private class ApiKeyFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String receivedApiKey = httpRequest.getHeader("API_KEY");

            if (httpRequest.getServletPath().startsWith("/api/v1/dfs/")) {
                if (receivedApiKey == null || !receivedApiKey.equals(expectedApiKey)) {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    httpResponse.getWriter().write("Unauthorized: API key is missing or incorrect");
                    return;
                }
            }

            chain.doFilter(request, response);
        }
    }
}
