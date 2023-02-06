package kr.co.vilez.back.model.service;

import kr.co.vilez.back.model.dto.ReturnRequestDto;
import kr.co.vilez.back.model.vo.AppointmentVO;
import kr.co.vilez.back.model.vo.RoomStatusVO;

import java.util.Map;

public interface BackService {
    RoomStatusVO getRoomStatus(int roomId) throws Exception;
    AppointmentVO confirmedReturns(int roomId) throws Exception;
    Map<String, String> checkReturnRequest(int roomId) throws Exception;
    void requestReturn(ReturnRequestDto returnRequestDto) throws Exception;
    Integer isState(int roomId) throws Exception;
}
