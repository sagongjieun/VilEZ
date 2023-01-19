package kr.co.vilez.oauth.model.service;

import kr.co.vilez.oauth.model.dto.UserInfoDto;
import kr.co.vilez.oauth.model.dto.OAuthUserDto;

public interface KakaoOAuthService {

    void update(OAuthUserDto userDto) throws Exception;
    int joinOauth(OAuthUserDto userDto) throws Exception;
    OAuthUserDto getOAuthUser(String email) throws Exception;
    String getAccessToken(String code) throws Exception;
    UserInfoDto getKakaoUserInfo(String accessToken) throws Exception;
}
