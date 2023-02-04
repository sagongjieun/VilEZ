package kr.co.vilez.fcm.model.mapper;

import kr.co.vilez.fcm.model.dto.FCMTokenDto;

import java.sql.SQLException;

public interface TokenMapper {
    void updateUserToken(FCMTokenDto fcmTokenDto) throws SQLException;
}
