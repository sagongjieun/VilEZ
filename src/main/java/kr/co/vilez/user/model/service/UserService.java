package kr.co.vilez.user.model.service;

import kr.co.vilez.data.HttpVO;
import kr.co.vilez.user.model.dto.UserDto;
import org.apache.catalina.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

public interface UserService {

    HttpVO check(String nickname) throws  Exception;
    HttpVO modifyUserInfo(HashMap<String,?> userDto) throws Exception;
    HttpVO modifyProfile(int userId, MultipartFile multipartFile) throws Exception;
    HttpVO login(UserDto userDto) throws Exception;
    HttpVO detail(int id) throws Exception;
    HttpVO refreshCheck(String token) throws Exception;
    HttpVO join(UserDto userDto) throws Exception;
    HttpVO list() throws Exception;
    UserDto detail2(int id) throws Exception;
}
