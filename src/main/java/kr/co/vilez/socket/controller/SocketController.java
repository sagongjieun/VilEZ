package kr.co.vilez.socket.controller;

import kr.co.vilez.data.HttpVO;
import kr.co.vilez.socket.model.service.SocketService;
import kr.co.vilez.socket.model.vo.RoomVO;
import kr.co.vilez.socket.model.vo.SocketVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/socket")
@Controller
public class SocketController {

    private SocketService socketService;
    private final SimpMessageSendingOperations sendingOperations;
    public SocketController(@Autowired SocketService socketService, @Autowired SimpMessageSendingOperations sendingOperations) {
        this.socketService = socketService;
        this.sendingOperations = sendingOperations;
    }

    /*
    *
    *  roomId =>  boardid:(int)user:(sum)
    *  type => 0: 공유자 1: 피공유자
    *  content => 대화 내용
    *
    * */
    @MessageMapping("/receive")
    @SendTo("/send")
    public SocketVO SocketHandler(SocketVO socketVO) {
        socketService.dispatchType(socketVO);
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
    @MessageMapping("/map")
    public SocketVO mapHandler(SocketVO socketVO) {
        socketService.dispatchType(socketVO);
        int type = Integer.parseInt(socketVO.getType());
        type = (type%2 + 1);
        sendingOperations.convertAndSend("/sendmap/"+String.valueOf(type),socketVO);
        return socketVO;
    }

    @MessageMapping("/marker")
    public SocketVO marker(SocketVO socketVO) {
        sendingOperations.convertAndSend("/sendmarker",socketVO);
        return socketVO;
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
        List<Object> data = new ArrayList<>();
        SocketVO msg = socketService.loadLocationByRoomId(roomId);
        data.add(msg);
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
    /*
    TODO : 만들긴 했는데 비동기때문에 소켓보다 먼저 일어나게 라이프 사이클 조정해야할듯?
    */
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
