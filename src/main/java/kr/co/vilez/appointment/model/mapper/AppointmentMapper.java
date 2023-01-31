package kr.co.vilez.appointment.model.mapper;

import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.dto.MyAppointListDto;
import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.vo.BoardStateVO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface AppointmentMapper {
    BoardStateVO getBoardState(int boardId) throws SQLException;
    List<MyAppointListDto> getMyAppointmentList(int userId) throws SQLException;
    List<AppointmentDto> getAppointmentList(int boardId) throws SQLException;
    void create(AppointmentDto appointmentDto) throws SQLException;
    List<RoomDto> getRoomListByUserId(int userId);
    void createRoom(RoomDto room);

    RoomDto getBoard(int roomId);

    int checkRoom(int userId, int boardId, int type);
}
