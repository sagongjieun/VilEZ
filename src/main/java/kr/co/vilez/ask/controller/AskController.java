package kr.co.vilez.ask.controller;

import kr.co.vilez.ask.model.dto.AskDto;
import kr.co.vilez.ask.model.service.AskService;
import kr.co.vilez.data.HttpVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/askboard")
public class AskController {

    @Autowired
    AskService askService;

    @GetMapping("/my/{userid}")
    public ResponseEntity<?> loadMyAskList(@PathVariable("userid") int userId) {
        HttpVO http = new HttpVO();
        List<AskDto> askDtoList = askService.loadMyAskList(userId);
        http.setFlag("success");

        http.setData(askDtoList);

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> loadAskList() {
        HttpVO http = new HttpVO();
        List<AskDto> askDtoList = askService.loadAskList();
        http.setFlag("success");

        http.setData(askDtoList);

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> writeAskBoard(@RequestPart(value = "board",required = false) AskDto askDto,
                                           @RequestPart(value = "image", required = false) MultipartFile[] files) {
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
    public ResponseEntity<?> updateAskBoard(@RequestPart(value = "board",required = false) AskDto askDto,
                                            @RequestPart(value = "image", required = false) MultipartFile[] files){
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
    public ResponseEntity<?> detailArticle(@PathVariable int boardId) {
        HttpVO http = new HttpVO();
        List<AskDto> data = new ArrayList<>();
        AskDto askDto = askService.detailArticle(boardId);
        http.setFlag("success");
        data.add(askDto);
        http.setData(data);
        return new ResponseEntity<HttpVO>(http,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteArticle(@RequestBody AskDto askDto) {
        HttpVO httpVO = new HttpVO();
        httpVO.setFlag("success");
        askService.deleteArticle(askDto.getId());
        return new ResponseEntity<HttpVO>(httpVO,HttpStatus.OK);
    }

}
