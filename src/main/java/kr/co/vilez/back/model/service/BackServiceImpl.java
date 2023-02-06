package kr.co.vilez.back.model.service;

import kr.co.vilez.appointment.model.mapper.AppointmentMapper;
import kr.co.vilez.back.model.dao.BackDao;
import kr.co.vilez.back.model.dto.AppointmentStateDto;
import kr.co.vilez.back.model.dto.ReturnRequestDto;
import kr.co.vilez.back.model.mapper.BackMapper;
import kr.co.vilez.back.model.vo.AppointmentVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class BackServiceImpl implements BackService{
    final BackDao backDao;
    final BackMapper backMapper;

    @Override
    public AppointmentVO confirmedReturns(int roomId) throws Exception {
        // roomId를 통한 예약 Id 불러오기
        AppointmentVO appointmentVO = backMapper.getAppointmentId(roomId);
        System.out.println("appointmentVO = " + appointmentVO);
        
        // 이전 반납 요청 내역 삭제
        backDao.deleteReturnRequest(roomId);

        // 에약 내역 -1 변경
        backMapper.setAppointment(appointmentVO);

        return appointmentVO;
    }

    @Override
    public Map<String, String> checkReturnRequest(int roomId) throws Exception{
        Map<String, String> map = new HashMap<>();

        ReturnRequestDto returnRequestDto = backDao.selectRequest(roomId);

        if(returnRequestDto == null){
            map.put("state", "false");
        }else{
            map.put("state", "true");
        }

        return map;
    }

    @Override
    public void requestReturn(ReturnRequestDto returnRequestDto)throws Exception{
        backDao.insert(returnRequestDto);
    }

    @Override
    public Integer isState(int roomId) throws Exception {
        List<Integer> list = backMapper.getAppointmentState(roomId);

        if(list.size() == 0){
            return -3;
        } else {
            return list.get(0);
        }
    }
}
