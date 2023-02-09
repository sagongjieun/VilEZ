package kr.co.vilez.user.model.service;

import kr.co.vilez.data.HttpVO;
import kr.co.vilez.user.model.dto.LocationDto;
import kr.co.vilez.user.model.dto.UserDto;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.HashMap;

public interface UserService {
    HttpVO loginFake(UserDto userDto) throws Exception;
    void modifyPassword(String email) throws Exception;
    void saveLocationMobile(HashMap<String, Object> map) throws Exception;
    void setManner(int userId, int degree) throws Exception;
    void saveLocation(LocationDto locationDto) throws Exception;
    UserDto checkEmail(String email) throws SQLException;
    HttpVO check(String nickname) throws  Exception;
    HttpVO modifyUserInfo(HashMap<String,?> userDto) throws Exception;
    HttpVO modifyProfile(int userId, MultipartFile multipartFile) throws Exception;
    HttpVO login(UserDto userDto) throws Exception;
    HttpVO detail(int id) throws Exception;
    HttpVO refreshCheck(String token) throws Exception;
    HttpVO join(UserDto userDto) throws Exception;
    HttpVO list() throws Exception;
    UserDto detail2(int id) throws Exception;

    void setPoint(int userId, int point);

    void logout(UserDto userDto) throws Exception;
}
