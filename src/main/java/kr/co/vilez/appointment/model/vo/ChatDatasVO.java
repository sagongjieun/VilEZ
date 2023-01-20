package kr.co.vilez.appointment.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class ChatDatasVO {
    int userId;
    int noReadCount;
    ArrayList<ChatLastVO> chatData;
}
