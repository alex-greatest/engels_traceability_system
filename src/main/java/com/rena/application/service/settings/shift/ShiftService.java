package com.rena.application.service.settings.shift;

import com.rena.application.config.mapper.component.common.ShiftsMapper;
import com.rena.application.entity.dto.settings.shift.ShiftDto;
import com.rena.application.entity.model.settings.shift.Shift;
import com.rena.application.exceptions.DbException;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.SettingRepository;
import com.rena.application.repository.settings.shift.ShiftRepository;
import com.rena.application.repository.settings.shift.ShiftStationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final ShiftsMapper shiftsMapper;
    private final SettingRepository settingRepository;
    private final ShiftStationRepository shiftStationRepository;

    public List<ShiftDto> getAllShifts() {
        List<Shift> shifts = shiftRepository.findAll();
        return shiftsMapper.toDto(shifts);
    }

    @Transactional
    public void addShift(ShiftDto shiftDto) {
        Shift shift = shiftsMapper.toEntity(shiftDto);
        shiftRepository.findByNumber(shiftDto.number()).ifPresent((_) -> {throw new DbException("Смена с таким номером уже существует");});
        shiftRepository.save(shift);
    }

    @Transactional
    public void updateShift(Integer oldShiftNumber, ShiftDto shiftDto) {
        Shift shift = shiftRepository.findByNumber(oldShiftNumber).
                orElseThrow(() -> new RecordNotFoundException("Смена не найдена"));
        shift.setNumber(shiftDto.number());
        shift.setTimeStart(shiftDto.timeStart());
        shift.setTimeEnd(shiftDto.timeEnd());
    }

    @Transactional
    public void deleteShift(Integer shiftNumber) {
        Shift shift = shiftRepository.findByNumber(shiftNumber).
                orElseThrow(() -> new RecordNotFoundException("Смена не найдена"));
        shiftRepository.delete(shift);
    }

    @Transactional
    public Integer updateShiftStation(String nameStation, Integer amount) {
        var shiftStation = shiftStationRepository.findByStationName(nameStation).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        shiftStation.setAmountBoilerMade(shiftStation.getAmountBoilerMade() + amount);
        return shiftStation.getAmountBoilerMade();
    }

    @Transactional
    public Optional<Shift> resetShiftsStation() {
        var setting = settingRepository.findById(1L).orElseThrow(() -> new RecordNotFoundException("Настройки не найдены"));
        var shift = getCurrentShift();
        if (!shift.getNumber().equals(setting.getPrevShift())) {
            setting.setPrevShift(shift.getNumber());
            var shiftsStation = shiftStationRepository.findAll();
            shiftsStation.forEach((shiftStation) -> {
                shiftStation.setAmountBoilerMade(0);
            });
            return Optional.of(shift);
        }
        return Optional.empty();
    }

    public Shift getCurrentShift() {
        boolean isWithinInterval;
        var now = LocalTime.now();
        List<Shift> shifts = shiftRepository.findAll();
        for (Shift shift : shifts) {
            LocalTime startTime = shift.getTimeStart();
            LocalTime endTime = shift.getTimeEnd();
            if (endTime.isAfter(startTime)) {
                isWithinInterval = now.isAfter(startTime) && now.isBefore(endTime);
            } else {
                isWithinInterval = now.isAfter(startTime) || now.isBefore(endTime);
            }
            if (isWithinInterval) {
                return shift;
            }
        }
        throw new RecordNotFoundException("Смена не найдена");
    }

    public Integer getAmountBoilerMade(String nameStation) {
        var shiftStation = shiftStationRepository.findByStationName(nameStation).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        return shiftStation.getAmountBoilerMade();
    }
}
