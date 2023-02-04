package kr.co.vilez.configuration.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        consumes.add("multipart/form-data");
        return consumes;
    }

    @Bean
    public Docket UserCommentApi() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("User  API")
                .description("<h3>User API에 대한 문서를 제공한다.</h3>")
                .version("0.0").build();

        return new Docket(DocumentationType.SWAGGER_2) // Swagger 2.0 기반의 문서 작성
                .apiInfo(apiInfo) // 문서에 대한 정보를 설정한다.
                .groupName("b. User")
                .select() // ApiSelectorBuilder를 반환하며 상세한 설정 처리
                .apis(RequestHandlerSelectors.basePackage("kr.co.vilez.user"))// 대상으로하는 api 설정
                .paths(PathSelectors.any()) // controller에서 swagger를 지정할 대상 path 설정
                .build();  // Docket 객체 생성
    }
    @Bean
    public Docket EmailCommentApi() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("Email  API")
                .description("<h3>Email API에 대한 문서를 제공한다.</h3>")
                .version("0.0").build();

        return new Docket(DocumentationType.SWAGGER_2) // Swagger 2.0 기반의 문서 작성
                .apiInfo(apiInfo) // 문서에 대한 정보를 설정한다.
                .groupName("c. Email")
                .select() // ApiSelectorBuilder를 반환하며 상세한 설정 처리
                .apis(RequestHandlerSelectors.basePackage("kr.co.vilez.email"))// 대상으로하는 api 설정
                .paths(PathSelectors.any()) // controller에서 swagger를 지정할 대상 path 설정
                .build();  // Docket 객체 생성
    }
    @Bean
    public Docket ShareCommentApi() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("Share  API")
                .description("<h3>Email API에 대한 문서를 제공한다.</h3>")
                .version("0.0").build();

        return new Docket(DocumentationType.SWAGGER_2) // Swagger 2.0 기반의 문서 작성
                .apiInfo(apiInfo) // 문서에 대한 정보를 설정한다.
                .groupName("d. Share")
                .select() // ApiSelectorBuilder를 반환하며 상세한 설정 처리
                .apis(RequestHandlerSelectors.basePackage("kr.co.vilez.share"))// 대상으로하는 api 설정
                .paths(PathSelectors.any()) // controller에서 swagger를 지정할 대상 path 설정
                .build();  // Docket 객체 생성
    }

    @Bean
    public Docket AskCommentApi() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("Ask  API")
                .description("<h3>Ask API에 대한 문서를 제공한다.</h3>")
                .version("0.0").build();

        return new Docket(DocumentationType.SWAGGER_2) // Swagger 2.0 기반의 문서 작성
                .consumes(getConsumeContentTypes())
                .apiInfo(apiInfo) // 문서에 대한 정보를 설정한다.
                .groupName("e. Ask")
                .select() // ApiSelectorBuilder를 반환하며 상세한 설정 처리
                .apis(RequestHandlerSelectors.basePackage("kr.co.vilez.ask"))// 대상으로하는 api 설정
                .paths(PathSelectors.any()) // controller에서 swagger를 지정할 대상 path 설정
                .build();  // Docket 객체 생성
    }
    @Bean
    public Docket OAuthCommentApi() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("OAuth  API")
                .description("<h3>OAuth API에 대한 문서를 제공한다.</h3>")
                .version("0.0").build();

        return new Docket(DocumentationType.SWAGGER_2) // Swagger 2.0 기반의 문서 작성
                .consumes(getConsumeContentTypes())
                .apiInfo(apiInfo) // 문서에 대한 정보를 설정한다.
                .groupName("f. OAuth")
                .select() // ApiSelectorBuilder를 반환하며 상세한 설정 처리
                .apis(RequestHandlerSelectors.basePackage("kr.co.vilez.oauth"))// 대상으로하는 api 설정
                .paths(PathSelectors.any()) // controller에서 swagger를 지정할 대상 path 설정
                .build();  // Docket 객체 생성
    }

    @Bean
    public Docket AppointmentCommentApi() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("Appointment  API")
                .description("<h3>Appointment API에 대한 문서를 제공한다.</h3>")
                .version("0.0").build();

        return new Docket(DocumentationType.SWAGGER_2) // Swagger 2.0 기반의 문서 작성
                .consumes(getConsumeContentTypes())
                .apiInfo(apiInfo) // 문서에 대한 정보를 설정한다.
                .groupName("g. Appointment")
                .select() // ApiSelectorBuilder를 반환하며 상세한 설정 처리
                .apis(RequestHandlerSelectors.basePackage("kr.co.vilez.appointment"))// 대상으로하는 api 설정
                .paths(PathSelectors.any()) // controller에서 swagger를 지정할 대상 path 설정
                .build();  // Docket 객체 생성
    }
    @Bean
    public Docket SocketCommentApi() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("User  API")
                .description("<h3>Socket API에 대한 문서를 제공한다.</h3>")
                .version("0.0").build();

        return new Docket(DocumentationType.SWAGGER_2) // Swagger 2.0 기반의 문서 작성
                .apiInfo(apiInfo) // 문서에 대한 정보를 설정한다.
                .groupName("z. Socket (사용X 권장)")
                .select() // ApiSelectorBuilder를 반환하며 상세한 설정 처리
                .apis(RequestHandlerSelectors.basePackage("kr.co.vilez.socket"))// 대상으로하는 api 설정
                .paths(PathSelectors.any()) // controller에서 swagger를 지정할 대상 path 설정
                .build();  // Docket 객체 생성
    }
    @Bean
    public Docket SignCommentApi() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("Sign  API")
                .description("<h3>Sign API에 대한 문서를 제공한다.</h3>")
                .version("0.0").build();

        return new Docket(DocumentationType.SWAGGER_2) // Swagger 2.0 기반의 문서 작성
                .apiInfo(apiInfo) // 문서에 대한 정보를 설정한다.
                .groupName("h. Sign")
                .select() // ApiSelectorBuilder를 반환하며 상세한 설정 처리
                .apis(RequestHandlerSelectors.basePackage("kr.co.vilez.sign"))// 대상으로하는 api 설정
                .paths(PathSelectors.any()) // controller에서 swagger를 지정할 대상 path 설정
                .build();  // Docket 객체 생성
    }
    @Bean
    public Docket QrCommentApi() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("Qr  API")
                .description("<h3>Qr API에 대한 문서를 제공한다.</h3>")
                .version("0.0").build();

        return new Docket(DocumentationType.SWAGGER_2) // Swagger 2.0 기반의 문서 작성
                .apiInfo(apiInfo) // 문서에 대한 정보를 설정한다.
                .groupName("I. Qr")
                .select() // ApiSelectorBuilder를 반환하며 상세한 설정 처리
                .apis(RequestHandlerSelectors.basePackage("kr.co.vilez.qr"))// 대상으로하는 api 설정
                .paths(PathSelectors.any()) // controller에서 swagger를 지정할 대상 path 설정
                .build();  // Docket 객체 생성
    }
    @Bean
    public Docket BackCommentApi() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("Back  API")
                .description("<h3>Back API에 대한 문서를 제공한다.</h3>")
                .version("0.0").build();

        return new Docket(DocumentationType.SWAGGER_2) // Swagger 2.0 기반의 문서 작성
                .apiInfo(apiInfo) // 문서에 대한 정보를 설정한다.
                .groupName("J. Back")
                .select() // ApiSelectorBuilder를 반환하며 상세한 설정 처리
                .apis(RequestHandlerSelectors.basePackage("kr.co.vilez.back"))// 대상으로하는 api 설정
                .paths(PathSelectors.any()) // controller에서 swagger를 지정할 대상 path 설정
                .build();  // Docket 객체 생성
    }

    @Bean
    public Docket AllApi() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("전체 API")
                .description("<h3>모든 Api에 대한 문서를 제공한다.</h3>")
                .version("0.0").build();

        return new Docket(DocumentationType.SWAGGER_2) // Swagger 2.0 기반의 문서 작성
                .consumes(getConsumeContentTypes())
                .apiInfo(apiInfo) // 문서에 대한 정보를 설정한다.

                .groupName("a. 전체")
                .select() // ApiSelectorBuilder를 반환하며 상세한 설정 처리
                .apis(RequestHandlerSelectors.basePackage("kr.co.vilez"))// 대상으로하는 api 설정
                .paths(PathSelectors.any()) // controller에서 swagger를 지정할 대상 path 설정
                .build();  // Docket 객체 생성
    }


}