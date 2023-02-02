package kr.co.vilez.appointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "check")
public class SetPeriodDto {
    int boardId;
    int shareUserId;
    int notShareUserId;
    String startDay;
    String endDay;
    int type;
}
