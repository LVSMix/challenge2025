package org.example.challenge.infraestrcture.config;

import org.example.challenge.infraestrcture.filter.RateLimitingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter() {
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter());
        registrationBean.addUrlPatterns("/api/*"); // Aplica el filtro a todas las rutas que comiencen con /api
        registrationBean.setOrder(1); // Prioridad del filtro
        return registrationBean;
    }
}
