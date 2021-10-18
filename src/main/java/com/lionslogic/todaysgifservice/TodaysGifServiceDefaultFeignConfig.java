package com.lionslogic.todaysgifservice;

import feign.Client;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TodaysGifServiceDefaultFeignConfig {
    @Bean
    public Client client() {
        return new OkHttpClient();
    }

    @Bean
    public Encoder encoder() {
        return new GsonEncoder();
    }

    @Bean
    public Decoder decoder() {
        return new GsonDecoder();
    }

    @Bean
    public Logger.Level logLevel() {
        return Logger.Level.FULL;
    }
}
