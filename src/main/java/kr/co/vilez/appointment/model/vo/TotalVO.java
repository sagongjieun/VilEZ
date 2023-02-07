package kr.co.vilez.appointment.model.vo;

import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.share.model.dto.ImgPath;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalVO {
    AppointmentDto appointmentDto;
    Object imgPath;
    int bookmarkCnt;
}
