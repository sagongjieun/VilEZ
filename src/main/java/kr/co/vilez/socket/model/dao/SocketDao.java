package kr.co.vilez.socket.model.dao;

import kr.co.vilez.socket.model.vo.RoomVO;
import kr.co.vilez.socket.model.vo.SocketVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class SocketDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveMsg(SocketVO socketVO) {
        mongoTemplate.save(new SocketVO(socketVO.getRoomId(),socketVO.getType(),socketVO.getContent(),
                socketVO.getLat(), socketVO.getLng(), socketVO.getLevel()));
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
    public SocketVO loadLocationByRoomId(String roomId) {
        SocketVO msg = mongoTemplate.findOne(
                Query.query(Criteria.where("roomId").is(roomId)
                        .and("type").is("2")),
                SocketVO.class
        );
        return msg;
    }

    // roomId에 저장된 좌표값을 삭제한다.
    public void deleteLocation(String roomId) {
        SocketVO msgs = mongoTemplate.findOne(
                Query.query(Criteria.where("roomId").is(roomId).
                        and("type").is("2")),
                SocketVO.class
        );
        if(msgs != null)
            mongoTemplate.remove(Query.query(Criteria.where("roomId").is(roomId).
                and("type").is("2")),SocketVO.class);
    }

    public void createRoom(RoomVO roomVO) {
        mongoTemplate.insert(roomVO);
    }
}
