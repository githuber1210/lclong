package com.example.es.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean
    public Docket MyDocket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                //接口信息
                .apiInfo(new ApiInfoBuilder()
                        .title("接口文档")
                        .description("lclong-admin的接口")
                        .contact("lclong")
                        .termsOfServiceUrl("43.138.224.38")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("1.0")
                .select()
                //扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.es.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
