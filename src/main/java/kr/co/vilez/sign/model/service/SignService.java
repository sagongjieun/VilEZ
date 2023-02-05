package kr.co.vilez.sign.model.service;

import kr.co.vilez.sign.model.dto.SignImg;
import org.springframework.web.multipart.MultipartFile;


public interface SignService {
    public void deleteContract(int roomId) throws Exception;
    public SignImg signUpload(SignImg signImg) throws Exception;
    public SignImg getContract(int roomId) throws Exception;
}
