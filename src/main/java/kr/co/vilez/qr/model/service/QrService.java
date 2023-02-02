package kr.co.vilez.qr.model.service;

import kr.co.vilez.qr.model.dto.QrCodeDto;

import java.util.Map;

public interface QrService {
    void deleteQr(String url) throws Exception;
    Map<String, String> createQR(int userId) throws Exception;
}
