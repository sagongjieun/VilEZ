package kr.co.vilez.appointment.controller;

import kr.co.vilez.appointment.model.service.AppointmentService;
import kr.co.vilez.appointment.model.vo.MapVO;
import kr.co.vilez.appointment.model.vo.RoomVO;
import kr.co.vilez.appointment.model.vo.SocketVO;
import kr.co.vilez.data.HttpVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/appoint")
@Controller
@AllArgsConstructor
@Slf4j
public class AppointmentController {

    private final AppointmentService socketService;
    private final SimpMessageSendingOperations sendingOperations;


    /*
    *
    *  roomId =>  boardid:(int)user:(sum)
    *  type => 0: 공유자 1: 피공유자
    *  content => 대화 내용
    *
    * */
    @MessageMapping("/recvchat")
    @SendTo("/sendchat")
    public SocketVO SocketHandler(SocketVO socketVO) {
//        socketService.dispatchType(socketVO);
        return socketVO;
    }

    /*
     *
     *  roomId =>  boardid:(int)user:(sum)
     *  type => 2: 지도 공유
     *  lat => 지도 공유 위도
     *  lng => 지도 공유 경도
     *  level => 지도 레벨
     * */
    @MessageMapping("/recvmap")
    public MapVO mapHandler(MapVO mapVO) {
        int type = mapVO.getType();
        if(type == 1) {
            type = 2;
        } else {
            type = 1;
        }
        log.info("{}",mapVO);
        socketService.saveLocation(mapVO);
        sendingOperations.convertAndSend("/sendmap/"+mapVO.getRoomId()+"/"+type,mapVO);
        return mapVO;
    }

    @ResponseBody()
    @PostMapping("/chat")
    public ResponseEntity<?> loadChatByRoomId(@RequestBody String roomId) {
        HttpVO http = new HttpVO();
        List<SocketVO> msg = socketService.loadMsgByRoomId(roomId);
        http.setFlag("success");
        http.setData(msg);
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody()
    @GetMapping("/map")
    public ResponseEntity<?> loadLocationByRoomId(@RequestParam String roomId) {
        HttpVO http = new HttpVO();
        List<MapVO> data = new ArrayList<>();
        MapVO vo = socketService.loadLocationByRoomId(roomId);
        if(vo == null) {
            return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
        }
        data.add(vo);
        http.setFlag("success");
        http.setData(data);
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody()
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody RoomVO roomVO) {
        HttpVO http = new HttpVO();
        List<Object> data = new ArrayList<>();
        String roomId = socketService.createRoom(roomVO);
        roomVO.setRoomId(roomId);
        data.add(roomVO);
        http.setFlag("success");
        http.setData(data);
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody()
    @PostMapping("/room/enter")
    public ResponseEntity<?> findRoomId(@RequestBody RoomVO roomVO) {
        HttpVO http = new HttpVO();
        List<Object> data = new ArrayList<>();
        String roomId = socketService.encryptionRoomId(roomVO);
        roomVO.setRoomId(roomId);
        data.add(roomVO);
        http.setFlag("success");
        http.setData(data);
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

}
