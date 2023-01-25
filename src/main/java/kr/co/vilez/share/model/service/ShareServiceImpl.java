package kr.co.vilez.share.model.service;

import kr.co.vilez.ask.model.dto.ImgPath2;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.share.model.dto.*;
import kr.co.vilez.share.model.dao.ShareDao;
import kr.co.vilez.share.model.mapper.ShareMapper;
import kr.co.vilez.tool.OSUpload;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService{
    HttpVO httpVO = null;

    final ShareDao shareDao;
    final ShareMapper shareMapper;

    final OSUpload osUpload;

    final String bucketName = "vilez";
    @Override
    public HttpVO bookmarkList(int boardId) throws Exception {
        httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        List<BookmarkDto> bookmarkDtos = shareDao.selectBookmarkList(boardId);
        data.add(bookmarkDtos);

        httpVO.setData(data);
        return httpVO;
    }

    @Override
    public HttpVO isBookmark(int boardId, int userId) throws Exception {
        httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        BookmarkDto bookmarkDto = shareDao.selectBookmark(boardId, userId);
        data.add(bookmarkDto);

        httpVO.setData(data);

        return httpVO;
    }

    @Override
    public HttpVO bookmark(int boardId, int userId, String state) throws Exception {
        httpVO = new HttpVO();

        if(state.equals("on")){
            shareDao.deleteBookmark(boardId, userId);
        } else{
            shareDao.insertBookmark(boardId, userId);
        }

        return httpVO;
    }

    @Override
    public HttpVO loadMyShareList(int userId) throws Exception {
        httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        List<ShareListDto> shareListDtos = shareMapper.loadMyShareList(userId);
        for(ShareListDto shareListDto : shareListDtos){
            shareListDto.setList(shareDao.list(shareListDto.getId()));
        }

        data.add(shareListDtos);
        httpVO.setData(data);

        return httpVO;
    }

    @Override
    public HttpVO detail(int boardId) throws Exception {
        httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        ShareDto shareDto = shareMapper.detailArticle(boardId);
        List<ImgPath> imgPaths = shareDao.list(boardId);
        shareDto.setBookmarkCnt(shareDao.countBookMark(boardId));
        shareDto.setList(imgPaths);
        data.add(shareDto);

        httpVO.setData(data);

        return httpVO;
    }

    public ShareDto saveFiles(ShareDto shareDto, List<MultipartFile> multipartFiles) throws Exception {
        String folderName = "share/"+shareDto.getId()+"/";
        if(multipartFiles.size() > 0) {

            for(MultipartFile multipartFile : multipartFiles) {


                File uploadFile = osUpload.convert(multipartFile)        // 파일 생성
                        .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));

                String fileName = folderName + System.nanoTime() + uploadFile.getName();

                osUpload.put(bucketName,fileName,uploadFile);

                ImgPath imgPath = new ImgPath();
                imgPath.setBoardId(shareDto.getId());
                imgPath.setPath("https://kr.object.ncloudstorage.com/"+bucketName+"/"+fileName);
                imgPath.setFileName(multipartFile.getOriginalFilename());

                shareDao.insert(imgPath);
                shareDto.getList().add(imgPath);
            }
        }
        return shareDto;
    }

    @Override
    public HttpVO delete(@RequestParam int boardId) throws Exception {
        httpVO = new HttpVO();

        shareMapper.delete(boardId);

        return httpVO;
    }

    @Override
    public HttpVO update(ShareDto shareDto, List<MultipartFile> multipartFiles) throws Exception {
        httpVO = new HttpVO();
        shareDto.setList(new ArrayList<>());
        shareMapper.update(shareDto);
        shareDao.delete(shareDto.getId());
        shareDto.setList(new ArrayList<>());
        if(!multipartFiles.get(0).getOriginalFilename().equals("")) {
            shareDto = saveFiles(shareDto, multipartFiles);
        }
        httpVO.setFlag("success");
        List<ShareDto> data = new ArrayList<>();
        data.add(shareDto);
        httpVO.setData(data);
        return httpVO;
    }

    @Override
    public HttpVO insert(ShareDto shareDto, List<MultipartFile> files) throws Exception {
        httpVO = new HttpVO();

        shareMapper.insert(shareDto);
        shareDto.setList(new ArrayList<>());
        if(!files.get(0).getOriginalFilename().equals("")) {
            shareDto = saveFiles(shareDto, files);
        }
        httpVO.setFlag("success");
        List<ShareDto> data = new ArrayList<>();
        data.add(shareDto);
        httpVO.setData(data);

        return httpVO;
    }

    @Override
    public HttpVO addBookmark(int boardId, int userId) {
        shareDao.insertBookmark(boardId,userId);
        httpVO = new HttpVO();
        httpVO.setFlag("success");
        return httpVO;
    }

    @Override
    public HttpVO deleteBookmark(int boardId, int userId) {
        shareDao.deleteBookmark(boardId,userId);
        httpVO = new HttpVO();
        httpVO.setFlag("success");
        return httpVO;
    }

    @Override
    public HttpVO loadShareList(PageNavigator pageNavigator) throws Exception {
        httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        if(pageNavigator.getWord() != null && pageNavigator.getWord() != ""){
            pageNavigator.setWord("%"+pageNavigator.getWord()+"%");
        }

        int tmp = pageNavigator.getHigh();
        pageNavigator.setHigh(pageNavigator.getHigh() * pageNavigator.getCnt());
        pageNavigator.setLow(tmp);
        List<ShareListDto> list = shareMapper.loadShareList(pageNavigator);

        for(ShareListDto shareListDto : list){
            TotalListShare totalListShare = new TotalListShare();

            List<ImgPath> imgPaths = shareDao.list(shareListDto.getId());
            shareListDto.setList(imgPaths);
            List<BookmarkDto> bookmarkList = shareDao.selectBookmarkList(shareListDto.getId());

            totalListShare.setShareListDto(shareListDto);
            totalListShare.setList(bookmarkList);
            data.add(totalListShare);
        }

        httpVO.setData(data);
        return httpVO;
    }
}
