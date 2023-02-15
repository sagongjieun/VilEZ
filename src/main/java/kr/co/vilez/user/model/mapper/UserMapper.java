package kr.co.vilez.user.model.mapper;

import kr.co.vilez.user.model.dto.LocationDto;
import kr.co.vilez.user.model.dto.UserDto;
import kr.co.vilez.user.model.vo.TokenVO;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    void setRefreshToken(UserDto refreshToken) throws SQLException;
    void logout(UserDto userDto) throws SQLException;
    void modifyPassword(String email, String password) throws SQLException;
    void setManner(int userId, int degree) throws SQLException;
    void saveLocation(LocationDto locationDto) throws SQLException;
    void saveLocationMobile(HashMap<String,Object> map) throws SQLException;
    UserDto checkEmail(String email) throws SQLException;
    UserDto check(String nickname) throws SQLException;
    void modifyUserInfo(HashMap<String,?> user) throws SQLException;
    void modifyProfile(UserDto user) throws SQLException;
    void join(UserDto user) throws SQLException;
    UserDto login(UserDto user) throws SQLException;
    UserDto loginFake(UserDto user) throws SQLException;
    UserDto refreshCheck(TokenVO vo) throws SQLException;
    void saveToken(HashMap<String, Object> map) throws SQLException;

    UserDto detail(int id) throws SQLException;
    //DEBUG
    List<UserDto> list() throws SQLException;

    void setPoint(int userId, int point);
}

