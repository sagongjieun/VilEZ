package kr.co.vilez.data;

import lombok.Data;

import java.util.List;

@Data
public class HttpVO {
    private String flag = "fail";
    private List<?> data;

}
