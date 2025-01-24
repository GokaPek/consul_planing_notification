package ru.promo.consul_plan_notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableFeignClients
@PropertySource("classpath:application.yml")
public class ConsulPlanNotifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsulPlanNotifyApplication.class, args);
    }

}
