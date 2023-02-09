package kr.co.vilez.appointment.model.mapper;

import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.dto.MyAppointListDto;
import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.vo.BoardInfoVO;
import kr.co.vilez.appointment.model.vo.BoardStateVO;
import kr.co.vilez.appointment.model.vo.PointVO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface AppointmentMapper {
    List<AppointmentDto> getMyAppointmentDateShare(int userId) throws SQLException;
    List<AppointmentDto> getMyAppointmentDateAsk(int userId) throws SQLException;

    BoardInfoVO getBoardInfoAsk(PointVO pointVO) throws SQLException;
    BoardInfoVO getBoardInfoShare(PointVO pointVO) throws SQLException;
    AppointmentDto getAppointmentDate(AppointmentDto appointmentDto) throws SQLException;
    void cancelAppointment(AppointmentDto appointmentDto) throws SQLException;
    void changePoint(PointVO pointVO) throws SQLException;

    //////////////////////////////////////////////////////////
    List<MyAppointListDto> getGiveListShare(int userId) throws SQLException;
    List<MyAppointListDto> getGiveListAsk(int userId) throws SQLException;
    List<AppointmentDto> getMyAppointmentCalendarListShare(int userId) throws SQLException;
    List<AppointmentDto> getMyAppointmentCalendarListAsk(int userId) throws SQLException;
    public BoardStateVO getBoardState(int boardId, int type) throws SQLException;
    public List<MyAppointListDto> getMyAppointmentListShare(int userId) throws SQLException;
    public List<MyAppointListDto> getMyAppointmentListAsk(int userId) throws SQLException;
    public List<AppointmentDto> getAppointmentList(int boardId, int type) throws SQLException;
    public void create(AppointmentDto appointmentDto) throws SQLException;
    List<RoomDto> getRoomListByUserId(int userId);
    void createRoom(RoomDto room);
    void deleteRoom(int roomId);

    RoomDto getBoard(int roomId);

    RoomDto checkRoom(int userId, int boardId, int type);

    RoomDto checkRoom2(int boardId, int type, int shareUserId, int notShareUserId);

    List<RoomDto> getRoomListByBoardId(int boardId,int type);
}
