package kr.co.vilez.appointment.model.service;

import kr.co.vilez.appointment.model.dao.AppointmentDao;
import kr.co.vilez.appointment.model.dto.*;
import kr.co.vilez.appointment.model.mapper.AppointmentMapper;
import kr.co.vilez.appointment.model.vo.ChatVO;
import kr.co.vilez.appointment.model.vo.MapVO;
import kr.co.vilez.appointment.model.vo.*;
import kr.co.vilez.ask.model.dao.AskDao;
import kr.co.vilez.ask.model.dto.ImgPath2;
import kr.co.vilez.back.model.dao.BackDao;
import kr.co.vilez.back.model.vo.RoomStatusVO;
import kr.co.vilez.share.model.dao.ShareDao;
import kr.co.vilez.share.model.dto.BookmarkDto;
import kr.co.vilez.share.model.dto.ImgPath;
import kr.co.vilez.user.model.dto.UserDto;
import kr.co.vilez.user.model.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
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
    private final BackDao backDao;
    private final AskDao askDao;

    @Override
    public Object getMyAppointmentDate(int  userId) throws Exception {
        List<TotalVO> totalList = new ArrayList<>();

        List<AppointmentDto> list = appointmentMapper.getMyAppointmentDateShare(userId);
        for(AppointmentDto appointmentDto : list){
            TotalVO totalVO = new TotalVO(appointmentDto,
                    shareDao.list(appointmentDto.getBoardId()),
                    shareDao.selectBookmarkList(appointmentDto.getBoardId()).size());
            totalList.add(totalVO);
        }

        list = appointmentMapper.getMyAppointmentDateAsk(userId);
        for(AppointmentDto appointmentDto : list){
            TotalVO totalVO = new TotalVO(appointmentDto, shareDao.list(appointmentDto.getBoardId()), 0);
            totalList.add(totalVO);
        }

        return totalList;
    }

    @Override
    public AppointmentDto getAppointmentDate(int boardId, int shareUserId, int notShareUserId, int type) throws Exception {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setBoardId(boardId);
        appointmentDto.setShareUserId(shareUserId);
        appointmentDto.setNotShareUserId(notShareUserId);
        appointmentDto.setType(type);

        return appointmentMapper.getAppointmentDate(appointmentDto);
    }

    @Override
    public CancelAppointmentDto checkRequest(int roomId) throws Exception {
        return appointmentDao.loadRequestCheck(roomId);
    }

    @Override
    public void saveRequest(CancelAppointmentDto cancelAppointmentDto) throws Exception {
        appointmentDao.saveRequest(cancelAppointmentDto);
    }

    @Override
    public void cancelAppointment(int roomId, int reason) throws Exception {
        // 해당하는 roomId의 확인 요청 데이터를 삭제한다.
        appointmentDao.deleteRequest(roomId);

        // 예약 취소
        // 룸상태 삭제
        backDao.deleteRoomStatus(roomId);

        // 현재 채팅방 상태를 설정한다.
        // 예약취소 -2
        backDao.insertRoomStatus(new RoomStatusVO(roomId, -2));

        // roomId 를 통해 appointment 내역을 가져온다
        RoomDto roomDto = appointmentMapper.getBoard(roomId);

        // 예약내역을 -2로 바꾼다.
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setBoardId(roomDto.getBoardId());
        appointmentDto.setShareUserId(roomDto.getShareUserId());
        appointmentDto.setNotShareUserId(roomDto.getNotShareUserId());
        appointmentDto.setType(roomDto.getType());
        appointmentMapper.cancelAppointment(appointmentDto);

        // 공유자의 포인트를 삭감한다.
        PointVO pointVO = new PointVO();
        pointVO.setBoardId(appointmentDto.getBoardId());
        pointVO.setUserId(appointmentDto.getShareUserId());
        pointVO.setPoint(-30);
        pointVO.setReason(1);

        LocalDate now = LocalDate.now();
        System.out.println("***** now = " + now);
        pointVO.setDate(now.toString());
        pointVO.setType(appointmentDto.getType());
        System.out.println("***** pointVO = " + pointVO);

        // 내역 저장
        appointmentDao.savePoint(pointVO);
        // 피공유자 포인트 변동
        appointmentMapper.changePoint(pointVO);

        // reason이 1번인 경우 공유자의 의해 예약이 취소되는 경우로 공유자 포인트 삭감
        // 피공유자 포인트 되돌리기를 진행한다.
        if(reason == 1){
            pointVO.setUserId(appointmentDto.getNotShareUserId());
            pointVO.setPoint(30);
            // 내역 저장
            appointmentDao.savePoint(pointVO);
            // 피공유자 포인트 변동
            appointmentMapper.changePoint(pointVO);
        }

    }

    @Override
    public void updatePeriod(SetPeriodDto setPeriodDto) throws Exception {
        appointmentDao.deleteCheck(setPeriodDto);
        appointmentDao.setPeriod(setPeriodDto);
    }

    @Override
    public void deleteCheck(AppointmentDto appointmentDto) throws Exception {
        appointmentDao.deleteCheck(appointmentDto);
    }

    @Override
    public SetPeriodDto check(int boardId, int shareUserId, int notShareUserId, int type) throws Exception {
        return appointmentDao.loadCheck(boardId, shareUserId, notShareUserId, type);
    }

    @Override
    public void setPeriod(SetPeriodDto setPeriodDto) throws Exception {
        appointmentDao.setPeriod(setPeriodDto);
    }

    ///////////////////////////////포인트 관련 내용 ///////////////////////////////
    @Override
    public List<PointListVO> getPointList(int userId) throws Exception {
        List<PointVO> pointVOList =  appointmentDao.getPointList(userId);

        List<PointListVO> list = new ArrayList<>();
        for(PointVO pointVO : pointVOList) {
            BoardInfoVO boardInfoVO = new BoardInfoVO();
            if(pointVO.getBoardId() == -1) {
                boardInfoVO.setTitle("기본 포인트");
            } else {
                if(pointVO.getType() == 2)
                    boardInfoVO = appointmentMapper.getBoardInfoShare(pointVO);
                else
                    boardInfoVO = appointmentMapper.getBoardInfoAsk(pointVO);
            }
            list.add(new PointListVO(pointVO, boardInfoVO));
        }

        return list;
    }

    @Override
    public void addPoint(AppointmentDto appointmentDto) throws Exception {
        // 공유자 포인트 내역을 저장하고 포인트를 바꾸는 로직
        PointVO pointVO = new PointVO();
        pointVO.setBoardId(appointmentDto.getBoardId());
        pointVO.setUserId(appointmentDto.getShareUserId());
        pointVO.setPoint(30);
        pointVO.setType(appointmentDto.getType());

        LocalDate now = LocalDate.now();
        pointVO.setDate(now.toString());
        // 내역 저장
        appointmentDao.savePoint(pointVO);

        // 공유자 포인트 변동
        appointmentMapper.changePoint(pointVO);

        // 공유자 포인트 내역을 저장하고 포인트를 바꾸는 로직
        pointVO.setUserId(appointmentDto.getNotShareUserId());
        pointVO.setPoint(-30);
        // 내역 저장
        appointmentDao.savePoint(pointVO);
        // 피공유자 포인트 변동
        appointmentMapper.changePoint(pointVO);
    }

    ///////////////////////////////예약 관련 내용////////////////////////////////
    @Override
    public List<TotalListVO> getGiveList(int userId) throws Exception {
        List<MyAppointListDto> listAsk = appointmentMapper.getGiveListAsk(userId);
        for(MyAppointListDto myAppointListDto : listAsk){
            myAppointListDto.setType(1);
        }
        List<MyAppointListDto> list = appointmentMapper.getGiveListShare(userId);
        for(MyAppointListDto myAppointListDto : list){
            myAppointListDto.setType(2);
        }


        list.addAll(listAsk);

        List<TotalListVO> totalList = new ArrayList<>();
        for(MyAppointListDto vo : list){
            TotalListVO totalListVO = new TotalListVO();
            totalListVO.setMyAppointListVO(vo);

            List<ImgPath> imageList;
            List<ImgPath2> imageList2;

            if(vo.getType() == 1){
                imageList2 = askDao.list(vo.getId());
                totalListVO.setImgPathList(imageList2);
            }
            else{
                imageList = shareDao.list(vo.getId());
                totalListVO.setImgPathList(imageList);
                List<BookmarkDto> bookmarkDtos = shareDao.selectBookmarkList(vo.getUserId());
                totalListVO.setBookmarkCnt(bookmarkDtos.size());
            }
            totalList.add(totalListVO);
        }

        return totalList;
    }

    @Override
    public BoardStateVO getBoardState(int boardId, int type) throws Exception {
        return appointmentMapper.getBoardState(boardId, type);
    }

    @Override
    public List<AppointmentDto> getMyAppointmentCalendarList(int userId) throws Exception {
            List<AppointmentDto> listAsk = appointmentMapper.getMyAppointmentCalendarListAsk(userId);
            for(AppointmentDto appointmentDto : listAsk){
                appointmentDto.setType(1);
            }

            List<AppointmentDto> list = appointmentMapper.getMyAppointmentCalendarListShare(userId);
            for(AppointmentDto appointmentDto : list){
                appointmentDto.setType(2);
            }


            list.addAll(listAsk);

        return list;
    }

    @Override
    public List<TotalListVO> getMyAppointmentList(int userId) throws Exception {
        List<MyAppointListDto> listAsk = appointmentMapper.getMyAppointmentListAsk(userId);
        System.out.println("listAsk = " + listAsk);
        for(MyAppointListDto myAppointListDto : listAsk){
            myAppointListDto.setType(1);
        }
        
        List<MyAppointListDto> list = appointmentMapper.getMyAppointmentListShare(userId);
        for(MyAppointListDto myAppointListDto : list){
            myAppointListDto.setType(2);
        }
        System.out.println("list = " + list);

        list.addAll(listAsk);

        List<TotalListVO> totalList = new ArrayList<>();
        for(MyAppointListDto vo : list){
            TotalListVO totalListVO = new TotalListVO();
            totalListVO.setMyAppointListVO(vo);


            List<ImgPath> imageList;
            List<ImgPath2> imageList2;

            if(vo.getType() == 1){
                imageList2 = askDao.list(vo.getId());
                totalListVO.setImgPathList(imageList2);
            }
            else{
                imageList = shareDao.list(vo.getId());
                totalListVO.setImgPathList(imageList);
                List<BookmarkDto> bookmarkDtos = shareDao.selectBookmarkList(vo.getUserId());
                totalListVO.setBookmarkCnt(bookmarkDtos.size());
            }


            totalList.add(totalListVO);
        }

        return totalList;

    }
    @Override
    public List<AppointmentDto> getAppointmentList(int boardId, int type) throws Exception {
        return appointmentMapper.getAppointmentList(boardId, type);
    }

    @Override
    public void create(AppointmentDto appointmentDto) throws Exception {
        appointmentMapper.create(appointmentDto);

        // 현재 채팅방 상태를 설정한다.
        // 예약중 0
        backDao.insertRoomStatus(new RoomStatusVO(appointmentDto.getRoomId(), 0));
    }

    ////////////////////////////////////////// chat ///////////////////////////////////////////
    private final UserMapper userMapper;

    @Override
    public void deleteRoom(int roomId, int userId) {
        appointmentDao.deleteRoom(roomId,userId);
        appointmentMapper.deleteRoom(roomId);
    }

    @Override
    public void deleteRoom(int roomId) throws Exception {
        appointmentMapper.deleteRoom(roomId);
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
        RoomDto roomDto = appointmentMapper.checkRoom2(room.getBoardId(),room.getType(),room.getShareUserId(),room.getNotShareUserId());
        if(roomDto != null) {
            return roomDto;
        }
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
            if (!userEnterMap.containsKey(vo.getRoomId())) {
                continue;
            }
            ChatDatasVO chatDatasVO = new ChatDatasVO();
            if(vo.getTime() > userEnterMap.get(vo.getRoomId())) {
                long count = appointmentDao.getChatCount(vo.getRoomId(),userEnterMap.get(vo.getRoomId()));
                chatDatasVO.setNoReadCount((int) count);
            }
            UserDto user = null;
            RoomDto room = appointmentMapper.getBoard(vo.getRoomId());
            if(room.getShareUserId() == userId) {
                try {
                    user =  userMapper.detail(room.getNotShareUserId());

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    user =  userMapper.detail(room.getShareUserId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            chatDatasVO.setNickName(user.getNickName());
            chatDatasVO.setProfile(user.getProfileImg());
            chatDatasVO.setChatData(vo);
            chatDatasVOList.add(chatDatasVO);

        }
        return chatDatasVOList;
    }

    @Override
    public RoomDto getBoard(int roomId) {
        return appointmentMapper.getBoard(roomId);
    }

    @Override
    public RoomDto checkRoom(int userId, int boardId, int type) {
        return appointmentMapper.checkRoom(userId,boardId,type);
    }

    @Override
    public void recvEnd(int roomId) {

    }

    @Override
    public List<RoomDto> getRoomListByBoardId(int boardId, int type) {
        return appointmentMapper.getRoomListByBoardId(boardId, type);
    }

}
