package kr.co.vilez.qr.controller;

import io.swagger.annotations.ApiOperation;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.qr.model.service.QrService;
import kr.co.vilez.tool.AES256;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/qrcodes")
@RequiredArgsConstructor
public class QrController {

    final QrService qrService;
    @PostMapping()
    @ApiOperation(value = "QR 코드를 생성하는 API",
            notes = "userId를 보내주면 해당하는 qr코드를 생성해준다.")
    public ResponseEntity<?> createQr(int userId) {
        HttpVO httpVO = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

        try{
            data.add(qrService.createQR(userId));
            httpVO.setFlag("success");
            httpVO.setData(data);
        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @DeleteMapping()
    @ApiOperation(value = "QR 코드를 삭제하는 API",
            notes = "해당 이미지의 url을 보내주면 해당하는 qr코드를 삭제해준다." +
                    "\n\t https://kr.object.ncloudstorage.com/vilez/qr/1/5446758498600 이미지 url이 이렇다면" +
                    "\n\t qr/1/5446758498600 이것만 보내줘요")
    public ResponseEntity<?> deleteQr(String imgUrl){
        HttpVO httpVO = new HttpVO();

        try{
            qrService.deleteQr(imgUrl);
            httpVO.setFlag("success");
        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }
}
