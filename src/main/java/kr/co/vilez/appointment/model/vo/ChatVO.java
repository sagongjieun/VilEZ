package kr.co.vilez.appointment.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Chat")
@AllArgsConstructor
public class ChatVO {

    private String roomId;

    private int type;

    private String content;

    private String time;

}
