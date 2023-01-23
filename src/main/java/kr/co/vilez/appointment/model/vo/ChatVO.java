package kr.co.vilez.appointment.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Chat")
public class ChatVO {

    private int roomId;
    private int boardId;
    private int type;
    private int fromUserId;
    private int toUserId;
    private String content;
    private long time;

}
