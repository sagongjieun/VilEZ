package kr.co.vilez.appointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyAppointListDto {
    private int id;
    private int userId;
    private String title;
    private String hopeAreaLat;
    private String hopeAreaLng;
    private String startDay;
    private String endDay;
    private String appointmentId;
    private String shareUserId;
    private String notShareUserId;
    private int type;
    private String date;
    private String appointmentStart;
    private String appointmentEnd;
}
