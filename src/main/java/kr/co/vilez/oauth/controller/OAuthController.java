package kr.co.vilez.oauth.controller;

import kr.co.vilez.data.HttpVO;
import kr.co.vilez.jwt.JwtProvider;
import kr.co.vilez.oauth.model.dto.UserInfoDto;
import kr.co.vilez.oauth.model.dto.OAuthUserDto;
import kr.co.vilez.oauth.model.service.KakaoOAuthService;
import kr.co.vilez.oauth.model.service.NaverOAuthService;
import kr.co.vilez.tool.SHA256;
import kr.co.vilez.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/code/kakao")
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

            OAuthUserDto oAuthUser = oAuthService.getOAuthUser(kaKaoUserInfoDto.getKakao_account());
            // 해당 회원정보가 이미 존재하는지 조회

            // null 이면 회원가입 가능한 이메일
            if(oAuthUser == null){
                String email = kaKaoUserInfoDto.getKakao_account();
                String tempNickName = "#KAKAO"+kaKaoUserInfoDto.getId().hashCode();
                System.out.println("tempNickName = " + tempNickName);
                oAuthUser = new OAuthUserDto();
                // 회원가입 진행
                oAuthUser.setOauth("kakao");
                oAuthUser.setEmail(email);
                oAuthUser.setNickName(tempNickName);
                oAuthUser.setPassword(Math.random()+sha256.encrypt(kaKaoUserInfoDto.getKakao_account()));
                oAuthUser.setProfileImg(kaKaoUserInfoDto.getPath());

                int userId = oAuthService.joinOauth(oAuthUser);
                String accessToken = jwtProvider.createToken(Integer.toString(userId), tempNickName);
                String refreshToken = jwtProvider.createRefreshToken(Integer.toString(userId), tempNickName);

                oAuthUser.setId(userId);
                oAuthUser.setAccessToken(accessToken);
                oAuthUser.setRefreshToken(refreshToken);
                oAuthService.update(oAuthUser);

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

    @GetMapping("/code/naver")
    public ResponseEntity<?> getCodeNaver(@RequestParam String code){
        HttpVO httpVO = new HttpVO();



        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }
}
