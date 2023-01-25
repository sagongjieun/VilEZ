package kr.co.vilez.appointment.model.service;

import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.vo.*;
import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.vo.ChatVO;
import kr.co.vilez.appointment.model.vo.MapVO;

import java.util.List;

public interface AppointmentService {
    List<TotalListVO> getMyAppointmentList(int userId) throws Exception;
    List<AppointmentDto> getAppointmentList(int boardId) throws Exception;
    void create(AppointmentDto appointmentDto) throws Exception;
    void deleteRoom(String roomId);
    void recvMsg(ChatVO chatVO);
    MapVO loadLocationByRoomId(int roomId);
    List<ChatVO> loadMsgByRoomId(int roomId);
    void saveLocation(MapVO mapVO);
    RoomDto createRoom(RoomDto roomVO);
    List<RoomDto> getRoomListByUserId(int userId);
    void setEnterTimeMsg(int roomId, int userId);
    ChatDatasVO loadMyChatNoReadList(int userId);
    List<ChatDatasVO> loadMyChatList(int userId);

}
