package kr.co.vilez.back.model.service;

import kr.co.vilez.back.model.dto.AppointmentStateDto;

public interface BackService {
    int isState(AppointmentStateDto appointmentStateDto) throws Exception;
}
