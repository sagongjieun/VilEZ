package kr.co.vilez.appointment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.service.AppointmentService;
import kr.co.vilez.appointment.model.vo.*;
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

@RequestMapping("/appointments")
@Controller
@AllArgsConstructor
@Slf4j
@Api("약속 관련 API 목록")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PutMapping
    public ResponseEntity<?> modifyAppointment(AppointmentDto appointmentDto){
        HttpVO httpVO = new HttpVO();

        try{

        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAppointmentList(AppointmentDto appointmentDto){
        HttpVO httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        try {
            data.add(appointmentService.getAppointmentList(appointmentDto));
            httpVO.setFlag("success");
            httpVO.setData(data);
        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @PostMapping()
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

    //////////////////////////////////////// chat /////////////////////////////////////////

    private final SimpMessageSendingOperations sendingOperations;


    /*
    *
    *  roomId =>  boardid:(int)user:(sum)
    *  type => 0: 공유자 1: 피공유자
    *  content => 대화 내용
    *
    * */
    @MessageMapping("/recvchat")
    public ChatNoReadVO socketHandler(ChatNoReadVO chatNoReadVO) {
        appointmentService.saveNoReadMsg(chatNoReadVO);
        sendingOperations.convertAndSend("/sendchat/"+chatNoReadVO.getRoomId()+"/"+chatNoReadVO.getType(),chatNoReadVO);
        return chatNoReadVO;
    }

    @MessageMapping("/recvhere")
    public ChatVO recvHereMsg(ChatVO chatVO) {
        appointmentService.recvHereMsg(chatVO);
        return chatVO;
    }

    @MessageMapping("/recvlogin")
    public ChatDatasVO loginMsg(ChatDatasVO chatNoDatasVO) {
        chatNoDatasVO = appointmentService.loadMyChatNoReadList(chatNoDatasVO.getUserId());

        sendingOperations.convertAndSend("/sendlogin/"+chatNoDatasVO.getUserId(),chatNoDatasVO);
        return chatNoDatasVO;
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
        appointmentService.saveLocation(mapVO);
        sendingOperations.convertAndSend("/sendmap/"+mapVO.getRoomId()+"/"+type,mapVO);
        return mapVO;
    }






    @ResponseBody()
    @PostMapping("/chat")
    @ApiOperation(value = "roomId로 채팅기록을 불러온다. (채팅방을 다시 들어갈때) ",
            notes = "EMAIL인증 서버를 불러온다. 'success' 또는 'fail' 문자열과 데이터를 반환한다." +
                    "email : 이메일 정보로 데이터를 날리면 해당 이메일로 코드가 날라가고 " +
                    "해쉬된 코드는 프론트에 저장된다.")
    public ResponseEntity<?> loadChatByRoomId(@RequestBody String roomId) {
        HttpVO http = new HttpVO();
        List<ChatVO> msg = appointmentService.loadMsgByRoomId(roomId);
        http.setFlag("success");
        http.setData(msg);
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody()
    @PostMapping("/chat/yet")
    @ApiOperation(value = "roomId로 채팅기록을 불러온다. (채팅방을 다시 들어갈때) ",
            notes = "EMAIL인증 서버를 불러온다. 'success' 또는 'fail' 문자열과 데이터를 반환한다." +
                    "email : 이메일 정보로 데이터를 날리면 해당 이메일로 코드가 날라가고 " +
                    "해쉬된 코드는 프론트에 저장된다.")
    public ResponseEntity<?> loadNoReadChatByRoomId(@RequestBody RoomDto room) {
        HttpVO http = new HttpVO();
        //List<ChatVO> msg = appointmentService.loadMsgByRoomId(room.getRoomId());
        http.setFlag("success");
        //http.setData(msg);
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody()
    @GetMapping("/map")
    public ResponseEntity<?> loadLocationByRoomId(@RequestParam String roomId) {
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

    @ResponseBody()
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody RoomDto room) {
        HttpVO http = new HttpVO();
        List<Integer> data = new ArrayList<>();
        room = appointmentService.createRoom(room);
        //data.add(room);
        http.setFlag("success");
        http.setData(data);
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

}
