package kr.co.vilez.appointment.model.service;

import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.vo.*;

import java.util.List;

public interface AppointmentService {
    public void deleteRoom(String roomId);
    public void saveNoReadMsg(ChatNoReadVO chatNoReadVO);
    public MapVO loadLocationByRoomId(String roomId);
    public List<ChatVO> loadMsgByRoomId(String roomId);
    public void saveLocation(MapVO mapVO);
    public RoomDto createRoom(RoomDto roomVO);

    void recvHereMsg(ChatVO chatVO);

    ChatDatasVO loadMyChatNoReadList(int userId);
}
