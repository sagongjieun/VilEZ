package kr.co.vilez.appointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private int appointmentId;
    private int boardId;
    private String title;
    private int shareUserId;
    private int notShareUserId;
    private String appointmentStart;
    private String appointmentEnd;
    private int state;
    private int type;

}