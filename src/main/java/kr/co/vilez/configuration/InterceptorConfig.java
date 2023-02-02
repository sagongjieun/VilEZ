package kr.co.vilez.configuration;

import kr.co.vilez.jwt.JwtProvider;
import kr.co.vilez.user.interceptor.UserInterceptor;
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
                .excludePathPatterns("/users/refresh")
                .excludePathPatterns("/users/code")
                .addPathPatterns("/users/**");
    }
}
