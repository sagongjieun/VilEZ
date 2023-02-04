package kr.co.vilez.back.model.mapper;

import kr.co.vilez.back.model.dto.AppointmentStateDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface BackMapper {
    List<Integer> getAppointmentState(int roomId) throws SQLException;
}
