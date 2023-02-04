package kr.co.vilez.fcm.model.service;

import kr.co.vilez.data.HttpVO;
import kr.co.vilez.fcm.model.dto.FCMTokenDto;
import kr.co.vilez.fcm.model.mapper.TokenMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
    HttpVO http = null;
    final TokenMapper tokenMapper;
    @Override
    public HttpVO updateUserToken(FCMTokenDto fcmTokenDto) throws Exception {
        ArrayList<Object> data = new ArrayList<>();
        tokenMapper.updateUserToken(fcmTokenDto);
        http = new HttpVO();
        http.setFlag("success");
        data.add(fcmTokenDto.getToken());
        http.setData(data);
        return http;
    }
}
