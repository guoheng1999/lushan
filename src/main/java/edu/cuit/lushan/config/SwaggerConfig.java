package edu.cuit.lushan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

//@Configuration
//@EnableSwagger2
//@ComponentScan(basePackages = { "edu.cuit.lushan.controller" })//扫描的包路径
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("edu.cuit.lushan"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(this.getParameterList())
                .ignoredParameterTypes(HttpSession.class, HttpServletRequest.class, HttpServletResponse.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("庐山数据集展示平台")
                .description("庐山数据集展示平台RESTful API接口")
                .contact("成都信息工程大学")
                .version("1.0")
                .build();
    }

    private List<Parameter> getParameterList() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("userToken")
                .description("令牌")
                .modelRef(new ModelRef(""))
                .parameterType("header")
                .required(false)
                .build();
        pars.add(tokenPar.build());
        return pars;
    }
}