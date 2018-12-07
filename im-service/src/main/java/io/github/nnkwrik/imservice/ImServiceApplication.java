package io.github.nnkwrik.imservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "io.github.nnkwrik")
@EnableEurekaClient
@EnableFeignClients
public class ImServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImServiceApplication.class, args);
    }
}
