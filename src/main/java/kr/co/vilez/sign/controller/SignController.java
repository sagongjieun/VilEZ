package kr.co.vilez.sign.controller;

import io.swagger.annotations.ApiOperation;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.sign.model.dto.SignImg;
import kr.co.vilez.sign.model.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequestMapping("/signs")
@RestController
@RequiredArgsConstructor
public class SignController {
    final SignService signService;

    @ApiOperation(value = "서명에 대한 이미지 정보 API를 제공한다.",
            notes = "피공유자 notShareUserId, 공유자 ShareUserId, 게시글 정보 boardId 정보를 주면" +
                    "\n\t 해당하는 정보를 제공해준다.")
    @GetMapping
    public ResponseEntity<?> getContract(SignImg signImg){
        System.out.println("signImg = " + signImg);
        
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
            notes = "피공유자 notShareUserId, 공유자 ShareUserId, 게시글 정보 boardId 정보와" +
                    "\n\t shareSign에는 공유자 이미지 인코딩 정보를 넣어준다" +
                    "\n\t notShareSign에는 피공유자 이미지 인코딩 정보를 넣어준다.")
    @PostMapping
    public ResponseEntity<?> signUpload(@RequestBody SignImg signImg){
        HttpVO httpVO = new HttpVO();
        ArrayList<SignImg> data = new ArrayList<>();

        try{
            data.add(signService.signUpload(signImg));
            httpVO.setData(data);
            httpVO.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @ApiOperation(value = "서명에 대한 이미지 삭제 API를 제공한다.",
            notes = "피공유자 notShareUserId, 공유자 ShareUserId, 게시글 정보 boardId 정보를 주면" +
                    "\n\t 해당하는 정보를 삭제해준다.")
    @DeleteMapping
    public ResponseEntity<?> deleteContract(SignImg signImg){
        HttpVO httpVO = new HttpVO();
        ArrayList<SignImg> data = new ArrayList<>();

        try{
            signService.deleteContract(signImg);
            httpVO.setData(data);
            httpVO.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

}
