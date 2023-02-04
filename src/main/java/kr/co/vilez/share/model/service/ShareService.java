package kr.co.vilez.share.model.service;

import kr.co.vilez.data.HttpVO;
import kr.co.vilez.share.model.dto.PageNavigator;
import kr.co.vilez.share.model.dto.ShareDto;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ShareService {
    HttpVO getBestList(String category, int userId, int boardId) throws Exception;
    HttpVO loadMyShareList(int boardId) throws Exception;
    HttpVO loadShareList(PageNavigator pageNavigator) throws Exception;
    HttpVO delete(int boardId) throws Exception;
    HttpVO update(ShareDto shareDto, List<MultipartFile> files) throws Exception;
    HttpVO bookmarkList(int boardId) throws Exception;
    HttpVO isBookmark(int boardId, int userId) throws Exception;
    HttpVO bookmark(int boardId, int userId, String state) throws Exception;
    HttpVO detail(int boardId) throws Exception;
    HttpVO insert(ShareDto shareDto, List<MultipartFile> files) throws Exception;

    HttpVO addBookmark(int boardId, int userId);

    HttpVO deleteBookmark(int boardId, int userId);

    HttpVO myBookmarkList(int userId) throws Exception;

//    HttpVO boardList() throws Exception;

}
