package kr.co.vilez.socket.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("chat")
@AllArgsConstructor
public class SocketVO {

    private String roomId;

    private String type;

    private String content;

    private String lat;

    private String lng;

    private String level;

}
