package kr.co.vilez.oauth.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KaKaoUserInfoDto {
    private String id;
    private String nickName;
    private String kakao_account;
    private String path;
}
