package kr.co.vilez;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

public class LocalDateTests {
    @Test
    void LocalDateTest(){
        LocalDate date = LocalDate.now();
        String str = "2023-02-08";
        LocalDate date1 = LocalDate.parse(str);
        Period period = Period.between(date, date1);

        System.out.println(period.getDays());

        if(date1.isAfter(date)) {
            System.out.println("success");
        }
    }



}
