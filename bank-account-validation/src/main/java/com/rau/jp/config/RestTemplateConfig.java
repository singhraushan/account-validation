/*
package com.rau.jp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Value("${http.connection.timeout}")
    Integer connectionTimeout;

    @Value("${http.read.timeout}")
    Integer readTimeout;

    @Bean
    //@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
    }

}*/
