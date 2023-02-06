package kr.co.vilez.back.model.dao;

import kr.co.vilez.appointment.model.dto.SetPeriodDto;
import kr.co.vilez.ask.model.dto.ImgPath2;
import kr.co.vilez.back.model.dto.ReturnRequestDto;
import kr.co.vilez.back.model.vo.RoomStatusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class BackDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void insertRoomStatus(RoomStatusVO roomStatusVO) {
        mongoTemplate.insert(roomStatusVO);
    }

    public RoomStatusVO selectRoomStatus(int roomId) {
        RoomStatusVO roomStatusVO = mongoTemplate.findOne(Query.query(Criteria.where("roomId").is(roomId)
                ),RoomStatusVO.class);
        return roomStatusVO;
    }

    public void deleteReturnRequest(int roomId){
        mongoTemplate.remove(Query.query(Criteria.where("roomId").is(roomId)
                ),
                ReturnRequestDto.class);
    }

    public void insert(ReturnRequestDto returnRequestDto) {
        mongoTemplate.insert(returnRequestDto);
    }

    public ReturnRequestDto selectRequest(int roomId){
        ReturnRequestDto back = mongoTemplate.findOne(Query.query(
                Criteria.where("roomId").is(roomId)), ReturnRequestDto.class);
        return back;
    }
}
