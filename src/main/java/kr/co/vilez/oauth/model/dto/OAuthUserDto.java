package kr.co.vilez.oauth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUserDto {
    private int id;
    private String email;
    private String password;
    private String nickName;
    private String area;
    private String point;
    private String manner;
    private String profileImg;
    private String date;
    private String oauth;
    private String accessToken;
    private String refreshToken;
    private int state;
}
