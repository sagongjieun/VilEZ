package kr.co.vilez.appointment.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document("ChatLast")
public class ChatLastVO implements Comparable<ChatLastVO>{
    int roomId;
    int toUserId;
    int fromUserId;
    String content;
    long time;

    @Override
    public int compareTo(ChatLastVO o) {
        return -Long.compare(this.time,o.time);
    }
}
