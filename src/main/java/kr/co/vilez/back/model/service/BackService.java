package kr.co.vilez.back.model.service;

import kr.co.vilez.back.model.dto.AppointmentStateDto;
import kr.co.vilez.back.model.dto.ReturnRequestDto;
import kr.co.vilez.back.model.vo.AppointmentVO;

import java.util.Map;

public interface BackService {
    AppointmentVO confirmedReturns(int roomId) throws Exception;
    Map<String, String> checkReturnRequest(int roomId) throws Exception;
    void requestReturn(ReturnRequestDto returnRequestDto) throws Exception;
    Integer isState(int roomId) throws Exception;
}
