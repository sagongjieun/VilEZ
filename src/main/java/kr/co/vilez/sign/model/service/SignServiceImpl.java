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
    public void deleteContract(SignImg signImg) throws Exception {
        signDao.delete(signImg);
    }

    @Override
    public SignImg getContract(SignImg signImg) throws Exception {
        return signDao.loadContract(signImg);
    }

    @Override
    public SignImg signUpload(SignImg signImg) throws Exception {
        signDao.insert(signImg);
        return signImg;
    }
}
