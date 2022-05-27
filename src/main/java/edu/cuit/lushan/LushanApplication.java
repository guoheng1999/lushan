package edu.cuit.lushan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(value = "edu.cuit.lushan.mapper")
@EnableScheduling
public class LushanApplication {
    public static void main(String[] args) {
        SpringApplication.run(LushanApplication.class, args);
    }

}
