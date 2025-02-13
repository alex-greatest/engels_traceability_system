package com.rena.application.service;

import com.rena.application.config.mapper.ShiftsMapper;
import com.rena.application.entity.dto.ShiftDto;
import com.rena.application.entity.model.Shift;
import com.rena.application.exceptions.DbException;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.ShiftRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
@Slf4j
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final ShiftsMapper shiftsMapper;

    public List<ShiftDto> getAllShifts() {
        List<Shift> componentNameSets = shiftRepository.findAll();
        return shiftsMapper.toDto(componentNameSets);
    }

    @Transactional
    public void addShift(ShiftDto shiftDto) {
        Shift shift = shiftsMapper.toEntity(shiftDto);
        shiftRepository.findByNumber(shiftDto.number()).ifPresent((s) -> {throw new DbException("Смена с таким номером уже существует");});
        shiftRepository.save(shift);
    }

    @Transactional
    public void updateShift(Long oldShiftNumber, ShiftDto shiftDto) {
        Shift shift = shiftRepository.findByNumber(oldShiftNumber).
                orElseThrow(() -> new RecordNotFoundException("Смена не найдена"));
        shift.setNumber(shiftDto.number());
        shift.setTimeStart(shiftDto.timeStart());
        shift.setTimeEnd(shiftDto.timeEnd());
        shiftRepository.save(shift);
    }

    @Transactional
    public void deleteShift(Long shiftNumber) {
        Shift shift = shiftRepository.findByNumber(shiftNumber).
                orElseThrow(() -> new RecordNotFoundException("Смена не найдена"));
        shiftRepository.delete(shift);
    }
}
