package kr.co.vilez.share.model.service;

import kr.co.vilez.data.HttpVO;
import kr.co.vilez.share.model.dto.PageNavigator;
import kr.co.vilez.share.model.dto.ShareDto;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ShareService {
    HttpVO loadMyShareList(int boardId) throws Exception;
    HttpVO loadShareList(PageNavigator pageNavigator) throws Exception;
    HttpVO delete(int boardId) throws Exception;
    HttpVO update(ShareDto shareDto, List<MultipartFile> files) throws Exception;
    HttpVO bookmarkList(String boardId) throws Exception;
    HttpVO isBookmark(String boardId, String userId) throws Exception;
    HttpVO bookmark(String boardId, String userId, String state) throws Exception;
    HttpVO detail(String boardId) throws Exception;
    HttpVO insert(ShareDto shareDto, List<MultipartFile> files) throws Exception;

//    HttpVO boardList() throws Exception;

}
