package kr.co.vilez.appointment.model.mapper;

import kr.co.vilez.appointment.model.dto.AppointmentDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface AppointmentMapper {
    public List<AppointmentDto> getAppointmentList(AppointmentDto appointmentDto) throws SQLException;
    public void create(AppointmentDto appointmentDto) throws SQLException;
}
