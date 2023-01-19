package kr.co.vilez.oauth.model.service;

import kr.co.vilez.oauth.model.dto.OAuthUserDto;
import kr.co.vilez.oauth.model.dto.UserInfoDto;

import java.sql.SQLException;

public interface NaverOAuthService {
    void update(OAuthUserDto userDto) throws Exception;
    int joinOauth(OAuthUserDto userDto) throws Exception;
    OAuthUserDto getOAuthUser(String email) throws Exception;
    UserInfoDto getNaverUserInfo(String accessToken) throws Exception;
    String getAccessToken(String code) throws Exception;
}
