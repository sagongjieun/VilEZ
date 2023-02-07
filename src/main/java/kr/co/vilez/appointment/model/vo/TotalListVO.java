package kr.co.vilez.appointment.model.vo;

import kr.co.vilez.appointment.model.dto.MyAppointListDto;
import kr.co.vilez.share.model.dto.ImgPath;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalListVO {
    MyAppointListDto myAppointListVO;
    int bookmarkCnt;
    Object imgPathList;
}
