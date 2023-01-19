package kr.co.vilez.oauth.model.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.vilez.oauth.model.dto.OAuthUserDto;
import kr.co.vilez.oauth.model.dto.UserInfoDto;
import kr.co.vilez.oauth.model.mapper.NaverOAuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;

@Service
public class NaverOAuthServiceImpl implements NaverOAuthService {
    String clientId = "KdnHuDfQRJ8OKSLDdgB6";
    String clientScret = "iaX0nJ2Q5Z";
    String state = "randomtext";

    @Autowired
    NaverOAuthMapper oAuthMapper;

    @Override
    public void update(OAuthUserDto userDto) throws SQLException {
        oAuthMapper.update(userDto);
    }

    @Override
    public int joinOauth(OAuthUserDto userDto) throws Exception {
        oAuthMapper.joinOAuth(userDto);
        return userDto.getId();
    }

    @Override
    public OAuthUserDto getOAuthUser(String email) throws Exception {
        return oAuthMapper.getOAuthUser(email);
    }

    public String getAccessToken(String code) throws Exception {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientScret);
        body.add("code", code);
        body.add("state", state);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
//        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    public UserInfoDto getNaverUserInfo(String accessToken) throws Exception {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                naverUserInfoRequest,
                String.class
        );

        // HTTP 응답 받아오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

//        String total = jsonNode.get("response").toString();
        String id = jsonNode.get("response").get("id").asText();
        String nickname = jsonNode.get("response").get("nickname").toString();
        String account = jsonNode.get("response").get("email").toString();
        String path = jsonNode.get("response").get("profile_image").toString();

        return new UserInfoDto(id, nickname, account, path);
    }
}
