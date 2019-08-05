package com.vgrazi.presentations.springcloudcontractconsumer;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Scope("prototype")
    public Stock stock() {
        return new Stock();
    }
}
