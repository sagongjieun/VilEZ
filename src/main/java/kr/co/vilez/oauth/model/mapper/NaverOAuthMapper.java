package kr.co.vilez.oauth.model.mapper;

import kr.co.vilez.oauth.model.dto.OAuthUserDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;

@Mapper
public interface NaverOAuthMapper {
    void update(OAuthUserDto userDto) throws SQLException;
    int joinOAuth(OAuthUserDto userDto) throws SQLException;
    OAuthUserDto getOAuthUser(String email) throws SQLException;
}
