package kr.co.vilez.fcm.model.service;

import kr.co.vilez.ask.model.dto.AskDto;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.fcm.model.dto.FCMTokenDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

public interface TokenService {
    HttpVO updateUserToken(FCMTokenDto fcmTokenDto) throws Exception;

}
