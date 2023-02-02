package kr.co.vilez.qr.model.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import kr.co.vilez.qr.model.dto.QrCodeDto;
import kr.co.vilez.tool.AES256;
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
    final AES256 aes256;
    final OSUpload osUpload;
    final String content = "https://i8d111.p.ssafy.io/server/?code=";
    final String bucketName = "vilez";
    @Override
    public Map<String, String> createQR(int userId) throws Exception {
        String code = aes256.encryptAES256(Integer.toString(userId));

        String tempFileName = "../../"+System.nanoTime()+".png";
        String RealFileName = "qr/"+code+"/"+System.nanoTime();

        // QR 코드 생성
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content+code
                , BarcodeFormat.QR_CODE, 100, 100);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        File file = new File(tempFileName);
        ImageIO.write(bufferedImage, "png", file);

        osUpload.put(bucketName, RealFileName, file);

        file.delete();
        Map<String, String> map = new HashMap<>();
        map.put("path", "https://kr.object.ncloudstorage.com/"
                +bucketName+"/"+RealFileName);

        return map;
    }
}
