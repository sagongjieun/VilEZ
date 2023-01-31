package kr.co.vilez.qr.model.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import kr.co.vilez.tool.OSUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QrSerivceImpl implements QrService {
    final OSUpload osUpload;
    final String content = "https://www.naver.com/?userId=";
    final String bucketName = "vilez";
    @Override
    public Map<String, String> createQR(int userId) throws Exception {
        String fileName = "../../"+System.nanoTime()+".png";

        // QR 코드 생성
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content+userId, BarcodeFormat.QR_CODE, 100, 100);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        File file = new File(fileName);
        ImageIO.write(bufferedImage, "png", file);

        osUpload.put(bucketName, fileName, file);

        Map<String, String> map = new HashMap<>();
        map.put("path", "https://kr.object.ncloudstorage.com/"
                +bucketName+"/"+fileName);

        return map;
    }
}
