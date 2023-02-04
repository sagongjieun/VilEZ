package kr.co.vilez.back.model.mapper;

import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.back.model.dto.AppointmentStateDto;
import kr.co.vilez.back.model.vo.AppointmentVO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface BackMapper {
    AppointmentVO getAppointmentId(int roomId) throws SQLException;
    void setAppointment(int appointmentId) throws SQLException;
    List<Integer> getAppointmentState(int roomId) throws SQLException;
}
