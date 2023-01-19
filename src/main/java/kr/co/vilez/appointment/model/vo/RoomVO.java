package kr.co.vilez.appointment.model.vo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Room")
@Data
public class RoomVO {
    private int boardId;
    private int user1;
    private int user2;
    private String roomId;
}
