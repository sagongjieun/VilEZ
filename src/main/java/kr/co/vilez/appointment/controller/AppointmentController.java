package kr.co.vilez.appointment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.service.AppointmentService;
import kr.co.vilez.appointment.model.vo.*;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.user.model.dto.UserDto;
import kr.co.vilez.user.model.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/appointments")
@Controller
@AllArgsConstructor
@Slf4j
@Api("약속 관련 API 목록")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final SimpMessageSendingOperations sendingOperations;

    @GetMapping("/check/{boardId}")
    @ApiOperation(value = "현재 공유 중인지 아닌지에 대한 정보를 요청하는 API",
    notes = "boardId에 boardDetail의 boardId 정보를 넣고 전송하면" +
            "\n\t 현재 날짜와 비교해서 예약중인 boardId면 해당 boardId 값이 return" +
            "\n\t 그렇지 않으면 null 값이 들어온다.")
    public ResponseEntity<?> getBoardState(@PathVariable int boardId){
        HttpVO httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        try {
            data.add(appointmentService.getBoardState(boardId));
            httpVO.setData(data);
            httpVO.setFlag("success");
            httpVO.setData(data);
        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    // 내가 공유받은 물품 목록을 볼 수 있다.
    // 한 게시글에서 여러번 물건을 빌렸으면 가장 최근에 빌린 내역만이 나온다.
    @ResponseBody
    @GetMapping("/my/{userId}")
    @ApiOperation(value = "나의 약속 정보들을 불러온다." ,
            notes = "List에 dto 담아서 리턴")
    public ResponseEntity<?> getMyAppointmentShare(@PathVariable int userId){
        HttpVO httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        try {
            data.add(appointmentService.getMyAppointmentList(userId));
            httpVO.setFlag("success");
            httpVO.setData(data);
        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/{boardId}")
    @ApiOperation(value = "글 번호에 약속 정보들을 불러온다." ,
            notes = "List에 dto 담아서 리턴")
    public ResponseEntity<?> getAppointmentList(@PathVariable int boardId){
        HttpVO httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();
        try {
            data.add(appointmentService.getAppointmentList(boardId));
            httpVO.setFlag("success");
            httpVO.setData(data);
        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping
    @ApiOperation(value = "약속 정보를 저장한다." ,
            notes = "")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDto appointmentDto){
        HttpVO httpVO = new HttpVO();
        try {
            appointmentService.create(appointmentDto);
            httpVO.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping("/room/enter/{roomId}")
    @ApiOperation(value = "roomId로 채팅기록을 불러온다. (채팅방을 다시 들어갈때) ")
    public ResponseEntity<?> loadChatByRoomId(@PathVariable int roomId) {
        HttpVO http = new HttpVO();
        List<ChatVO> msg = appointmentService.loadMsgByRoomId(roomId);
        http.setFlag("success");
        http.setData(msg);
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/map/{roomId}")
    @ApiOperation(value = "roomId로 마지막 저장된 위치를 불러온다.")
    public ResponseEntity<?> loadLocationByRoomId(@PathVariable int roomId) {
        HttpVO http = new HttpVO();
        List<MapVO> data = new ArrayList<>();
        MapVO vo = appointmentService.loadLocationByRoomId(roomId);
        if(vo == null) {
            return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
        }
        data.add(vo);
        http.setFlag("success");
        http.setData(data);
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody RoomDto room) {
        HttpVO http = new HttpVO();
        List<RoomDto> data = new ArrayList<>();
        room = appointmentService.createRoom(room);

        data.add(room);
        http.setFlag("success");
        http.setData(data);
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/room/{userId}")
    @ApiOperation(value = "userId로 마지막 채팅내용 리스트를 불러온다.")
    public ResponseEntity<?> getRoomListByUserId(@PathVariable int userId) {
        HttpVO http = new HttpVO();
        http.setFlag("success");
        http.setData(appointmentService.loadMyChatList(userId));
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/room/text")
    public ResponseEntity<?> debug(@RequestBody ChatVO chatVO) {
        HttpVO http = new HttpVO();
        http.setFlag("success");
        appointmentService.recvMsg(chatVO);
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/room/board/{roomId}")
    @ApiOperation(value = "Room ID로 board 정보 가져오는 메소드 (path) ")
    public ResponseEntity<?> getBoard(@PathVariable int roomId) {
        HttpVO http = new HttpVO();
        http.setFlag("success");
        List<RoomDto> list = new ArrayList<>();

        RoomDto roomDto = appointmentService.getBoard(roomId);
        list.add(roomDto);
        http.setData(list);
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    ///////////////////////////////////////////////////////////

    @MessageMapping("/recvchat")
    public ChatVO socketHandler(ChatVO chatVO) {
        appointmentService.recvMsg(chatVO);
        HashMap<String, Object> map = new HashMap<>();
        UserDto user = null;
        try {
            user = userService.detail2(chatVO.getFromUserId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        map.put("nickName",user.getNickName());
        map.put("area", user.getArea());
        map.put("content", chatVO.getContent());
        map.put("roomId",chatVO.getRoomId());
        map.put("fromUserId",chatVO.getFromUserId());
        sendingOperations.convertAndSend("/sendlist/"+chatVO.getToUserId(),map);
//        sendingOperations.convertAndSend("/sendmy/"+chatVO.getRoomId()+"/"+chatVO.getFromUserId(),chatVO);
        sendingOperations.convertAndSend("/sendchat/"+chatVO.getRoomId()+"/"+chatVO.getToUserId(),chatVO);
        return chatVO;
    }


    @MessageMapping("/room_enter")
    public void setEnterTimeMsg(HashMap<String, Integer> payload) {
        appointmentService.setEnterTimeMsg(payload.get("roomId"), payload.get("userId"));
    }

    @MessageMapping("/room_list")
    public void getRoomList(HashMap<String, Integer> payload) {
        List<?> list = appointmentService.loadMyChatList(payload.get("userId"));
        sendingOperations.convertAndSend("/send_room_list/"+payload.get("userId"),list);
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
        int toUserId = mapVO.getToUserId();
        appointmentService.saveLocation(mapVO);
        sendingOperations.convertAndSend("/sendmap/"+mapVO.getRoomId()+"/"+toUserId,mapVO);
        return mapVO;
    }
}
