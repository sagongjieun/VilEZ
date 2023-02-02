package kr.co.vilez.appointment.model.service;

import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.dto.SetPeriodDto;
import kr.co.vilez.appointment.model.vo.*;
import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.vo.ChatVO;
import kr.co.vilez.appointment.model.vo.MapVO;

import java.util.List;

public interface AppointmentService {
    void deletePeriod(SetPeriodDto setPeriodDto) throws Exception;
    void deleteCheck(AppointmentDto appointmentDto) throws Exception;
    boolean check(int boardId, int shareUserId, int notShareUserId, int type) throws Exception;
    void setPeriod(SetPeriodDto setPeriodDto) throws Exception;


    ////////////////////////포인트 관련 내용 //////////////////////////////////


    List<PointVO> getPointList(int userId) throws Exception;
    void addPoint(AppointmentDto appointmentDto) throws Exception;

    //////////////////////// 예약관련 ////////////////////////////////////////
    List<TotalListVO> getGiveList(int userId) throws Exception;
    List<AppointmentDto> getMyAppointmentCalendarList(int userId) throws Exception;
    BoardStateVO getBoardState(int boardId) throws Exception;
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

    RoomDto getBoard(int roomId);

    RoomDto checkRoom(int userId, int boardId, int type);
}
