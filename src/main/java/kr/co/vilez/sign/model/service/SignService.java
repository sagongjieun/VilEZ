package kr.co.vilez.sign.model.service;

import kr.co.vilez.sign.model.dto.SignImg;
import org.springframework.web.multipart.MultipartFile;


public interface SignService {
    public SignImg signUpload(SignImg signImg , MultipartFile multipartFile) throws Exception;
    public SignImg getContract(SignImg signImg) throws Exception;
}
