package kr.co.vilez.configuration.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .order(1)
                .excludePathPatterns("/users/location")
                .excludePathPatterns("/users/login")
                .excludePathPatterns("/users/join")
                .excludePathPatterns("/users/code")
                .excludePathPatterns("/users/login/fake")
                .addPathPatterns("/users/refresh")
                .addPathPatterns("/askboard")
                .addPathPatterns("/shareboard")
                .addPathPatterns("/signs")
                .addPathPatterns("/appointments");
    }
}
