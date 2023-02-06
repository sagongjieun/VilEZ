package kr.co.vilez.share.model.dto;

import kr.co.vilez.ask.model.dto.ImgPath2;
import lombok.Data;

import java.util.List;

@Data
public class ShareListDto {
    int id;
    int userId;
    String category;
    String title;
    String content;
    String date;
    String hopeAreaLat;
    String hopeAreaLng;
    String startDay;
    String endDay;
    int state;
    String path;
    private int manner;
    private List<ImgPath> list;
    private String nickName;
    private String address;
}
