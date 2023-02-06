package kr.co.vilez.appointment.model.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class RoomDto {
    private int id;
    private int type;
    private int boardId;
    private int shareUserId;
    private int notShareUserId;
    private int state;
}
