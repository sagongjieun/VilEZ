package kr.co.vilez.appointment.model.service;

import kr.co.vilez.appointment.model.dao.AppointmentDao;
import kr.co.vilez.appointment.model.vo.MapVO;
import kr.co.vilez.appointment.model.vo.RoomVO;
import kr.co.vilez.appointment.model.vo.SocketVO;
import kr.co.vilez.tool.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentDao appointmentDao;


    @Override
    public void deleteRoom(String roomId) {

    }

    @Override
    public MapVO loadLocationByRoomId(String roomId) {
        return appointmentDao.loadLocationByRoomId(roomId);
    }

    @Override
    public List<SocketVO> loadMsgByRoomId(String roomId) {
        List<SocketVO> msgs = appointmentDao.loadMsgByRoomId(roomId);
        return msgs;
    }

    @Override
    public void saveLocation(MapVO mapVO) {
        appointmentDao.saveLocation(mapVO);
    }

    @Override
    public String createRoom(RoomVO roomVO) {
        appointmentDao.createRoom(roomVO);
        String encryption = encryptionRoomId(roomVO);
        return encryption;
    }

    @Override
    public String encryptionRoomId(RoomVO roomVO) {
        SHA256 sha256 = new SHA256();
        StringBuilder sb = new StringBuilder();
        sb.append("boardid:").append(roomVO.getBoardId())
                .append("user:").append(roomVO.getUser1() + roomVO.getUser2());

        try {
            return sha256.encrypt(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
