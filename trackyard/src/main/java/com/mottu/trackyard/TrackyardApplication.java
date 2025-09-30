package com.mottu.trackyard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
public class TrackyardApplication {
    
    @PostConstruct
    public void init() {
        // Configurar timezone para Brasil
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
    
    public static void main(String[] args) {
        SpringApplication.run(TrackyardApplication.class, args);
    }
}