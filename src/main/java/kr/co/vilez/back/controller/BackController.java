package kr.co.vilez.back.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.vilez.back.model.dto.ReturnRequestDto;
import kr.co.vilez.back.model.service.BackService;
import kr.co.vilez.data.HttpVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/returns")
@Controller
@AllArgsConstructor
@Slf4j
@Api("반납 관련 API 목록")
public class BackController {
    final BackService backService;

    @PostMapping("/confirmed/returns")
    @ApiOperation(value = "반납 확정 API",
            notes = "-- return --" +
                    "\n\t 반납확정시 예약된 내역을 삭제하고(-1로 설정)")
    public ResponseEntity<?> confirmedReturns(@RequestBody ReturnRequestDto returnRequestDto){
        HttpVO http = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

        try{
            data.add(backService.confirmedReturns(returnRequestDto.getRoomId()));
            http.setData(data);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping()
    @ApiOperation(value = "반납 요청을 확인하는 API",
    notes = "-- return --" +
            "\n\t 반납 요청이 있으면 true, 없으면 false")
    public ResponseEntity<?> requestReturn(@RequestParam int roomId){
        HttpVO http = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

        try{
            data.add(backService.checkReturnRequest(roomId));
            http.setData(data);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "반납 요청을 보내는 API")
    public ResponseEntity<?> requestReturn(@RequestBody ReturnRequestDto returnRequestDto){
        HttpVO http = new HttpVO();

        try{
            backService.requestReturn(returnRequestDto);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping("/state")
    @ApiOperation(value = "게시글의 예약전, 중 상태를 나타내는 API",
            notes = "-- 매개변수 --" +
                    "\n\t roomId : 채팅 방번호" +
                    "\n\t-- return --" +
                    "\n\t 0 : 예약 후" +
                    "\n\t -1 : 예약 전(반납 완료)" +
                    "\n\t -2 : 에약 전(예약 취소)" +
                    "\n\t -3 : 예약 전(한번도 공유를 신청한 적 없는 경우)"
                    )
    public ResponseEntity<?> isState(@RequestParam int roomId){
        HttpVO http = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

        try{
            int num = backService.isState(roomId);

            Map<String, Integer> map = new HashMap<>();
            map.put("state", num);
            data.add(map);

            http.setData(data);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

}
