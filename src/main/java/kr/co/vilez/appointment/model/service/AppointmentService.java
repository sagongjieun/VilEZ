package kr.co.vilez.appointment.model.service;

import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.vo.ChatNoReadVO;
import kr.co.vilez.appointment.model.vo.ChatVO;
import kr.co.vilez.appointment.model.vo.MapVO;
import kr.co.vilez.appointment.model.vo.RoomVO;

import java.util.List;

public interface AppointmentService {
    public List<AppointmentDto> getAppointmentList(AppointmentDto appointmentDto) throws Exception;
    public void create(AppointmentDto appointmentDto) throws Exception;
    public void deleteRoom(String roomId);
    public void saveNoReadMsg(ChatNoReadVO chatNoReadVO);
    public MapVO loadLocationByRoomId(String roomId);
    public List<ChatVO> loadMsgByRoomId(String roomId);
    public void saveLocation(MapVO mapVO);
    public String createRoom(RoomVO roomVO);
    public String encryptionRoomId(RoomVO roomVO);

    void recvHereMsg(ChatVO chatVO);
}
