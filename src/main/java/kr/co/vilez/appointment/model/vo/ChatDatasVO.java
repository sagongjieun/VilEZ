package kr.co.vilez.appointment.model.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class ChatDatasVO {
    int userId;
    int noReadCount;
    List<ChatVO> chatData;
}
