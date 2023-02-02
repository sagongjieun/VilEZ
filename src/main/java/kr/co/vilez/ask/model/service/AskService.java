package kr.co.vilez.ask.model.service;

import kr.co.vilez.ask.model.dto.AskDto;
import kr.co.vilez.share.model.dto.PageNavigator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AskService {

    List<AskDto> loadAskList(PageNavigator pageNavigator);

    List<AskDto> loadMyAskList(int userId);

    void writeAskBoard(AskDto askDto , MultipartFile[] multipartFiles) throws IOException;

    void updateAskBoard(AskDto askDto, MultipartFile[] files) throws IOException;

    void deleteArticle(int boardId);

    AskDto detailArticle(int boardId);


}
