package kr.co.vilez.sign.model.dao;


import kr.co.vilez.sign.model.dto.SignImg;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class SignDao {
    final private MongoTemplate mongoTemplate;
    public void insert(SignImg img) {
        mongoTemplate.insert(img);
    }
    public void delete(int roomId) {
        mongoTemplate.remove(Query.query(Criteria.where("roomId").is(roomId)
        ), SignImg.class);
    }
    public SignImg loadContract(int roomId) {
        SignImg msgs = mongoTemplate.findOne(Query.query(Criteria.where("roomId").is(roomId)
        ), SignImg.class);
        return msgs;
    }
}
