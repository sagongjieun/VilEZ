package kr.co.vilez.user.model.service;

import kr.co.vilez.data.HttpVO;
import kr.co.vilez.jwt.JwtProviderImpl;
import kr.co.vilez.user.model.dto.UserDto;
import kr.co.vilez.user.model.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    JwtProviderImpl jwtProvider;

    @Autowired
    ResourceLoader resourceLoader;

    HttpVO http = null;
    List<Object> data = null;
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
    public HttpVO modifyUserInfo(UserDto userDto) throws Exception {
        http = new HttpVO();
        http.setFlag("success");
        userMapper.modifyUserInfo(userDto);

        return http;
    }

    @Override
    public HttpVO modifyProfile(String email, MultipartFile multipartFile) throws Exception {
        http = new HttpVO();
        List<Object> data = new ArrayList<>();

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        String [] formats = {".jpeg", ".png", ".bmp", ".jpg"};
        // 원래 파일 이름 추출
        String origName = multipartFile.getOriginalFilename();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));
        StringTokenizer st = new StringTokenizer(userDto.getEmail(), "@");
        String userId = st.nextToken();

        for(int i = 0; i < formats.length; i++) {
            if (extension.equals(formats[i])){
                // user email과 확장자 결합
                String savedName = userId + extension;
                // 파일을 불러올 때 사용할 파일 경로
                Resource resource = resourceLoader.getResource("classpath:/static/");
                String savedPath = resource.getURI().getPath() + "/images/profiles/"+ userId + "/" +savedName;

                userDto.setProfileImg(savedPath);
                userMapper.modifyProfile(userDto);

                multipartFile.transferTo(new File(savedPath));
                http.setFlag("success");
                data.add(savedPath);
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

        data.add(map);
        http.setData(data);
        return http;
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
