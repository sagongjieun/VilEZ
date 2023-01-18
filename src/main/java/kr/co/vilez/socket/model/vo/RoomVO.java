package kr.co.vilez.socket.model.vo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Room")
@Data
public class RoomVO {
    private String boardId;
    private String User1;
    private String User2;
    private String roomId;
}
