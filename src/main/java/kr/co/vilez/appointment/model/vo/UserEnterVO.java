package kr.co.vilez.appointment.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document("UserEnter")
public class UserEnterVO {
    int roomId;
    int userId;
    long time;

}
