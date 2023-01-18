package kr.co.vilez.ask.model.dao;

import kr.co.vilez.ask.model.dto.ImgPath2;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class AskDao {

    final MongoTemplate mongoTemplate;

    public void insert(ImgPath2 img) {
        mongoTemplate.insert(img);
    }

    public void delete(int boardId) {
        mongoTemplate.remove(Query.query(Criteria.where("boardId").is(boardId)),ImgPath2.class);
    }

    public ImgPath2 select(int boardId, String fileName) {
        ImgPath2 img = mongoTemplate.findOne(Query.query(Criteria.where("boardId").is(boardId)
                .and("fileName").is(fileName)),ImgPath2.class);
        return img;
    }

    public List<ImgPath2> list(int boardId) {
        List<ImgPath2> imgs = mongoTemplate.find(Query.query(Criteria.where("boardId").is(boardId)),
                ImgPath2.class);
        return imgs;
    }


}
