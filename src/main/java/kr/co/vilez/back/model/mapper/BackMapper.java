package kr.co.vilez.back.model.mapper;

import kr.co.vilez.back.model.dto.AppointmentStateDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;

@Mapper
public interface BackMapper {
    Integer getAppointmentState(AppointmentStateDto appointmentStateDto) throws SQLException;
}
