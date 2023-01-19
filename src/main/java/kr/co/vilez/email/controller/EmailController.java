package kr.co.vilez.email.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.email.model.service.EmailService;
import kr.co.vilez.tool.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Api("email")
@Controller
public class EmailController {

    SHA256 sha256 = new SHA256();
    @Autowired
    EmailService emailService;

    @ApiOperation(value = "EMAIL 리스트를 불러온다.",
            notes = "EMAIL인증 서버를 불러온다. 'success' 또는 'fail' 문자열과 데이터를 반환한다. " +
                    "email : 이메일 정보로 데이터를 날리면 해당 이메일로 코드가 날라가고 " +
                    "해쉬된 코드는 프론트에 저장된다.",
            response = String.class)
    @PostMapping("/emailConfirm")
    public ResponseEntity<?> emailConfirm(@RequestBody HashMap<String, String> map) throws Exception {
        HttpVO httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        String email = emailService.userEmailCheck(map.get("email"));

        if(email == null) {
            String confirm = emailService.sendSimpleMessage(map.get("email"));
            data.add(sha256.encrypt(confirm));
            httpVO.setData(data);
            httpVO.setFlag("success");
        } else{
            httpVO.setFlag("duplicated");
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @ApiOperation(value = "임시비밀번호 EMAIL로 전송한다.",
            notes = "임시비밀번호를 모달 창에 입력하고 해당 코드가 정확하다면 비밀번호 변경페이지로 이동한다.",
            response = String.class)
    @PostMapping("/passowrd")
    public ResponseEntity<?> modifyPassword(@RequestBody HashMap<String, String> map) throws Exception {
        HttpVO httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        String email = emailService.userEmailCheck(map.get("email"));

        if(email != null) {
            String confirm = emailService.sendSimpleMessage(map.get("email"));
            data.add(sha256.encrypt(confirm));
            httpVO.setData(data);
            httpVO.setFlag("success");
        } else{
            httpVO.setFlag("Not_Exist");
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }
}
