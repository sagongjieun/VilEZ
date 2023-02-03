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
    public void delete(SignImg signImg) {
        mongoTemplate.remove(Query.query(Criteria.where("boardId").is(signImg.getBoardId())
                .and("shareUserId")
                .is(signImg.getShareUserId())
                .and("notShareUserId")
                .is(signImg.getNotShareUserId())
        ), SignImg.class);
    }
    public SignImg loadContract(SignImg signImg) {
        SignImg msgs = mongoTemplate.findOne(Query.query(Criteria.where("boardId").is(signImg.getBoardId())
                .and("shareUserId")
                .is(signImg.getShareUserId())
                .and("notUserId")
                .is(signImg.getNotShareUserId())
        ), SignImg.class);
        return msgs;
    }
}
