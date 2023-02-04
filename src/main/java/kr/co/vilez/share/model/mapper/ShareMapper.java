package kr.co.vilez.share.model.mapper;

import kr.co.vilez.share.model.dto.PageNavigator;
import kr.co.vilez.share.model.dto.ShareDto;
import kr.co.vilez.share.model.dto.ShareListDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface ShareMapper {

    List<ShareListDto> getBestList(PageNavigator pageNavigator) throws SQLException;
    List<ShareListDto> loadMyShareList(int userId) throws SQLException;
    List<ShareListDto> loadShareList(PageNavigator pageNavigator) throws SQLException;
    void delete(int boardId) throws SQLException;
    void update(ShareDto shareDto) throws SQLException;
    ShareDto detailArticle(int boardId) throws SQLException;
    int insert(ShareDto shareDto) throws SQLException;

    List<ShareListDto> getShareBoardList() throws SQLException;
//    List<ShareListDto> getList() throws SQLException;
}
