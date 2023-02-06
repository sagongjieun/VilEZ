package kr.co.vilez.user.controller;

import io.swagger.annotations.ApiOperation;
import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.service.AppointmentService;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.user.model.dto.LocationDto;
import kr.co.vilez.user.model.dto.UserDto;
import kr.co.vilez.user.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/users")
@RestController
@Slf4j
@RequiredArgsConstructor
@Log4j2
public class UserController {
    HttpVO http = null;
    final AppointmentService appointmentService;
    final UserService userService;

    @PutMapping("/modify/password")
    public ResponseEntity<?> modifyPassword(String email, String password){
        HttpVO http = new HttpVO();

        try{
            userService.modifyPassword(email);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PutMapping("/point")
    public ResponseEntity<?> addManner(@RequestBody HashMap<String,Integer> map){
        HttpVO http = new HttpVO();
        http.setFlag("success");
        try{
            userService.setPoint(map.get("userId"),map.get("point"));

        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PostMapping("/manner")
    @ApiOperation(value = "매너지수 증가 및 삭감 API"
            , notes = "0 : 불만족, 1 : 조금 불친절, 2:보통, 3:친절, 4:마음이 뜨신 사람 ")
    public ResponseEntity<?> setManner(@RequestBody HashMap<String,Integer> map){
        HttpVO http = new HttpVO();
        http.setFlag("success");
        try{
            userService.setManner(map.get("userId"),map.get("degree"));

        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PutMapping("/location")
    @ApiOperation(value = "유저의 GPS 정보를 받아 주소를 인증해주는 API"
            , notes = "dto의 모든 변수를 채워넣어 전송하면 해당 정보를 토대로 내 동네가 인증된다.")
    public ResponseEntity<?> saveLocation(@RequestBody LocationDto locationDto){
        http = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

//        System.out.println(locationDto.getCode());
        Map<String, String> map = new HashMap<String, String>();

        try{
            userService.saveLocation(locationDto);
            map.put("url", "https://i8d111.p.ssafy.io/check");
            data.add(map);
            http.setData(data);
            http.setFlag("success");

        } catch(Exception e){
            e.printStackTrace();
        }
        map.clear();
        map.put("flag","success");
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PutMapping("/locationMobile")
    @ApiOperation(value = "유저의 주소를 update한다."
            , notes = "code에 userId를 string으로 변환해서 넣기")
    public ResponseEntity<?> saveLocationMobile(@RequestBody HashMap<String, Object> map){
        http = new HttpVO();
        try{
            userService.saveLocationMobile(map);
            http.setFlag("success");
        } catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping("/check/{email}")
    @ApiOperation(value = "이메일을 이용한 사용자 회원가입 여부 확인 API"
    , notes = "email을 보내면 해당 email로 회원가입한 정보가 있으면 해당 email이 리턴 아니면 null이 리턴된다.")
    public ResponseEntity<?> checkEmail(@PathVariable String email){
        HttpVO httpVO = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

        try{
            UserDto userDto = userService.checkEmail(email);
            if(userDto == null) {
                data.add(false);
            } else{
                data.add(true);
            }
            httpVO.setData(data);
            httpVO.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    // access 토큰 갱신
    @PostMapping("/refresh")
    @ApiOperation(value = "access토큰을 갱신한다.", notes = "{\n refresh_token : String \n}")
    public ResponseEntity<?> refresh(@RequestBody String refresh_token){
        try {
            http = userService.refreshCheck(refresh_token);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }


    // 닉네임 중복확인
    @GetMapping("/check")
    @ApiOperation(value = "닉네임 중복 확인.", notes = "nickname으로 확인")
    public ResponseEntity<?> check(@RequestParam String nickname){
        try {
            http = userService.check(nickname);
            log.info("닉네임 중복 확인 성공 : ");
        } catch (Exception e){
            e.printStackTrace();
            log.error("닉네임 중복 확인 실패");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // 회원 정보 변경(Text)
    @PutMapping("/modify")
    @ApiOperation(value = "유저 정보 수정한다.", notes = "{\n id : Number," +
            "\n nickName : String," +
            "\n password : String" +
            "\n }")
    public ResponseEntity<?> modify(@RequestBody HashMap<String,?> user){
        try {
            System.out.println(user);
            http = userService.modifyUserInfo(user);
            log.info("회원정보 수정 성공 : " + user.get("id"));
        } catch (Exception e){
            e.printStackTrace();
            log.error("회원정보 수정 실패");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // 회원 정보 변경(Image)
    @PutMapping("/profile")
    @ApiOperation(value = "유저 프로필 정보 수정한다.", notes = "{\n\t id : userId(int)" +
            "\n\t 프로필 사진에 아무것도 넣지 않으면 기본 이미지로 설정된다.")
    public ResponseEntity<?> modifyImg(@RequestPart(value = "userId") int userId,
            @RequestPart(value = "image",required = false) MultipartFile file) {
        try {
            http = userService.modifyProfile(userId, file);
            log.info("회원프로필 수정 성공");
        } catch (Exception e){
            e.printStackTrace();
            log.error("회원프로필 수정 실패");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // 회원가입
    @PostMapping("/join")
    @ApiOperation(value = "유저 정보 수정한다.", notes = "{" +
            "\n email : String," +
            "\n password : String," +
            "\n nickName : String" +
            "\n}")
    public ResponseEntity<?> join(@RequestBody UserDto user){
        try {
            http = userService.join(user);
            log.info("회원가입 성공 user : " + user.getId());
        } catch (Exception e){
            e.printStackTrace();
            log.error("회원가입 실패");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "{" +
            "\n email : String," +
            "\n password : String" +
            "\n }")
    public ResponseEntity<?> login(@RequestBody UserDto user){
        try {
            http = userService.login(user);
            http.setFlag("success");
            log.info("로그인 성공 user : " + user);
        } catch (Exception e){
            e.printStackTrace();
            log.error("로그인 실패");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable int id){
        try {
            http = userService.detail(id);
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> list() {
        try {
            http = userService.list();
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }
}
