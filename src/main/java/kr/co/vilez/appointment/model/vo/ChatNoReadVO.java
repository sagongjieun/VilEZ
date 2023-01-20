package kr.co.vilez.appointment.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("ChatNoRead")
@AllArgsConstructor
public class ChatNoReadVO {

    private String roomId;

    private int type; // 공유자 : 1 피공유자 : 2

    private int userId; // 받을 userId (mongo 검색용)

    private String content;

    private String time;

}
