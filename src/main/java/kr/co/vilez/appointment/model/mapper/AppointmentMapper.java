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
    public BoardStateVO getBoardState(int boardId) throws SQLException;
    public List<MyAppointListDto> getMyAppointmentList(int userId) throws SQLException;
    public List<AppointmentDto> getAppointmentList(int boardId) throws SQLException;
    public void create(AppointmentDto appointmentDto) throws SQLException;
    List<RoomDto> getRoomListByUserId(int userId);
    void createRoom(RoomDto room);
}
