package kr.co.vilez.oauth.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import kr.co.vilez.appointment.model.dao.AppointmentDao;
import kr.co.vilez.appointment.model.vo.PointVO;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.jwt.JwtProvider;
import kr.co.vilez.oauth.model.dto.UserInfoDto;
import kr.co.vilez.oauth.model.dto.OAuthUserDto;
import kr.co.vilez.oauth.model.service.KakaoOAuthService;
import kr.co.vilez.oauth.model.service.NaverOAuthService;
import kr.co.vilez.tool.SHA256;
import kr.co.vilez.user.model.dto.UserDto;
import kr.co.vilez.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("oauth2")
public class OAuthController {

    SHA256 sha256 = new SHA256();
    @Autowired
    KakaoOAuthService oAuthService;
    @Autowired
    UserService userService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    NaverOAuthService naverOAuthService;
    @Autowired
    AppointmentDao appointmentDao;

    @GetMapping("/code/kakao")
    @ApiOperation(value = "카카오 OAuth 로그인.",
            notes = "https://kauth.kakao.com/oauth/authorize\n" +
                    "?client_id=bc1b0d18239d9332d371b33821ffb2e5\n" +
                    "&redirect_uri=http://i8d111.p.ssafy.io:8082/vilez/oauth2/code/kakao\n" +
                    "&response_type=code" +
                    "해당 주소로 url 연결 부탁합니다.",
            response = String.class)
    public ResponseEntity<?> getCodeKakao(@RequestParam String code){
        HttpVO httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        // 카카오 로그인 url을 통해 인증시 redirect url에 code 발송
        System.out.println("code = " + code);

        try {
            String access_token = oAuthService.getAccessToken(code);
            System.out.println("access_token = " + access_token);
            // code를 통해 로그인 사용자 정보 조회 가능한 access_token 발급
            // jwt의 access 토큰이 아님!, 카카오 유저 정보를 받아오기 위한 access_token

            UserInfoDto kaKaoUserInfoDto = oAuthService.getKakaoUserInfo(access_token);
            System.out.println("kaKaoUserInfoDto = " + kaKaoUserInfoDto);
            // 토큰을 통해 유저 고유값 유저 id 번호와 닉네임 정보 조회

            OAuthUserDto oAuthUser = oAuthService.getOAuthUser(kaKaoUserInfoDto.getId());
            // 해당 회원정보가 이미 존재하는지 조회

            // null 이면 회원가입 가능한 이메일
            if(oAuthUser == null){
                String tempNickName = "#KAKAO"+kaKaoUserInfoDto.getId();
                System.out.println("tempNickName = " + tempNickName);
                oAuthUser = new OAuthUserDto();
                // 회원가입 진행
                oAuthUser.setOauth("kakao");
                oAuthUser.setEmail(kaKaoUserInfoDto.getId());
                oAuthUser.setNickName(tempNickName);
                oAuthUser.setPassword(Math.random()+sha256.encrypt(kaKaoUserInfoDto.getAccount()));
                oAuthUser.setProfileImg(kaKaoUserInfoDto.getPath().replaceAll("\"", ""));

                int userId = oAuthService.joinOauth(oAuthUser);
                PointVO pointVO = new PointVO();
                pointVO.setBoardId(-1);
                pointVO.setUserId(userId);
                pointVO.setPoint(100);
                pointVO.setType(-1);
                LocalDate now = LocalDate.now();
                pointVO.setDate(now.toString());
                appointmentDao.savePoint(pointVO);

                String accessToken = jwtProvider.createToken(Integer.toString(userId), tempNickName);
                String refreshToken = jwtProvider.createRefreshToken(Integer.toString(userId), tempNickName);

                oAuthUser.setId(userId);
                oAuthUser.setAccessToken(accessToken);
                oAuthUser.setRefreshToken(refreshToken);
                oAuthService.update(oAuthUser);

                oAuthUser.setPassword("");
                data.add(oAuthUser);
                httpVO.setFlag("oauth_join_success & login_success");
            } else{
                // null 이 아니면, 가입이 불가능한 이메일로 어느 OAuth를 통해 가입했는지를 리턴
                if(oAuthUser.getOauth().equals("kakao")) {
                    String accessToken = jwtProvider.createToken(Integer.toString(oAuthUser.getId()), oAuthUser.getNickName());
                    oAuthUser.setAccessToken(accessToken);

                    data.add(oAuthUser);
                    httpVO.setFlag("login_success");
                } else{
                    // 카카오가 아니면 어디에서 가입했는지만 알려준다.
                    data.add(oAuthUser.getOauth());
                    httpVO.setFlag("id_exist");
                }

            }
            httpVO.setData(data);
        } catch (Exception e){
            e.printStackTrace();
        }
        
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }
    @ApiOperation(value = "네이버 OAuth 로그인.",
            notes = "https://nid.naver.com/oauth2.0/authorize?\n" +
                    "response_type=code&\n" +
                    "client_id=KdnHuDfQRJ8OKSLDdgB6&\n" +
                    "state=randomtext&\n" +
                    "redirect_uri=http://i8d111.p.ssafy.io:8082/vilez/oauth2/code/naver" +
                    "해당 주소로 url 연결 부탁합니다.",
            response = String.class)
    @GetMapping("/code/naver")
    public ResponseEntity<?> getCodeNaver(@RequestParam String code){
        HttpVO httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        try {
            String accessToken = naverOAuthService.getAccessToken(code);
            System.out.println("accessToken = " + accessToken);
            UserInfoDto userInfoDto = naverOAuthService.getNaverUserInfo(accessToken);
            System.out.println("userInfoDto = " + userInfoDto);

            // 기존 회원정보가 존재하는지 확인
            OAuthUserDto userDto = naverOAuthService.getOAuthUser(userInfoDto.getId());

            // 기존 회원정보가 존재하지않으면 회원가입을 자동으로 진행하고, 그렇지 않으면 로그인을 시도한다.
            if(userDto == null){
                String tempNickName = "#NAVER"+userInfoDto.getId();
                System.out.println("tempNickName = " + tempNickName);

                userDto = new OAuthUserDto();
                // 회원가입 진행
                userDto.setOauth("naver");
                userDto.setEmail(userInfoDto.getId());
                userDto.setNickName(tempNickName);
                userDto.setPassword(Math.random()+sha256.encrypt(userInfoDto.getAccount()));
                userDto.setProfileImg(userInfoDto.getPath().replaceAll("\"", ""));

                int userId = naverOAuthService.joinOauth(userDto);

                PointVO pointVO = new PointVO();
                pointVO.setBoardId(-1);
                pointVO.setUserId(userId);
                pointVO.setPoint(100);
                pointVO.setType(-1);
                LocalDate now = LocalDate.now();
                pointVO.setDate(now.toString());
                appointmentDao.savePoint(pointVO);

                String access_Token = jwtProvider.createToken(Integer.toString(userId), tempNickName);
                String refresh_Token = jwtProvider.createRefreshToken(Integer.toString(userId), tempNickName);

                userDto.setId(userId);
                userDto.setAccessToken(access_Token);
                userDto.setRefreshToken(refresh_Token);
                oAuthService.update(userDto);

                userDto.setPassword("");
                data.add(userDto);
                httpVO.setFlag("oauth_join_success & login_success");
            } else{
                // null 이 아니면, 가입이 불가능한 이메일로 어느 OAuth를 통해 가입했는지를 리턴
                if(userDto.getOauth().equals("naver")) {
                    String access_Token = jwtProvider.createToken(Integer.toString(userDto.getId()),
                            userDto.getNickName());
                    userDto.setAccessToken(accessToken);

                    data.add(userDto);
                    httpVO.setFlag("login_success");
                } else{
                    // 카카오가 아니면 어디에서 가입했는지만 알려준다.
                    data.add(userDto.getOauth());
                    httpVO.setFlag("id_exist");
                }
            }

            httpVO.setData(data);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }


}
