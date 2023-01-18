package kr.co.vilez.socket.model.service;

import kr.co.vilez.socket.model.dao.SocketDao;
import kr.co.vilez.socket.model.vo.RoomVO;
import kr.co.vilez.socket.model.vo.SocketVO;
import kr.co.vilez.tool.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class SocketServiceImpl implements SocketService{

    @Autowired
    private SocketDao socketDao;

    @Override
    public void saveMsg(SocketVO socketVO) {
        socketDao.saveMsg(socketVO);
    }

    @Override
    public void deleteRoom(String roomId) {

    }

    @Override
    public SocketVO loadLocationByRoomId(String roomId) {
        SocketVO socketVO = socketDao.loadLocationByRoomId(roomId);
        if(socketVO == null) {
            socketVO = new SocketVO(roomId,"2",null, "37.5666805","126.9784147", "5");
        }
        return socketVO;
    }

    @Override
    public List<SocketVO> loadMsgByRoomId(String roomId) {
        List<SocketVO> msgs = socketDao.loadMsgByRoomId(roomId);
        return msgs;
    }

    @Override
    public void dispatchType(SocketVO socketVO) {
        int type = Integer.parseInt(socketVO.getType());
        switch (type) {
            case 2:
                //TODO 지도 공유 서비스
                socketDao.deleteLocation(socketVO.getRoomId());
                break;
        }
        socketDao.saveMsg(socketVO);
    }

    @Override
    public String createRoom(RoomVO roomVO) {
        socketDao.createRoom(roomVO);
        String encryption = encryptionRoomId(roomVO);
        return encryption;
    }

    @Override
    public String encryptionRoomId(RoomVO roomVO) {
        SHA256 sha256 = new SHA256();
        String encryption = "boardid:"+roomVO.getBoardId()
                +"user:"+(Integer.parseInt(roomVO.getUser1())
                + Integer.parseInt(roomVO.getUser2()));
        try {
            encryption = sha256.encrypt(encryption);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryption;
    }
}
