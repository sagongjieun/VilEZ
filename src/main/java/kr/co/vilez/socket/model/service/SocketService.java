package kr.co.vilez.socket.model.service;

import kr.co.vilez.socket.model.vo.RoomVO;
import kr.co.vilez.socket.model.vo.SocketVO;

import java.util.List;

public interface SocketService {
    public void saveMsg(SocketVO socketVO);
    public void deleteRoom(String roomId);

    public  SocketVO loadLocationByRoomId(String roomId);
    public List<SocketVO> loadMsgByRoomId(String roomId);
    public void dispatchType(SocketVO socketVO);
    public String createRoom(RoomVO roomVO);
    public String encryptionRoomId(RoomVO roomVO);

}
