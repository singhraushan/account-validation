package com.rau.jp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties
public class ProviderProperties {
    private final Map<String,String> url = new HashMap<>();

    public Map<String, String> getUrl() {
        return url;
    }
}
