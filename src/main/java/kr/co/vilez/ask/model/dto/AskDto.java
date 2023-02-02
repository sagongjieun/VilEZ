package kr.co.vilez.ask.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(value = "AskDto : 요청 게시글", description = "요청 게시글의 상세정보를 나타낸다.")
public class AskDto {
    private int id;
    private int userId;
    private String category;
    private String title;
    private String content;
    private String date;
    private String hopeAreaLat;
    private String hopeAreaLng;
    private String startDay;
    private String endDay;
    private int manner;
    private List<ImgPath2> list;
    private String nickName;
    private String area;
    private int state;
    private String address;
    private String type;

}
