package kr.co.vilez.oauth.model.service;

import kr.co.vilez.oauth.model.dto.KaKaoUserInfoDto;
import kr.co.vilez.oauth.model.dto.OAuthUserDto;

public interface OAuthService {

    void update(OAuthUserDto userDto) throws Exception;
    int joinOauth(OAuthUserDto userDto) throws Exception;
    OAuthUserDto getOAuthUser(String email) throws Exception;
    String getAccessToken(String code) throws Exception;
    KaKaoUserInfoDto getKakaoUserInfo(String accessToken) throws Exception;
}
