package kr.co.vilez.appointment.model.dao;

import kr.co.vilez.appointment.model.vo.MapVO;
import kr.co.vilez.appointment.model.vo.RoomVO;
import kr.co.vilez.appointment.model.vo.SocketVO;
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
                SocketVO.class);
    }

    public List<SocketVO> loadMsgByRoomId(String roomId) {
        List<SocketVO> msgs = mongoTemplate.find(
                Query.query(Criteria.where("roomId").is(roomId)),
                SocketVO.class
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
}
