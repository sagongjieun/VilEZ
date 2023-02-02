package kr.co.vilez.ask.model.mapper;

import kr.co.vilez.ask.model.dto.AskDto;
import kr.co.vilez.share.model.dto.PageNavigator;
import org.apache.ibatis.annotations.Mapper;
import java.sql.SQLException;
import java.util.List;

@Mapper
public interface AskMapper {
    List<AskDto> loadAskList(PageNavigator pageNavigator) throws SQLException;

    List<AskDto> loadMyAskList(int userId) throws SQLException;

    void writeAskBoard(AskDto askDto) throws SQLException;

    void updateAskBoard(AskDto askDto) throws SQLException;

    void deleteArticle(int boardId) throws SQLException;

    AskDto detailArticle(int boardId) throws SQLException;
}
