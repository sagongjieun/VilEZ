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
}
