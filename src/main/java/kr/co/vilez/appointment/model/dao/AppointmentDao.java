package kr.co.vilez.appointment.model.dao;

import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class AppointmentDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveLocation(MapVO mapVO) {
        MapVO msgs = mongoTemplate.findOne(
                Query.query(Criteria.where("roomId").is(mapVO.getRoomId()).
                        and("isMarker").is(true)),
                MapVO.class
        );
        if(msgs != null && !mapVO.isMarker)  {
            return;
        }
        mongoTemplate.remove(Query.query(Criteria.where("roomId").is(mapVO.getRoomId())), MapVO.class);
        mongoTemplate.save(mapVO);
    }

    public void deleteRoom(String roomId) {
        mongoTemplate.remove(Query.query(Criteria.where("roomId").is(roomId)),
                ChatVO.class);
    }

    public List<ChatVO> loadMsgByRoomId(int roomId) {
        List<ChatVO> msgs = mongoTemplate.find(
                Query.query(Criteria.where("roomId").is(roomId)),
                ChatVO.class
        );
        return msgs;
    }

    //roomId에 저장된 좌표값을 불러온다.
    public MapVO loadLocationByRoomId(String roomId) {
        MapVO msg = mongoTemplate.findOne(
                Query.query(Criteria.where("roomId").is(roomId)),
                MapVO.class
        );
        return msg;
    }

    public void saveNoReadMsg(ChatNoReadVO chatNoReadVO) {
        mongoTemplate.insert(chatNoReadVO);
    }

    public void recvHereMsg(ChatNoReadVO chatNoReadVO) {
        mongoTemplate.remove(chatNoReadVO);
    }


    public ChatDatasVO first(int userId) {
        // 최종 리턴 시킬 전체 목록이다.
        ChatDatasVO chatDatasVO = new ChatDatasVO();
        // user의 안읽은 메세지를 찾는다.
//        List<ChatNoReadVO> chatNoReadVOList = mongoTemplate.find(Query.query(Criteria.where("toUserId").is(userId)),ChatNoReadVO.class);
//        // 룸 번호의 배열에 저장해둔다.
//        LinkedHashMap<Integer, ChatLastVO> chatNoReadByRoomId = new LinkedHashMap<>();
//        Collections.reverse(chatNoReadVOList);
//        for(ChatNoReadVO chat : chatNoReadVOList) {
//            if(!chatNoReadByRoomId.containsKey(chat.getRoomId())) {
//                ChatLastVO chatLastVO = new ChatLastVO();
//                chatLastVO.setCount(0);
//                chatLastVO.setContent(chat.getContent());
//                chatNoReadByRoomId.put(chat.getRoomId(),chatLastVO);
//            }
//            ChatLastVO chatLastVO =  chatNoReadByRoomId.get(chat.getRoomId());
//            chatLastVO.setCount(chatLastVO.getCount() + 1);
//            chatLastVO.setRoomId(chat.getRoomId());
//            chatNoReadByRoomId.put(chat.getRoomId(),chatLastVO);
//        }
//        chatDatasVO.setNoReadCount(chatNoReadVOList.size());
//        List<ChatVO> chatList = mongoTemplate.find(Query.query(Criteria.where("").orOperator(Criteria.where("toUserId").is(userId),Criteria.where("fromUserId").is(userId))),ChatVO.class);
//        Collections.reverse(chatList);
//        for(ChatVO chatVO : chatList) {
//            if(chatNoReadByRoomId.containsKey(chatVO.getRoomId())) {
//                continue;
//            }
//            ChatLastVO chatLastVO = new ChatLastVO();
//            chatLastVO.setContent(chatVO.getContent());
//            chatLastVO.setRoomId(chatVO.getRoomId());
//            chatNoReadByRoomId.put(chatVO.getRoomId(),chatLastVO);
//        }
//        ArrayList<ChatLastVO> datas = new ArrayList<>();
//        for (int roomId : chatNoReadByRoomId.keySet()) {
//            ChatLastVO chatLastVO = chatNoReadByRoomId.get(roomId);
//
//            // 상대방 닉네임과 지역찾아보기
//            chatLastVO.setTitle("임시 제목입니다. todo");
//            datas.add(chatLastVO);
//        }
//        chatDatasVO.setUserId(userId);
//        chatDatasVO.setChatData(datas);
        return chatDatasVO;
    }
    public void saveMsg(ChatVO chatVO) {
        mongoTemplate.insert(chatVO);
    }

    public void deleteUserEnterMsg(int roomId, int userId) {
        mongoTemplate.remove(Query.query(Criteria.where("roomId").is(roomId).and("userId").is(userId)),
                            UserEnterVO.class);
    }

    public void addUserEnterMsg(UserEnterVO userEnterVO) {
        mongoTemplate.insert(userEnterVO);
    }

    public void saveLastMsg(ChatLastVO chatLastVO) {
        mongoTemplate.remove(Query.query(Criteria.where("roomId").is(chatLastVO.getRoomId())), ChatLastVO.class);
        mongoTemplate.insert(chatLastVO);
    }

    public List<ChatLastVO> getChatLastVOList(int userId) {
        return mongoTemplate.find(Query.query(
                Criteria.where("").orOperator
                        (Criteria.where("fromUserId").is(userId),Criteria.where("toUserId").is(userId))
        ),ChatLastVO.class);
    }

    public Long getChatCount(int roomId, Long time) {
        return mongoTemplate.count(Query.query(Criteria.where("roomId").is(roomId).and("time").gt(time)),ChatVO.class);
    }

    public List<UserEnterVO> getUserEnterVOList(int userId) {
        return mongoTemplate.find(Query.query(Criteria.where("userId").is(userId)), UserEnterVO.class);
    }
}
