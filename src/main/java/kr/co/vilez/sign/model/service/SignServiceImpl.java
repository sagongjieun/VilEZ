package kr.co.vilez.sign.model.service;

import kr.co.vilez.sign.model.dao.SignDao;
import kr.co.vilez.sign.model.dto.SignImg;
import kr.co.vilez.tool.OSUpload;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RequiredArgsConstructor
@Service
public class SignServiceImpl implements SignService {

    final SignDao signDao;
    final OSUpload osUpload;
    String bucketName = "vilez";

    @Override
    public SignImg getContract(SignImg signImg) throws Exception {
        return signDao.loadContract(signImg);
    }

    @Override
    public SignImg signUpload(SignImg signImg, MultipartFile multipartFile) throws Exception {
        if(multipartFile.isEmpty()){
            String basicPath = "https://kr.object.ncloudstorage.com/vilez/basicProfile.png";
            signImg.setPath(basicPath);
        } else{
            String [] formats = {".jpeg", ".png", ".bmp", ".jpg"};
            // 원래 파일 이름 추출
            String origName = multipartFile.getOriginalFilename();

            // 확장자 추출(ex : .png)
            String extension = origName.substring(origName.lastIndexOf("."));
            String folderName = "sign";
            for(int i = 0; i < formats.length; i++) {
                if (extension.equals(formats[i])){
                    File uploadFile = osUpload.convert(multipartFile)        // 파일 생성
                            .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));

//                osUpload.mkdir(bucketName, folderName);

                    String fileName = folderName + "/" + System.nanoTime() + extension;
                    osUpload.put(bucketName, fileName, uploadFile);

                    String path = "https://kr.object.ncloudstorage.com/"+bucketName+"/"+fileName;
                    signImg.setPath(path);


                    break;
                }
            }
        }

        signDao.insert(signImg);
        return signImg;
    }
}
