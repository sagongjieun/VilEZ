package kr.co.vilez.appointment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.dto.CancelAppointmentDto;
import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.dto.SetPeriodDto;
import kr.co.vilez.appointment.model.service.AppointmentService;
import kr.co.vilez.appointment.model.vo.*;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.fcm.model.service.FCMDataService;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/appointments")
@Controller
@AllArgsConstructor
@Slf4j
@Api("약속 관련 API 목록")
public class AppointmentController {
    private final FCMDataService fcmDataService;
    private final AppointmentService appointmentService;
    private final UserService userService;
    private final SimpMessageSendingOperations sendingOperations;
    @GetMapping("/date")
    @ApiOperation(value = "채팅방 유저간의 약속된 시간을 불러오는 API")
    public ResponseEntity<?> getAppointmentDate(@RequestParam int boardId,
                                                @RequestParam int shareUserId,
                                                @RequestParam int notShareUserId,
                                                @RequestParam int type){
        HttpVO http = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

        try{
            data.add(appointmentService.getAppointmentDate(boardId, shareUserId, notShareUserId, type));
            http.setData(data);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PostMapping("/request/cancel")
    @ApiOperation(value = "피공유자가 예약 취소 요청을 보내는 API")
    public ResponseEntity<?> sendRequest(@RequestBody CancelAppointmentDto cancelAppointmentDto){
        HttpVO http = new HttpVO();

        try{
            appointmentService.saveRequest(cancelAppointmentDto);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping("/request/cancel/{roomId}")
    @ApiOperation(value = "피공유자가 예약을 취소하는 요청이 들어왔는지 확인하는 API"
    ,notes ="null 이면 확인 안됨" +
            "\n\t dto 가 존재하면 확인 됨")
    public ResponseEntity<?> checkRequest(@PathVariable int roomId){
        HttpVO http = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

        try{
            data.add(appointmentService.checkRequest(roomId));
            http.setFlag("success");
            http.setData(data);
        } catch (Exception e){
            e.printStackTrace();
        }


        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @DeleteMapping()
    @ApiOperation(value = "예약을 취소하는 API",
    notes = "-- arg --" +
            "\n\t roomId" +
            "\n\t type : ask(1) OR share(2)" +
            "\n\t reason : by share User(1) OR by Not Share User(2)")
    public ResponseEntity<?> cancelAppointment(@RequestParam int roomId,
                                               @RequestParam int reason){
        HttpVO http = new HttpVO();

        try{
            appointmentService.cancelAppointment(roomId,reason);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }


    @GetMapping("/my/point")
    @ApiOperation(value = "나의 포인트 목록을 보여주는 정보를 출력하는 API",
            notes = "reason 데이터가 2이면, 패널티에 의해 되돌려받은 포인트 내역" +
                    "\n\treason 데이터가 1이면, 취소에 의해 되돌려받은 포인트 내역" +
                    "\n\treason 데이터가 0이면 예약에 의해 삭감/증가된 포인트 내역")
    public ResponseEntity<?> getPointList(@RequestParam int userId){
        HttpVO http = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

        try{
            data.add(appointmentService.getPointList(userId));
            http.setData(data);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // 공유자가 만남(예약)을 초기화 하고 싶을 때, 기존 설정한 날짜를 삭제해주는 API
    @PutMapping("/set/period")
    @ApiOperation(value = "공유자가 만남(예약) 중 희망 공유 기간을 잘못 설정해 삭제하고 싶을때, 사용하는 API",
            notes ="-- 필요한 데이터 --" +
                    "\n\t boardId, 현재 채팅을 진행하는 게시글 방 번호" +
                    "\n\t shareUserId, 공유자의 Id" +
                    "\n\t notShareUserId, 피공유자의 Id" +
                    "\n\t type, 공유글에 의한 기간설정인지, 요청글에 의한 기간설정인지" +
                    "1 = 요청글 , 2 = 공유글")
    public ResponseEntity<?> updatePeriod(@RequestBody SetPeriodDto setPeriodDto){
        HttpVO http = new HttpVO();

        try{
            appointmentService.updatePeriod(setPeriodDto);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // 공유자가 만남(예약)의 기간을 설정했는지에 대한 여부를 알려주는 API
    @GetMapping("/set/check")
    @ApiOperation(value = "공유자가 만남(예약) 중 희망 공유 기간 설정할 때 사용하는 API",
            notes = "값이 존재 하면 공유자가 희망 공유기간 설정을 마친 후" +
                    "null면 공유자가 아직 공유기간을 설정하지 않은 것")
    public ResponseEntity<?> checkSet(@RequestParam int boardId,
                                      @RequestParam int shareUserId,
                                      @RequestParam int notShareUserId,
                                      @RequestParam int type){
        HttpVO http = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();
        try{
            data.add(appointmentService.check(boardId, shareUserId, notShareUserId, type));
            http.setData(data);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // 공유자가 희망공유 기간 설정할 때 사용하는 API
    @PostMapping("/set/period")
    @ApiOperation(value = "공유자가 만남(예약) 중 희망 공유 기간 설정할 때 사용하는 API",
    notes ="-- 필요한 데이터 --" +
            "\n\t boardId, 현재 채팅을 진행하는 게시글 방 번호" +
            "\n\t shareUserId, 공유자의 Id" +
            "\n\t notShareUserId, 피공유자의 Id" +
            "\n\t startDay, 시작일" +
            "\n\t endDay, 종료일" +
            "\n\t type, 공유글에 의한 기간설정인지, 요청글에 의한 기간설정인지" +
            "1 = 요청글 , 2 = 공유글")
    public ResponseEntity<?> setPeriod(@RequestBody SetPeriodDto setPeriodDto){
        HttpVO http = new HttpVO();

        try{
            appointmentService.setPeriod(setPeriodDto);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }


    ///////////////////////예약 관련 내용////////////////////////
    @GetMapping("/my/give/{userId}")
    @ApiOperation(value = "내가 빌려준 내역 정보를 요청하는 API",
            notes = "\n\t type = 1 요청 게시글 "+"\n\t type = 2 공유 게시글"
                    )
    public ResponseEntity<?> getGiveList(@PathVariable int userId){
        HttpVO http = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

        try{
            data.add(appointmentService.getGiveList(userId));
            http.setFlag("success");
            http.setData(data);
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping("/check/{boardId}/{type}")
    @ApiOperation(value = "현재 공유 중인지 아닌지에 대한 정보를 요청하는 API",
    notes = "boardId에 boardDetail의 boardId 정보를 넣고 전송하면" +
            "\n\t 현재 날짜와 비교해서 예약중인 boardId면 해당 boardId 값이 return" +
            "\n\t 그렇지 않으면 null 값이 들어온다.")
    public ResponseEntity<?> getBoardState(@PathVariable int boardId, @PathVariable int type){
        HttpVO http = new HttpVO();
        List<Object> data = new ArrayList<>();

        try {
            data.add(appointmentService.getBoardState(boardId, type));
            http.setData(data);
            http.setFlag("success");
            http.setData(data);
        } catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // 내가 공유받은 물품 목록을 볼 수 있다.
    // 한 게시글에서 여러번 물건을 빌렸으면 가장 최근에 빌린 내역만이 나온다.
    @GetMapping("/my/appointlist/{userId}")
    @ApiOperation(value = "나의 약속 정보들을 불러온다." ,
                 notes = "List에 dto 담아서 리턴" +
                         "\n\t state 가 1이면 내가 공유자 입장" +
                         "\n\t state 가 0이면 내가 피공유자 입장")
    public ResponseEntity<?> getMyAppointmentList(@PathVariable int userId){
        HttpVO http = new HttpVO();
        List<Object> data = new ArrayList<>();

        try {
            data.add(appointmentService.getMyAppointmentCalendarList(userId));
//            data.add(appointmentService.getMyAppointmentList(userId));
            http.setFlag("success");
            http.setData(data);
        } catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/my/{userId}")
    @ApiOperation(value = "나의 약속 정보들을 불러온다." ,
            notes = "type 1는 요청 게시글에 의해 공유 받은 예약 정보" +
                    "\n\t type 2는 공유 게시글 의해 공유 받은 예약정보")
    public ResponseEntity<?> getMyAppointmentShare(@PathVariable int userId){
        HttpVO http = new HttpVO();
        List<Object> data = new ArrayList<>();

        try {
            data.add(appointmentService.getMyAppointmentList(userId));
            http.setFlag("success");
            http.setData(data);
        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/{boardId}")
    @ApiOperation(value = "글 번호에 약속 정보들을 불러온다." ,
            notes = "List에 dto 담아서 리턴")
    public ResponseEntity<?> getAppointmentList(@PathVariable int boardId,
                                                @PathVariable int type){
        HttpVO http = new HttpVO();
        List<Object> data = new ArrayList<>();
        try {
            data.add(appointmentService.getAppointmentList(boardId, type));
            http.setFlag("success");
            http.setData(data);
        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping
    @ApiOperation(value = "약속 정보를 저장한다.(만남[예약] 확정)" +
            "\n\t 약속 정보와 함께 해당하는 공유자와 피공유자의 포인트가 추가/삭감된다." +
            "\n\t 그리고 해당하는 공유자가 확정한 날짜 정보를 삭제해준다." ,
            notes = "게시글 정보(boarId)" +
                    "\n\t 약속 기간(appointmentStart, End)" +
                    "\n\t 빌린사람, 빌려주는 사람(notShareUserId, shareUserId) " +
                    "\n\t 현재 날짜(date) 부탁합니다" +
                    "\n\t 요청글에 대한 type인지 share에 의한 type인지도 부탁합니다" +
                    "\n\t 요청은 type = 1, 공유는 type = 2")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDto appointmentDto){
        HttpVO http = new HttpVO();

        try {
            appointmentService.deleteCheck(appointmentDto);
            System.out.println("delete success");

            appointmentService.create(appointmentDto);
            System.out.println("create appointment success");

            appointmentService.addPoint(appointmentDto);
            System.out.println("point decrease/increase success");

            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
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
    @GetMapping("/board/checkroom")
    @ApiOperation(value = "userId와 boardId와 type으로 해당 글에 내 방이 있는지 확인한다.\n (글 읽는사람 시점, 쿼리스트링)")
    public ResponseEntity<?> checkRoom(@RequestParam int userId,
                                       @RequestParam int boardId,
                                       @RequestParam int type) {
        HttpVO http = new HttpVO();
        RoomDto room = appointmentService.checkRoom(userId,boardId,type);
        List<RoomDto> list = new ArrayList<>();
        if(room != null) {
            http.setFlag("success");
            list.add(room);
            http.setData(list);
        } else {
            http.setFlag("fail");
        }
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
    /////////////// //////////// 채팅관련 API ////////////////////////////////
    @DeleteMapping("/chat")
    @ApiOperation(value = "해당하는 채팅을 나가는 API")
    public ResponseEntity<?> deleteChat(@RequestParam("roomId") int roomId, @RequestParam("userId") int userId){
        HttpVO http = new HttpVO();
        try{
            appointmentService.deleteRoom(roomId, userId);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @MessageMapping("/recvchat")
    public ChatVO socketHandler(ChatVO chatVO) throws IOException {
        appointmentService.recvMsg(chatVO);
        HashMap<String, Object> map = new HashMap<>();
        UserDto user = null;
        UserDto user2 = null;
        RoomDto roomDto = appointmentService.getBoard(chatVO.getRoomId());
        int fromUserId = chatVO.getFromUserId();
        int toUserId = chatVO.getToUserId();
        if(chatVO.getFromUserId()==-1) {
            if(roomDto.getNotShareUserId() == toUserId) {
                fromUserId = roomDto.getShareUserId();
            } else {
                fromUserId = roomDto.getNotShareUserId();
            }
        }
        try {
            user = userService.detail2(fromUserId);
            user2 = userService.detail2(fromUserId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        map.put("nickName",user.getNickName());
        map.put("token", user.getToken());
        map.put("content", chatVO.getContent());
        map.put("roomId",chatVO.getRoomId());
        map.put("fromUserId",chatVO.getFromUserId());
        sendingOperations.convertAndSend("/sendlist/"+chatVO.getToUserId(),map);

        map.put("nickName",user2.getNickName());
        sendingOperations.convertAndSend("/sendlist/"+chatVO.getFromUserId(),map);
//        sendingOperations.convertAndSend("/sendmy/"+chatVO.getRoomId()+"/"+chatVO.getFromUserId(),chatVO);
        System.out.println(chatVO);
        System.out.println(chatVO.getToUserId());
        sendingOperations.convertAndSend("/sendchat/"+chatVO.getRoomId()+"/"+chatVO.getToUserId(),chatVO);

        // send FCM
        if(chatVO.isSystem()) {
            fcmDataService.sendChatMessageTo(user.getToken(), chatVO.getRoomId(), user2.getId(), "시스템", chatVO.getContent());
            fcmDataService.sendChatMessageTo(user2.getToken(), chatVO.getRoomId(), user.getId(), "시스템", chatVO.getContent());
        } else {
            fcmDataService.sendChatMessageTo(user2.getToken(), chatVO.getRoomId(), chatVO.getFromUserId(), user.getNickName(), chatVO.getContent());
        }
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
