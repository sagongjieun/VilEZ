package kr.co.vilez.appointment.model.mapper;

import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.dto.RoomDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface AppointmentMapper {
    List<AppointmentDto> getAppointmentList(int boardId) throws SQLException;
    void create(AppointmentDto appointmentDto) throws SQLException;





    List<RoomDto> getRoomListByUserId(int userId);

    RoomDto createRoom(RoomDto room);
}
