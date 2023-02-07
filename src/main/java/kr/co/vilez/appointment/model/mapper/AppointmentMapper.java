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
    BoardInfoVO getBoardInfoAsk(PointVO pointVO) throws Exception;
    BoardInfoVO getBoardInfoShare(PointVO pointVO) throws Exception;
    AppointmentDto getAppointmentDate(AppointmentDto appointmentDto) throws Exception;
    void cancelAppointment(AppointmentDto appointmentDto) throws Exception;
    void changePoint(PointVO pointVO) throws Exception;

    //////////////////////////////////////////////////////////
    List<MyAppointListDto> getGiveListShare(int userId) throws Exception;
    List<MyAppointListDto> getGiveListAsk(int userId) throws Exception;
    List<AppointmentDto> getMyAppointmentCalendarListShare(int userId) throws Exception;
    List<AppointmentDto> getMyAppointmentCalendarListAsk(int userId) throws Exception;
    public BoardStateVO getBoardState(int boardId) throws SQLException;
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
}
