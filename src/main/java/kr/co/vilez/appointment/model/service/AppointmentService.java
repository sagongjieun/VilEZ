package kr.co.vilez.appointment.model.service;

import kr.co.vilez.appointment.model.vo.MapVO;
import kr.co.vilez.appointment.model.vo.RoomVO;
import kr.co.vilez.appointment.model.vo.SocketVO;

import java.util.List;

public interface AppointmentService {
    public void deleteRoom(String roomId);

    public MapVO loadLocationByRoomId(String roomId);
    public List<SocketVO> loadMsgByRoomId(String roomId);
    public void saveLocation(MapVO mapVO);
    public String createRoom(RoomVO roomVO);
    public String encryptionRoomId(RoomVO roomVO);

}
