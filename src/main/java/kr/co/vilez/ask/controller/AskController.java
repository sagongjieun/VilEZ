package kr.co.vilez.ask.controller;

import io.swagger.annotations.ApiOperation;
import kr.co.vilez.ask.model.dto.AskDto;
import kr.co.vilez.ask.model.service.AskService;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.share.model.dto.PageNavigator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/askboard")
public class AskController {
    final AskService askService;

    @GetMapping("/my/{userid}")
    @ApiOperation(value = "내의 요청글 리스트를 불러온다.", notes = "userId : Number \n" +
            "List로 AskDto가 출력된다.")
    public ResponseEntity<?> loadMyAskList(@PathVariable("userid") int userId) {
        HttpVO http = new HttpVO();
        List<AskDto> askDtoList = askService.loadMyAskList(userId);

        http.setFlag("success");
        http.setData(askDtoList);

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "전체 요청글 리스트를 불러온다." ,
                 notes = "List로 전체 AskDto가 출력된다.")
    public ResponseEntity<?> loadAskList(PageNavigator pageNavigator) {
        HttpVO http = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

        try {
            data.add(askService.loadAskList(pageNavigator));
            http.setFlag("success");
            http.setData(data);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "글 작성을 한다. (이미지 필수X)" ,
            notes = "{\n \t userId : Number," +
                    "\n \t category : String," +
                    "\n \t title : String," +
                    "\n \t hopeAreaLat : String," +
                    "\n \t hopeAreaLng : String," +
                    "\n \t startDay : String," +
                    "\n \t endDay : String" +
                    "\n \t }")
    public ResponseEntity<?> writeAskBoard(@RequestPart(value = "board",required = false) AskDto askDto,
                                           @RequestPart(value = "image", required = false) MultipartFile[] files) throws IOException {
        HttpVO http = new HttpVO();
        askDto.setId(-1);
        askService.writeAskBoard(askDto, files);
        //글 작성 성공 : 0 , 실패 : -1
        if(askDto.getId() != -1) {
            List<AskDto> data = new ArrayList<AskDto>();
            data.add(askDto);
            http.setFlag("success");
            http.setData(data);
        }
        return new ResponseEntity<HttpVO>(http,HttpStatus.OK);
    }

    @PutMapping
    @ApiOperation(value = "글 수정을 한다. (이미지 필수X)" ,
            notes = "{\n \t boardId : Number," +
                    "\n \t category : String," +
                    "\n \t title : String," +
                    "\n \t hopeAreaLat : String," +
                    "\n \t hopeAreaLng : String," +
                    "\n \t startDay : String," +
                    "\n \t endDay : String" +
                    "\n \t }")
    public ResponseEntity<?> updateAskBoard(@RequestPart(value = "board",required = false) AskDto askDto,
                                            @RequestPart(value = "image", required = false) MultipartFile[] files) throws IOException {
        HttpVO http = new HttpVO();
        askService.updateAskBoard(askDto, files);
        //글 작성 성공 : 0 , 실패 : -1
        List<AskDto> data = new ArrayList<AskDto>();
        data.add(askDto);
        http.setFlag("success");
        http.setData(data);

        return new ResponseEntity<HttpVO>(http,HttpStatus.OK);
    }

    @GetMapping("/detail/{boardId}")
    @ApiOperation(value = "글 상세 정보를 본다" ,
            notes = "boardId 를 path로 보내준다.")
    public ResponseEntity<?> detailArticle(@PathVariable int boardId) {
        HttpVO http = new HttpVO();
        List<AskDto> data = new ArrayList<>();
        AskDto askDto = askService.detailArticle(boardId);
        http.setFlag("success");
        data.add(askDto);
        http.setData(data);
        return new ResponseEntity<HttpVO>(http,HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteArticle(@PathVariable int boardId) {
        HttpVO httpVO = new HttpVO();
        httpVO.setFlag("success");
        askService.deleteArticle(boardId);
        return new ResponseEntity<HttpVO>(httpVO,HttpStatus.OK);
    }

}
