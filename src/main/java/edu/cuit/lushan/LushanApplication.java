package edu.cuit.lushan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "edu.cuit.lushan.mapper")
public class LushanApplication {

    public static void main(String[] args) {
        SpringApplication.run(LushanApplication.class, args);
    }

}
