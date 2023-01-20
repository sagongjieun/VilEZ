package kr.co.vilez.appointment.model.service;

import kr.co.vilez.appointment.model.dao.AppointmentDao;
import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.mapper.AppointmentMapper;
import kr.co.vilez.appointment.model.vo.ChatNoReadVO;
import kr.co.vilez.appointment.model.vo.ChatVO;
import kr.co.vilez.appointment.model.vo.MapVO;
import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.vo.*;
import kr.co.vilez.tool.SHA256;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@AllArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDao appointmentDao;

    private final AppointmentMapper appointmentMapper;

    @Override
    public List<AppointmentDto> getAppointmentList(int boardId) throws Exception {
        return appointmentMapper.getAppointmentList(boardId);
    }

    @Override
    public void create(AppointmentDto appointmentDto) throws Exception {
        appointmentMapper.create(appointmentDto);
    }

    ////////////////////////////////////////// chat ///////////////////////////////////////////



    @Override
    public void deleteRoom(String roomId) {

    }

    @Override
    public void saveNoReadMsg(ChatNoReadVO chatNoReadVO) {
        appointmentDao.saveNoReadMsg(chatNoReadVO);
    }

    @Override
    public MapVO loadLocationByRoomId(String roomId) {
        return appointmentDao.loadLocationByRoomId(roomId);
    }

    @Override
    public List<ChatVO> loadMsgByRoomId(String roomId) { return appointmentDao.loadMsgByRoomId(roomId); }

    @Override
    public void saveLocation(MapVO mapVO) { appointmentDao.saveLocation(mapVO); }


    @Override
    public RoomDto createRoom(RoomDto room) {
       //return appointmentDao.createRoom(room);
        return null;
    }

    @Override
    public void recvHereMsg(ChatVO chatVO) {
        appointmentDao.recvHereMsg(chatVO);
    }

    @Override
    public ChatDatasVO loadMyChatNoReadList(int userId) {
       // ChatDatasVO chatNoReadVO = appointmentDao.loadMyChatNoReadList(userId);
        return null;
    }
}
