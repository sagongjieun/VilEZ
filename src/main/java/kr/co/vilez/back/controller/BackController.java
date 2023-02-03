package kr.co.vilez.back.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import kr.co.vilez.back.model.dto.AppointmentStateDto;
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

    @GetMapping()
    @ApiOperation(value = "게시글의 예약전, 중 상태를 나타내는 API",
            notes = "-- 매개변수 --" +
                    "\n\t boardId = 글 번호" +
                    "shareUserId = 빌려주는 사람" +
                    "notShareUserId = 빌리는 사람" +
                    "type = 공유글인지 요청글인지(공유글 = 2, 요청글 = 1)" +
                    "\n\t -- result --" +
                    "\n\t state 가 0 현재 예약중" +
                    "\n\t -1 예약전" +
                    "\n\t -2 예약완료")
    public ResponseEntity<?> isState( AppointmentStateDto appointmentStateDto){
        HttpVO http = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

        try{
            int num = backService.isState(appointmentStateDto);
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
