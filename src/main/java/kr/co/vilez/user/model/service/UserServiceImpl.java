package kr.co.vilez.user.model.service;

import kr.co.vilez.data.HttpVO;
import kr.co.vilez.jwt.JwtProviderImpl;
import kr.co.vilez.tool.AES256;
import kr.co.vilez.tool.OSUpload;
import kr.co.vilez.user.model.dto.LocationDto;
import kr.co.vilez.user.model.dto.UserDto;
import kr.co.vilez.user.model.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final AES256 aes256;
    final UserMapper userMapper;
    final JwtProviderImpl jwtProvider;
    final ResourceLoader resourceLoader;
    final OSUpload osUpload;
    final SimpMessageSendingOperations sendingOperations;

    final String bucketName = "vilez";
    HttpVO http = null;
    List<Object> data = null;

    @Override
    public void setManner(int userId, int degree) throws Exception {
        int temperature = 0;

        if(degree == 0){
            temperature = -3;
        } else if(degree == 1){
            temperature = -1;
        } else if(degree == 2){
            temperature = 0;
        } else if(degree == 3){
            temperature = 1;
        } else{
            temperature = 3;
        }

        userMapper.setManner(userId, temperature);

    }

    @Override
    public void saveLocation(LocationDto locationDto) throws Exception {
        String userId = locationDto.getCode();
        userId = aes256.decryptAES256(userId);

        locationDto.setCode(userId);
        Map<String,Object> map = new HashMap<>();
        map.put("flag","success");
        sendingOperations.convertAndSend("/sendloc/"+userId,map);

        userMapper.saveLocation(locationDto);
    }

    @Override
    public UserDto checkEmail(String email) throws SQLException {
        return userMapper.checkEmail(email);
    }

    @Override
    public HttpVO check(String nickname) throws Exception {
        http = new HttpVO();
        data = new ArrayList<>();

        UserDto user = userMapper.check(nickname);
        if(user == null){
            http.setFlag("success");
            data.add(user);
        }

        return http;
    }

    @Override
    public HttpVO modifyUserInfo(HashMap<String,?> userDto) throws Exception {
        http = new HttpVO();
        http.setFlag("success");
        userMapper.modifyUserInfo(userDto);

        return http;
    }

    @Override
    public HttpVO modifyProfile(int userId, MultipartFile multipartFile) throws Exception {
        http = new HttpVO();
        List<Object> data = new ArrayList<>();

        UserDto userDto = new UserDto();
        userDto.setId(userId);

        if(multipartFile==null || multipartFile.isEmpty()){
            String basicPath = "https://kr.object.ncloudstorage.com/vilez/basicProfile.png";
            userDto.setProfileImg(basicPath);
            userMapper.modifyProfile(userDto);

            http.setFlag("success");
            data.add(basicPath);
            http.setData(data);

            return http;
        }

        String [] formats = {".jpeg", ".png", ".bmp", ".jpg"};
        // 원래 파일 이름 추출
        String origName = multipartFile.getOriginalFilename();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));

        String folderName = "profile";
        for(int i = 0; i < formats.length; i++) {
            if (extension.equals(formats[i])){
                // user email과 확장자 결합
                String savedName = userId + extension;

                File uploadFile = osUpload.convert(multipartFile)        // 파일 생성
                        .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));

//                osUpload.mkdir(bucketName, folderName);

                String fileName = folderName + "/" + System.nanoTime() + extension;
                osUpload.put(bucketName, fileName, uploadFile);

                String path = "https://kr.object.ncloudstorage.com/"+bucketName+"/"+fileName;
                userDto.setProfileImg(path);
                userMapper.modifyProfile(userDto);

                http.setFlag("success");
                data.add(path);
                http.setData(data);
                break;
            }
        }

        return http;
    }

    @Override
    public HttpVO join(UserDto userDto) throws Exception {
        userMapper.join(userDto);
        http = new HttpVO();
        http.setFlag("success");

        return http;
    }

    @Override
    public HttpVO refreshCheck(String token) throws Exception {
        http = new HttpVO();
        List<Object> data = new ArrayList<>();

        String userId = jwtProvider.getUserId(token);
        String nickname = jwtProvider.getUserNickname(token);
        
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("token", token);

        UserDto user = userMapper.refreshCheck(map);
            
        if(user != null){
            http.setFlag("success");
            user.setAccessToken(jwtProvider.createToken(userId, nickname));
            data.add(user);
            http.setData(data);
        }
        return http;
    }

    @Override
    public HttpVO login(UserDto userDto) throws Exception {
        UserDto user = userMapper.login(userDto);
        http = new HttpVO();
        data = new ArrayList<>();

        String accessToken = null;
        String refreshToken = null;
        if(user != null){
            accessToken = jwtProvider.createToken(user.getEmail(), user.getNickName());
            refreshToken = jwtProvider.createRefreshToken(user.getEmail(), user.getNickName());

            // 로그인에 성공했다면, refresh 값을 db에 저장
            HashMap<String, String> map = new HashMap<>();
            map.put("userEmail", user.getEmail());
            map.put("token", refreshToken);
            userMapper.saveToken(map);

            http.setFlag("success");
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            data.add(user);

            http.setData(data);
            userMapper.saveToken(map);
        }

        return http;
    }

    @Override
    public HttpVO detail(int id) throws Exception {
        http = new HttpVO();
        http.setFlag("success");
        List<HashMap> data = new ArrayList<>();
        UserDto user = userMapper.detail(id);

        HashMap<String, Object> map = new HashMap<>();
        map.put("nickName",user.getNickName());
        map.put("area",user.getArea());
        map.put("manner",user.getManner());
        map.put("point",user.getPoint());
        map.put("profile_img", user.getProfileImg());
        map.put("areaLng", user.getAreaLng());
        map.put("areaLat", user.getAreaLat());

        data.add(map);
        http.setData(data);
        return http;
    }

    public UserDto detail2(int id) throws Exception {
        UserDto user = userMapper.detail(id);

        return user;
    }

    @Override
    public HttpVO list() throws Exception{
        http = new HttpVO();
        http.setFlag("success");
        List<UserDto> data = userMapper.list();
        http.setData(data);
        return http;
    }
}
