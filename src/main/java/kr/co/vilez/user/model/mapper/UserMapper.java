package kr.co.vilez.user.model.mapper;

import kr.co.vilez.user.model.dto.LocationDto;
import kr.co.vilez.user.model.dto.UserDto;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserMapper {
    void setManner(int userId, int degree) throws Exception;
    void saveLocation(LocationDto locationDto) throws Exception;
    void saveLocationMobile(UserDto userDto) throws Exception;
    UserDto checkEmail(String email) throws SQLException;
    UserDto check(String nickname) throws SQLException;
    void modifyUserInfo(HashMap<String,?> user) throws SQLException;
    void modifyProfile(UserDto user) throws SQLException;
    void join(UserDto user) throws SQLException;
    UserDto login(UserDto user) throws SQLException;
    UserDto refreshCheck(HashMap<String, String> map) throws SQLException;
    void saveToken(HashMap<String, Object> map) throws SQLException;

    UserDto detail(int id) throws SQLException;
    //DEBUG
    List<UserDto> list() throws SQLException;

    void setPoint(int userId, int point);
}

