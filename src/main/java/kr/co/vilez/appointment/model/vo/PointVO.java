package kr.co.vilez.appointment.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("point")
public class PointVO {
    int boardId;
    int userId;
    int type;
    int point;
    String date;
}
