package kr.co.vilez.share.model.dao;

import kr.co.vilez.ask.model.dto.ImgPath2;
import kr.co.vilez.share.model.dto.BookmarkDto;
import kr.co.vilez.share.model.dto.ImgPath;
import kr.co.vilez.share.model.dto.InterestingDto;
import kr.co.vilez.socket.model.vo.SocketVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShareDao  {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<InterestingDto> loadBoardInteresting(int boardId) {
        List<InterestingDto> msgs = mongoTemplate.find(
                Query.query(Criteria.where("board_id").is(boardId)),
                InterestingDto.class
        );
        return msgs;
    }

    public void insert(ImgPath img) {
        mongoTemplate.insert(img);
    }

    public void delete(int boardId) {
        mongoTemplate.remove(Query.query(Criteria.where("boardId").is(boardId)), ImgPath.class);
    }

    public BookmarkDto selectBookmark(String boardId, String userId){
        BookmarkDto bookmark = mongoTemplate.findOne(Query.query(Criteria.where("boardId").is(boardId)
                .and("userId").is(userId)),BookmarkDto.class);
        return bookmark;
    }

    public  List<BookmarkDto> selectBookmarkList(String boardId) {
        List<BookmarkDto> bookmarkDtos = mongoTemplate.find(
                Query.query(Criteria.where("boardId").is(boardId)),
                BookmarkDto.class
        );
        return bookmarkDtos;
    }

    public void insertBookmark(String boardId, String userId){
        BookmarkDto bookmarkDto = new BookmarkDto(boardId, userId);
        mongoTemplate.insert(bookmarkDto);
    }

    public void deleteBookmark(String boardId, String userId) {
        mongoTemplate.remove(Query.query(Criteria.where("boardId").is(boardId)
                .and("userId").is(userId)), BookmarkDto.class);
    }

    public ImgPath select(int boardId, String fileName) {
        ImgPath img = mongoTemplate.findOne(Query.query(Criteria.where("boardId").is(boardId)
                .and("fileName").is(fileName)),ImgPath.class);
        return img;
    }

    public List<ImgPath> list(int boardId) {
        List<ImgPath> imgs = mongoTemplate.find(Query.query(Criteria.where("boardId").is(boardId)),
                ImgPath.class);
        return imgs;
    }

}
