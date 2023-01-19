package kr.co.vilez.email.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;

@Mapper
public interface EmailMapper {
    String userEmailCheck(String email) throws SQLException;
}
