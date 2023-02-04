package kr.co.vilez.back.model.service;

import kr.co.vilez.appointment.model.mapper.AppointmentMapper;
import kr.co.vilez.back.model.dto.AppointmentStateDto;
import kr.co.vilez.back.model.mapper.BackMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BackServiceImpl implements BackService{

    final BackMapper backMapper;

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
