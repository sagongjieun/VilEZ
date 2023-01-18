package kr.co.vilez.user.controller;

import kr.co.vilez.data.HttpVO;
import kr.co.vilez.user.model.dto.UserDto;
import kr.co.vilez.user.model.service.UserService;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/users")
@RestController
@Slf4j
@Log4j2
public class UserController {
    HttpVO http = null;
    @Autowired
    UserService userService;

    // access 토큰 갱신
    @PostMapping("/refresh")
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
    public ResponseEntity<?> modify(@RequestBody UserDto user){
        try {
            System.out.println(user);
            http = userService.modifyUserInfo(user);
            log.info("회원정보 수정 성공 : " + user.getId());
        } catch (Exception e){
            e.printStackTrace();
            log.error("회원정보 수정 실패");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // 회원 정보 변경(Image)
    @PutMapping("/profile")
    public ResponseEntity<?> modifyImg(@RequestParam String userId,
            @RequestParam MultipartFile file) {
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
    public ResponseEntity<?> login(@RequestBody UserDto user){
        try {
            http = userService.login(user);
            log.info("로그인 성공 user : " + user.getId());
        } catch (Exception e){
            e.printStackTrace();
            log.error("로그인 실패");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }
}
