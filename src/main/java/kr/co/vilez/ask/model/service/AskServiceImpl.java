package kr.co.vilez.ask.model.service;

import kr.co.vilez.ask.model.dao.AskDao;
import kr.co.vilez.ask.model.dto.AskDto;
import kr.co.vilez.ask.model.dto.ImgPath2;
import kr.co.vilez.ask.model.mapper.AskMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AskServiceImpl implements AskService{


    final AskMapper askMapper;
    final AskDao askDao;
    final ResourceLoader resourceLoader;

    @Override
    public List<AskDto> loadAskList(){
        List<AskDto> askDtoList = new ArrayList<>();
        try {
            askDtoList = askMapper.loadAskList();
            for(AskDto askDto : askDtoList){
                askDto.setList(askDao.list(askDto.getId()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return askDtoList;
    }

    @Override
    public List<AskDto> loadMyAskList(int userId){
        List<AskDto> askDtoList = new ArrayList<>();
        try {
            askDtoList = askMapper.loadMyAskList(userId);
            for(AskDto askDto : askDtoList){
                askDto.setList(askDao.list(askDto.getId()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return askDtoList;
    }

    @Override
    public void writeAskBoard(AskDto askDto, MultipartFile[] multipartFiles) {
        try {
            askMapper.writeAskBoard(askDto);
            askDto.setList(new ArrayList<>());
            Resource resource = resourceLoader.getResource("classpath:/static/");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(multipartFiles.length > 0) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);
            System.out.println(current_date);
            // 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
            // 경로 구분자 File.separator 사용
            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
            Resource resource = resourceLoader.getResource("classpath:/static/");
            // 파일을 저장할 세부 경로 지정
            String path = "";
            try {
                path = resource.getURI().getPath() + "images" + "/" + "ask" + "/" + askDto.getId();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(path);
            File file = new File(path);

            // 디렉터리가 존재하지 않을 경우
            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if(!wasSuccessful)
                    System.out.println("file: was not successful");
            }
            for(MultipartFile multipartFile : multipartFiles) {
                String originalFileExtension;
                String contentType = multipartFile.getContentType();

                if(contentType != null){
                    contentType = contentType.toLowerCase();
                }
                // 확장자명이 존재하지 않을 경우 처리 x
                if(ObjectUtils.isEmpty(contentType)) {
                    break;
                } else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
                    if(contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else  // 다른 확장자일 경우 처리 x
                        continue;
                }
                String new_file_name = System.nanoTime() + originalFileExtension;
                ImgPath2 img = new ImgPath2();
                img.setBoardId(askDto.getId());
                img.setPath(path + "/" + new_file_name);
                img.setFileName(multipartFile.getOriginalFilename());
                file = new File(path + "/"+ new_file_name);

                try {
                    multipartFile.transferTo(file);
                    askDao.insert(img);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // 파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
                askDto.getList().add(img);
            }
        }
    }

    @Override
    public void updateAskBoard(AskDto askDto, MultipartFile[] multipartFiles) {
        try {
            askMapper.updateAskBoard(askDto);
            askDto.setList(new ArrayList<>());
            askDao.delete(askDto.getId());
            Resource resource = resourceLoader.getResource("classpath:/static/");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(multipartFiles.length > 0) {
            LocalDateTime now = LocalDateTime.now();

            // 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
            // 경로 구분자 File.separator 사용
            Resource resource = resourceLoader.getResource("classpath:/static/");
            // 파일을 저장할 세부 경로 지정
            String path = "";
            try {
                path = resource.getURI().getPath() + "images" + "/" + "ask" + "/" + askDto.getId();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(path);
            File file = new File(path);

            // 디렉터리가 존재하지 않을 경우
            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if(!wasSuccessful)
                    System.out.println("file: was not successful");
            }
            for(MultipartFile multipartFile : multipartFiles) {
                String originalFileExtension;
                String contentType = multipartFile.getContentType();
                if(contentType != null){
                    contentType = contentType.toLowerCase();
                }
                // 확장자명이 존재하지 않을 경우 처리 x
                if(ObjectUtils.isEmpty(contentType)) {
                    break;
                } else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
                    if(contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else  // 다른 확장자일 경우 처리 x
                        continue;
                }
                String new_file_name = System.nanoTime() + originalFileExtension;
                ImgPath2 img = new ImgPath2();
                img.setBoardId(askDto.getId());
                img.setPath(path + "/" + new_file_name);
                img.setFileName(multipartFile.getOriginalFilename());
                file = new File(path + "/"+ new_file_name);

                try {
                    multipartFile.transferTo(file);
                    askDao.insert(img);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // 파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
                askDto.getList().add(img);
            }
        }
    }

    @Override
    public void deleteArticle(int boardId) {
        try {
            askMapper.deleteArticle(boardId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AskDto detailArticle(int boardId) {
        AskDto askDto = null;
        try {
            askDto = askMapper.detailArticle(boardId);
            askDto.setList(askDao.list(boardId));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return askDto;
    }
}
