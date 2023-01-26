package kr.co.vilez;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan(basePackages = {"kr.co.vilez.*.model.mapper"})
public class VilezApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(VilezApplication.class, args);
    }

}
