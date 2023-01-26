package kr.co.vilez.email.model.service;

public interface EmailService {

    String userEmailCheck(String email) throws Exception;
    String sendSimpleMessage(String to)throws Exception;
}
