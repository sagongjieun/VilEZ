package kr.co.vilez.appointment.model.service;

import kr.co.vilez.appointment.model.dao.AppointmentDao;
import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.dto.MyAppointListDto;
import kr.co.vilez.appointment.model.mapper.AppointmentMapper;
import kr.co.vilez.appointment.model.vo.ChatVO;
import kr.co.vilez.appointment.model.vo.MapVO;
import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.vo.*;
import kr.co.vilez.share.model.dao.ShareDao;
import kr.co.vilez.share.model.dto.BookmarkDto;
import kr.co.vilez.share.model.dto.ImgPath;
import kr.co.vilez.user.model.dto.UserDto;
import kr.co.vilez.user.model.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentDao appointmentDao;
    private final AppointmentMapper appointmentMapper;
    private final ShareDao shareDao;

    @Override
    public BoardStateVO getBoardState(int boardId) throws Exception {
        return appointmentMapper.getBoardState(boardId);
    }

    @Override
    public List<AppointmentDto> getMyAppointmentCalendarList(int userId) throws Exception {
        return appointmentMapper.getMyAppointmentCalendarList(userId);
    }

    @Override
    public List<TotalListVO> getMyAppointmentList(int userId) throws Exception {
        List<MyAppointListDto> list = appointmentMapper.getMyAppointmentList(userId);

        List<TotalListVO> totalList = new ArrayList<>();
        for(MyAppointListDto vo : list){
            TotalListVO totalListVO = new TotalListVO();
            totalListVO.setMyAppointListVO(vo);

            List<BookmarkDto> bookmarkDtos = shareDao.selectBookmarkList(vo.getUserId());
            totalListVO.setBookmarkCnt(bookmarkDtos.size());

            List<ImgPath> imageList = shareDao.list(vo.getUserId());
            totalListVO.setImgPathList(imageList);
            totalList.add(totalListVO);
        }

        return totalList;

    }
    @Override
    public List<AppointmentDto> getAppointmentList(int boardId) throws Exception {
        return appointmentMapper.getAppointmentList(boardId);
    }

    @Override
    public void create(AppointmentDto appointmentDto) throws Exception {
        appointmentMapper.create(appointmentDto);
    }

    ////////////////////////////////////////// chat ///////////////////////////////////////////
    private final UserMapper userMapper;

    @Override
    public void deleteRoom(String roomId) {

    }

    @Override
    public void recvMsg(ChatVO chatVO) {
        long now = System.currentTimeMillis();
        chatVO.setTime(now);
        appointmentDao.saveMsg(chatVO);
        ChatLastVO chatLastVO = new ChatLastVO(chatVO.getRoomId(),chatVO.getToUserId(),
                chatVO.getFromUserId(), chatVO.getContent(), now);
        appointmentDao.saveLastMsg(chatLastVO);
        setEnterTimeMsg(chatVO.getRoomId(),chatVO.getFromUserId());
    }

    @Override
    public MapVO loadLocationByRoomId(int roomId) {
        return appointmentDao.loadLocationByRoomId(roomId);
    }

    @Override
    public List<ChatVO> loadMsgByRoomId(int roomId) { return appointmentDao.loadMsgByRoomId(roomId); }

    @Override
    public void saveLocation(MapVO mapVO) { appointmentDao.saveLocation(mapVO); }


    @Override
    public RoomDto createRoom(RoomDto room) {
        appointmentMapper.createRoom(room);
        long now = System.currentTimeMillis();
        UserEnterVO userEnterVO = new UserEnterVO(room.getId(), room.getShareUserId(),now);
        appointmentDao.addUserEnterMsg(userEnterVO);
        userEnterVO = new UserEnterVO(room.getId(), room.getNotShareUserId(),now);
        appointmentDao.addUserEnterMsg(userEnterVO);
        ChatLastVO chatLastVO = new ChatLastVO(room.getId(), room.getNotShareUserId(), room.getShareUserId(),"",System.currentTimeMillis());
        appointmentDao.saveLastMsg(chatLastVO);
        //방을 만들때 입장도 같이하게 한다.
        return room;
    }

    @Override
    public List<RoomDto> getRoomListByUserId(int userId) {
        return appointmentMapper.getRoomListByUserId(userId);
    }


    @Override
    public void setEnterTimeMsg(int roomId, int userId) {
        appointmentDao.deleteUserEnterMsg(roomId, userId);
        UserEnterVO userEnterVO = new UserEnterVO(roomId,userId,System.currentTimeMillis());
        appointmentDao.addUserEnterMsg(userEnterVO);
    }

    @Override
    public ChatDatasVO loadMyChatNoReadList(int userId) {
        ChatDatasVO chatNoReadVO = appointmentDao.first(userId);
        return chatNoReadVO;
    }

    @Override
    public List<ChatDatasVO> loadMyChatList(int userId) {
        List<ChatDatasVO> chatDatasVOList = new ArrayList<>();
        List<UserEnterVO> userEnterVOList = appointmentDao.getUserEnterVOList(userId);
        HashMap<Integer, Long> userEnterMap  = new HashMap<>();
        for(UserEnterVO data : userEnterVOList) {
            userEnterMap.put(data.getRoomId(), data.getTime());
        }
        List<ChatLastVO> lastVOS = appointmentDao.getChatLastVOList(userId);
        Collections.sort(lastVOS);
        for(ChatLastVO vo : lastVOS) {
            System.out.println(vo);
            ChatDatasVO chatDatasVO = new ChatDatasVO();
            if(vo.getTime() > userEnterMap.get(vo.getRoomId())) {
                long count = appointmentDao.getChatCount(vo.getRoomId(),userEnterMap.get(vo.getRoomId()));
                chatDatasVO.setNoReadCount((int) count);
            }
            UserDto user = null;
            if(vo.getToUserId() == userId) {
                try {
                    user =  userMapper.detail(vo.getFromUserId());

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    user =  userMapper.detail(vo.getToUserId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            chatDatasVO.setArea(user.getArea());
            chatDatasVO.setNickName(user.getNickName());
            chatDatasVO.setChatData(vo);
            chatDatasVOList.add(chatDatasVO);

        }
        return chatDatasVOList;
    }

    @Override
    public RoomDto getBoard(int roomId) {
        return appointmentMapper.getBoard(roomId);
    }

}
