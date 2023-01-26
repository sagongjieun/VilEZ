package kr.co.vilez.sign.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.sign.model.dto.SignImg;
import kr.co.vilez.sign.model.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@RequestMapping("/signs")
@RestController
@RequiredArgsConstructor
public class SignController {
    final SignService signService;

    @ApiOperation(value = "서명에 대한 이미지 제공 API를 제공한다.",
    notes = "userId 값과 boardId 값을 넘겨주면 해당하는 게시글과 유저에 대한 계약서 서명 이미지를 받을 수 있다.")
    @GetMapping
    public ResponseEntity<?> getContract(SignImg signImg){
        HttpVO httpVO = new HttpVO();
        ArrayList<SignImg> data = new ArrayList<>();

        try{
            data.add(signService.getContract(signImg));
            httpVO.setData(data);
            httpVO.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @ApiOperation(value = "서명에 대한 이미지 저장 API를 제공한다.",
            notes = "key가 sign인 객체에 userId, boardId 정보 넘겨주고" +
                    "\n\t 사진 한장의 key를 image 이름으로 보내면 된다.")
    @PostMapping
    public ResponseEntity<?> signUpload(@RequestPart(value = "sign")SignImg signImg,
                                        @RequestPart(value = "image") MultipartFile multipartFile){
        HttpVO httpVO = new HttpVO();
        ArrayList<SignImg> data = new ArrayList<>();

        try{
            data.add(signService.signUpload(signImg, multipartFile));
            httpVO.setData(data);
            httpVO.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

}
