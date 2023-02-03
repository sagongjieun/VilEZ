package kr.co.vilez.ask.model.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.co.vilez.ask.model.dao.AskDao;
import kr.co.vilez.ask.model.dto.AskDto;
import kr.co.vilez.ask.model.dto.AskList;
import kr.co.vilez.ask.model.dto.ImgPath2;
import kr.co.vilez.ask.model.mapper.AskMapper;
import kr.co.vilez.configuration.NaverObjectStorageConfig;
import kr.co.vilez.share.model.dto.PageNavigator;
import kr.co.vilez.tool.OSUpload;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AskServiceImpl implements AskService{
    final AskMapper askMapper;
    final AskDao askDao;
    final OSUpload osUpload;
    final String bucketName = "vilez";

    @Override
    public ArrayList<AskList> loadAskList(PageNavigator pageNavigator) throws Exception{
        List<AskDto> askDtoList = new ArrayList<>();

        if(pageNavigator.getWord() != null && pageNavigator.getWord() != ""){
            pageNavigator.setWord("%"+pageNavigator.getWord()+"%");
        }

        int tmp = pageNavigator.getHigh();
        pageNavigator.setHigh(pageNavigator.getHigh() * pageNavigator.getCnt());
        pageNavigator.setLow(tmp);

        ArrayList<AskList> list = new ArrayList<>();

        askDtoList = askMapper.loadAskList(pageNavigator);


        for(AskDto askDto : askDtoList) {
            AskList askList = new AskList();
            askDto.setList(askDao.list(askDto.getId()));
            askList.setAskDto(askDto);
            list.add(askList);
        }

        return list;
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
    public void writeAskBoard(AskDto askDto, MultipartFile[] multipartFiles) throws IOException {
        try {
            askMapper.writeAskBoard(askDto);
            askDto.setList(new ArrayList<>());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // create folder
        String folderName = "ask/"+askDto.getId()+"/";
        if(multipartFiles.length > 0) {
            osUpload.mkdir(bucketName,folderName);
            for(MultipartFile multipartFile : multipartFiles) {


                File uploadFile = osUpload.convert(multipartFile)        // 파일 생성
                        .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));

                String fileName = folderName + System.nanoTime() + uploadFile.getName();

                ImgPath2 img = new ImgPath2();
                img.setBoardId(askDto.getId());
                img.setPath("https://kr.object.ncloudstorage.com/"+bucketName+"/"+fileName);
                img.setFileName(multipartFile.getOriginalFilename());
                System.out.println(img);
                askDao.insert(img);
                askDto.getList().add(img);
                osUpload.put(bucketName,fileName,uploadFile);
            }
        }
    }

    @Override
    public void updateAskBoard(AskDto askDto, MultipartFile[] multipartFiles) throws IOException{
        try {
            askMapper.updateAskBoard(askDto);
            askDto.setList(new ArrayList<>());
            askDao.delete(askDto.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // create folder
        String folderName = "ask/"+askDto.getId()+"/";
        if(multipartFiles.length > 0) {
            osUpload.mkdir(bucketName,folderName);
            for(MultipartFile multipartFile : multipartFiles) {

                File uploadFile = osUpload.convert(multipartFile)        // 파일 생성
                        .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));

                String fileName = folderName + System.nanoTime() + uploadFile.getName();


                ImgPath2 img = new ImgPath2();
                img.setBoardId(askDto.getId());
                img.setPath("https://kr.object.ncloudstorage.com/"+bucketName+"/"+fileName);
                img.setFileName(multipartFile.getOriginalFilename());
                System.out.println(img);
                askDao.insert(img);
                askDto.getList().add(img);
                osUpload.put(bucketName,fileName,uploadFile);
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
