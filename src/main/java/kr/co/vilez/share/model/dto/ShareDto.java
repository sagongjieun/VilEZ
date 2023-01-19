package kr.co.vilez.share.model.dto;

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
@ApiModel(value = "ShareDto : 공유 게시글", description = "공유 게시글의 상세정보를 나타낸다.")
public class ShareDto {
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
    List<ImgPath> list;
    int state;

}
