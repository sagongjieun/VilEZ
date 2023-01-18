package kr.co.vilez.user.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {
    private int id;
    private String email;
    private String password;
    private String nickName;
    private String area;
    private int point;
    private int manner;
    private String profileImg;
    private String date;
    private String oauth;
    private String accessToken;
    private String refreshToken;
    private int state;

}
