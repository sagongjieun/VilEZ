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
String title;
String hopeAreaLat;
String hopeAreaLng;
String startDay;
String endDay;
String appointmentId;
String shareUserId;
String notShareUserId;
int type;
}
