package kr.co.vilez.fcm.controller;

import java.io.IOException;

import io.swagger.annotations.ApiOperation;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.fcm.model.dto.FCMTokenDto;
import kr.co.vilez.fcm.model.service.FCMDataService;
import kr.co.vilez.fcm.model.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin("*")
public class FCMController {
    HttpVO http = null;
	private static final Logger logger = LoggerFactory.getLogger(FCMController.class);

    @Autowired
    TokenService tokenService;

    // 백그라운드에서 동작가능한 data service
    @Autowired
    FCMDataService dataService;

    @ApiOperation(value = "새로운 토큰을 서버에 저장한다.",
            notes = "성공시 flag = success, data = user.area에 저장한 fcm token, 실패시 flag=fail")
    @PostMapping("/token")
    public ResponseEntity<?> registToken(@RequestBody FCMTokenDto token) {
        HttpVO http = new HttpVO();
        try {
            if(dataService.addToken(token.getToken())) { // 토큰 추가
                logger.info("registToken : token:{}", token);
                // 유저 정보도 수정해야함
                http = tokenService.updateUserToken(token);
                logger.info("유저 fcm 토큰정보 수정완료!!!!");
            } else {
                http.setFlag("fail");
                logger.info("유저 fcm 토큰정보 수정실패!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("fcm 토큰 추가 중 에러 발생");
        }
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ApiOperation(value = "가지고 있는 모든 토큰에 메시지를 보낸다",
            notes = "")
    @PostMapping("/broadcast")
    public Integer broadCast(String title, String body) throws IOException {
    	logger.info("broadCast : title:{}, body:{}", title, body);
    	return dataService.broadCastMessage(title, body);
    }

    @ApiOperation(value = "특정 토큰에 FCM을 보낸다.",
            notes = "")
    @PostMapping("/sendMessageTo")
    public void sendMessageTo(String token, String title, String body) throws IOException {
    	logger.info("sendMessageTo : token:{}, title:{}, body:{}", token, title, body);
        dataService.sendMessageTo(token, title, body);
    }

    @ApiOperation(value = "채팅 FCM을 보낸다.",
            notes = "안드로이드에서 noti 받을때 roomId, otherUserId는 Integer 으로 타입캐스팅 필요")
    @PostMapping("/sendChatMessageTo")
    public void sendChatMessageTo(String token, Integer roomId, Integer otherUserId, String title, String body) throws IOException {
        logger.info("sendMessageTo : token:{}, title:{}, body:{}", token, title, body);
        dataService.sendChatMessageTo(token, roomId, otherUserId, title, body);
    }
    

}

