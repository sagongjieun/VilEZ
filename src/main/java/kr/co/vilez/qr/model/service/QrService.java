package kr.co.vilez.qr.model.service;

import java.util.Map;

public interface QrService {
    Map<String, String> createQR(int userId) throws Exception;
}
