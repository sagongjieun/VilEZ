package kr.co.vilez.appointment.model.dao;

import kr.co.vilez.appointment.model.vo.ChatNoReadVO;
import kr.co.vilez.appointment.model.vo.ChatVO;
import kr.co.vilez.appointment.model.vo.MapVO;
import kr.co.vilez.appointment.model.vo.RoomVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<ChatVO> loadMsgByRoomId(String roomId) {
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


    public void createRoom(RoomVO roomVO) {
        mongoTemplate.insert(roomVO);
    }

    public void saveNoReadMsg(ChatNoReadVO chatNoReadVO) {
        mongoTemplate.insert(chatNoReadVO);
    }

    public void recvHereMsg(ChatVO chatVO) {
        mongoTemplate.findOne(Query.query(Criteria.where("roomId").is(chatVO.getRoomId())
                                .and("type").is(chatVO.getType())),ChatNoReadVO.class);

    }
}
