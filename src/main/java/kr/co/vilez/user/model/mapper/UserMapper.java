package kr.co.vilez.user.model.mapper;

import kr.co.vilez.user.model.dto.UserDto;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserMapper {
    UserDto check(String nickname) throws SQLException;
    void modifyUserInfo(UserDto user) throws SQLException;
    void modifyProfile(UserDto user) throws SQLException;
    void join(UserDto user) throws SQLException;
    UserDto login(UserDto user) throws SQLException;
    UserDto refreshCheck(HashMap<String, String> map) throws SQLException;
    void saveToken(HashMap<String, String> map) throws SQLException;

    UserDto detail(int id) throws SQLException;
    //DEBUG
    List<UserDto> list() throws SQLException;

}

