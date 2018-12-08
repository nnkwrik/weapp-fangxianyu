package io.github.nnkwrik.imservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"io.github.nnkwrik", "fangxianyu.innerApi.user", "fangxianyu.innerApi.goods"})
@EnableEurekaClient
@EnableFeignClients(basePackages = {"fangxianyu.innerApi.user", "fangxianyu.innerApi.goods"})
public class ImServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImServiceApplication.class, args);
    }
}
