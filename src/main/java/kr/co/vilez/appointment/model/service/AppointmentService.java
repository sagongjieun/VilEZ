package kr.co.vilez.appointment.model.service;

import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.vo.*;
import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.vo.ChatNoReadVO;
import kr.co.vilez.appointment.model.vo.ChatVO;
import kr.co.vilez.appointment.model.vo.MapVO;

import java.util.List;

public interface AppointmentService {
    public List<TotalListVO> getMyAppointmentList(int userId) throws Exception;
    public List<AppointmentDto> getAppointmentList(int boardId) throws Exception;
    public void create(AppointmentDto appointmentDto) throws Exception;
    public void deleteRoom(String roomId);
    public void saveNoReadMsg(ChatNoReadVO chatNoReadVO);
    public MapVO loadLocationByRoomId(String roomId);
    public List<ChatVO> loadMsgByRoomId(String roomId);
    public void saveLocation(MapVO mapVO);
    public RoomDto createRoom(RoomDto roomVO);

    void recvHereMsg(ChatVO chatVO);

    ChatDatasVO loadMyChatNoReadList(int userId);
}
