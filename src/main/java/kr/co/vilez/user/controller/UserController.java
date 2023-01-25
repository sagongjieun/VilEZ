package kr.co.vilez.user.controller;

import io.swagger.annotations.ApiOperation;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.user.model.dto.UserDto;
import kr.co.vilez.user.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RequestMapping("/users")
@RestController
@Slf4j
@RequiredArgsConstructor
@Log4j2
public class UserController {
    HttpVO http = null;

    final UserService userService;

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
    @ApiOperation(value = "유저 정보 수정한다.", notes = "{\n id : Number" +
            "\n nickName : String" +
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
    @ApiOperation(value = "유저 정보 수정한다.", notes = "{" +
            "\n email : String" +
            "\n password : String" +
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
            "\n email : String" +
            "\n password : String" +
            "\n }")
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
