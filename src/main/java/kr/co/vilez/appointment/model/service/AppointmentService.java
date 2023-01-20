package kr.co.vilez.appointment.model.service;

import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.vo.*;
import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.vo.ChatNoReadVO;
import kr.co.vilez.appointment.model.vo.ChatVO;
import kr.co.vilez.appointment.model.vo.MapVO;

import java.util.List;

public interface AppointmentService {
    public List<AppointmentDto> getAppointmentList(AppointmentDto appointmentDto) throws Exception;
    public void create(AppointmentDto appointmentDto) throws Exception;
    public void deleteRoom(String roomId);
    public void recvMsg(ChatNoReadVO chatNoReadVO);
    public MapVO loadLocationByRoomId(String roomId);
    public List<ChatVO> loadMsgByRoomId(int roomId);
    public void saveLocation(MapVO mapVO);
    public RoomDto createRoom(RoomDto roomVO);
    public List<RoomDto> getRoomListByUserId(int userId);
    void recvHereMsg(ChatNoReadVO chatNoReadVO);

    ChatDatasVO loadMyChatNoReadList(int userId);
}
