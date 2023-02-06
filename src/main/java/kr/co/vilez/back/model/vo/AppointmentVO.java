package kr.co.vilez.back.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentVO {
    private int appointmentId;
    private int boardId;
    private int shareUserId;
    private int notShareUserId;
    private int type;
}
