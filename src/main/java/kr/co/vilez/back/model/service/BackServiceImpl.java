package kr.co.vilez.back.model.service;

import kr.co.vilez.appointment.model.dao.AppointmentDao;
import kr.co.vilez.appointment.model.mapper.AppointmentMapper;
import kr.co.vilez.appointment.model.vo.PointVO;
import kr.co.vilez.back.model.dao.BackDao;
import kr.co.vilez.back.model.dto.ReturnRequestDto;
import kr.co.vilez.back.model.mapper.BackMapper;
import kr.co.vilez.back.model.vo.AppointmentVO;
import kr.co.vilez.back.model.vo.RoomStatusVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class BackServiceImpl implements BackService{
    final BackDao backDao;
    final BackMapper backMapper;
    final AppointmentDao appointmentDao;
    final AppointmentMapper appointmentMapper;
    @Override
    public RoomStatusVO getRoomStatus(int roomId) throws Exception {
        return backDao.selectRoomStatus(roomId);
    }

    @Override
    public AppointmentVO confirmedReturns(int roomId) throws Exception {
        // roomId를 통한 예약 Id 불러오기
        AppointmentVO appointmentVO = backMapper.getAppointmentId(roomId);
        log.error("appointmentVO : ",appointmentVO);

       ///////////////////////////////////////////////////////////////////
        LocalDate appointmentEnd = LocalDate.parse(appointmentVO.getDate());
        LocalDate now = LocalDate.now();
        if(appointmentEnd.isAfter(now)){
            PointVO pointVO = new PointVO();
            pointVO.setBoardId(appointmentVO.getBoardId());
            pointVO.setUserId(appointmentVO.getNotShareUserId());

            Period period = Period.between(now, appointmentEnd);
            int days = period.getDays();

            pointVO.setPoint(-30 * days);
            pointVO.setType(appointmentVO.getType());
            pointVO.setDate(appointmentVO.getDate());

            // 내역 저장
            appointmentDao.savePoint(pointVO);
            // 피공유자 포인트 변동
            appointmentMapper.changePoint(pointVO);

            System.out.println("반납기한을 넘겨 반납하셨습니다.");
        }

            // 현재 채팅방 상태를 설정한다.
        // 반납 -1
        backDao.insertRoomStatus(new RoomStatusVO(roomId, -1));

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
