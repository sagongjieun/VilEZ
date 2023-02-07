package kr.co.vilez.appointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private int roomId;
    private int appointmentId;
    private int boardId;
    private int shareUserId;
    private int notShareUserId;
    private String appointmentStart;
    private String appointmentEnd;
    private String title;
    private int state;
    private String date;
    private int type;
    private String status;

}