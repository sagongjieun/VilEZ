package kr.co.vilez.sign.model.dao;

import kr.co.vilez.share.model.dto.BookmarkDto;
import kr.co.vilez.share.model.dto.ImgPath;
import kr.co.vilez.share.model.dto.InterestingDto;
import kr.co.vilez.sign.model.dto.SignImg;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Queue;

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
                .and("notUserId")
                .is(signImg.getNotUserId())
        ), SignImg.class);
    }
    public SignImg loadContract(SignImg signImg) {
        SignImg msgs = mongoTemplate.findOne(Query.query(Criteria.where("boardId").is(signImg.getBoardId())
                .and("shareUserId")
                .is(signImg.getShareUserId())
                .and("notUserId")
                .is(signImg.getNotUserId())
        ), SignImg.class);
        return msgs;
    }
}
