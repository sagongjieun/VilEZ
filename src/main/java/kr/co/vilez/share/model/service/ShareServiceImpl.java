package kr.co.vilez.share.model.service;

import kr.co.vilez.data.HttpVO;
import kr.co.vilez.share.model.dto.*;
import kr.co.vilez.share.model.dao.ShareDao;
import kr.co.vilez.share.model.mapper.ShareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShareServiceImpl implements ShareService{
    HttpVO httpVO = null;

    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    ShareDao shareDao;
    @Autowired
    ShareMapper shareMapper;

    @Override
    public HttpVO bookmarkList(String boardId) throws Exception {
        httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        List<BookmarkDto> bookmarkDtos = shareDao.selectBookmarkList(boardId);
        data.add(bookmarkDtos);

        httpVO.setData(data);
        return httpVO;
    }

    @Override
    public HttpVO isBookmark(String boardId, String userId) throws Exception {
        httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        BookmarkDto bookmarkDto = shareDao.selectBookmark(boardId, userId);
        data.add(bookmarkDto);

        httpVO.setData(data);

        return httpVO;
    }

    @Override
    public HttpVO bookmark(String boardId, String userId, String state) throws Exception {
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
    public HttpVO detail(String boardId) throws Exception {
        httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        ShareDto shareDto = shareMapper.detailArticle(boardId);
        List<ImgPath> imgPaths = shareDao.list(Integer.parseInt(boardId));

        shareDto.setList(imgPaths);
        data.add(shareDto);

        httpVO.setData(data);

        return httpVO;
    }

    public void saveFiles(int boardId, MultipartFile file) throws Exception {
        ImgPath imgPath = new ImgPath();

        String [] formats = {".jpeg", ".png", ".bmp", ".jpg"};
        // 원래 파일 이름 추출
        String origName = file.getOriginalFilename();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));
        extension = extension.toLowerCase();
        Resource resource = resourceLoader.getResource("classpath:/static/");
        // 파일을 저장할 세부 경로 지정
        String path = resource.getURI().getPath()
                + "images"+ "/" +
                "share" + "/" +
                boardId;

        File file1 = new File(path);
        // 디렉터리가 존재하지 않을 경우
        if(!file1.exists()) {
            boolean wasSuccessful = file1.mkdirs();

            // 디렉터리 생성에 실패했을 경우
            if(!wasSuccessful)
                System.out.println("file: was not successful");
        }

        for(int i = 0; i < formats.length; i++) {
            if (extension.equals(formats[i])){
                // user email과 확장자 결합
                String new_file_name = System.nanoTime() + extension;
                // 파일을 불러올 때 사용할 파일 경로
                String savedPath = resource.getURI().getPath() +
                        "images" + "/" +
                        "share" + "/"
                        + boardId + "/"
                        + new_file_name;

                imgPath.setBoardId(boardId);
                imgPath.setPath(savedPath);
                imgPath.setFileName(origName);
//              shareMapper.saveFiles(imgPath);

                shareDao.insert(imgPath);

                System.out.println("insert DAO");

                file.transferTo(new File(savedPath));
                break;
            }
        }
    }

    @Override
    public HttpVO delete(@RequestParam int boardId) throws Exception {
        httpVO = new HttpVO();

        shareMapper.delete(boardId);

        return httpVO;
    }

    @Override
    public HttpVO update(ShareDto shareDto, List<MultipartFile> files) throws Exception {
        httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();

        shareMapper.update(shareDto);
        shareDao.delete(shareDto.getId());

        if(!files.get(0).getOriginalFilename().equals("")) {
            for (MultipartFile file : files) {
                saveFiles(shareDto.getId(), file);
            }
        }
        return httpVO;
    }

    @Override
    public HttpVO insert(ShareDto shareDto, List<MultipartFile> files) throws Exception {
        httpVO = new HttpVO();

        shareMapper.insert(shareDto);
        int boardId = shareDto.getId();

        if(!files.get(0).getOriginalFilename().equals("")) {
            for (MultipartFile file : files) {
                saveFiles(boardId, file);
            }
        }
        httpVO.setFlag("success");
        return httpVO;
    }

    @Override
    public HttpVO loadShareList() throws Exception {
        httpVO = new HttpVO();
        List<Object> data = new ArrayList<>();
        List<ShareListDto> list = shareMapper.loadShareList();

        for(ShareListDto shareListDto : list){
            TotalListShare totalListShare = new TotalListShare();

            List<ImgPath> imgPaths = shareDao.list(shareListDto.getId());
            shareListDto.setList(imgPaths);
            List<BookmarkDto> bookmarkList = shareDao.selectBookmarkList(Integer.toString(shareListDto.getId()));

            totalListShare.setShareListDto(shareListDto);
            totalListShare.setList(bookmarkList);
            data.add(totalListShare);
        }

        httpVO.setData(data);
        return httpVO;
    }
}
