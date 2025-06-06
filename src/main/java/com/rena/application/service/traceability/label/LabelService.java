package com.rena.application.service.traceability.label;

import com.rena.application.config.mapper.label.LabelValueMapper;
import com.rena.application.entity.dto.traceability.label.*;
import com.rena.application.entity.model.traceability.common.station.Station;
import com.rena.application.entity.model.traceability.label.LabelStationActive;
import com.rena.application.entity.model.traceability.label.LabelType;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import com.rena.application.repository.traceability.common.station.StationRepository;
import com.rena.application.repository.traceability.label.LabelStationActiveRepository;
import com.rena.application.repository.traceability.label.LabelTypeRepository;
import com.rena.application.repository.traceability.label.LabelValueRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LabelService {
    private final LabelTypeRepository labelTypeRepository;
    private final LabelValueRepository labelValueRepository;
    private final StationRepository stationRepository;
    private final LabelStationActiveRepository labelStationActiveRepository;
    private final LabelValueMapper labelValueMapper;
    private final BoilerRepository boilerRepository;

    @Transactional
    public LabelLastStation getLastLabel(String stationName) {
        var labelStationActive = labelStationActiveRepository.findByStation_Name(stationName);
        if (labelStationActive.isEmpty()) {
            return new LabelLastStation(null, null);
        }
        var labelValueDtoList = getLabelListValueDto(labelStationActive.get().getLabelType().getName());
        return new LabelLastStation(labelStationActive.get().getLabelType().getName(), labelValueDtoList);
    }

    public ListLabels getListLabels(String stationName) {
        var station = stationRepository.findByName(stationName)
            .orElseThrow(() -> new RecordNotFoundException("Станция не найдена: " + stationName));
        var labelTypes = labelTypeRepository.findByStationType_Name(station.getStationType().getName());
        var listLabes = labelTypes.stream()
            .map(LabelType::getName)
            .toList();
        return new ListLabels(listLabes);
    }

    public ListLabelValue getListLabelValue(String labelName) {
        var labelValueDtoList = getLabelListValueDto(labelName);
        return new ListLabelValue(labelValueDtoList);
    }

    private List<LabelValueDto> getLabelListValueDto(String labelName) {
        var labelValues = labelValueRepository.findByLabelType_Name(labelName);
        return labelValueMapper.toLabelValueDto(labelValues);
    }

    @Transactional
    public LabelLastStation selectLabelType(@Valid LabelStationNameData labelStationNameData) {
        var activeLabel = labelStationActiveRepository.findByStation_Name(labelStationNameData.getStationName());
        activeLabel.ifPresent((active) -> {
            labelStationActiveRepository.delete(active);
            labelStationActiveRepository.flush();
        });
        var station = stationRepository.findByName(labelStationNameData.getStationName())
                .orElseThrow(() -> new RecordNotFoundException("Станция не найдена: " + labelStationNameData.getStationName()));
        var labelType = labelTypeRepository.findByName(labelStationNameData.getLabelName())
                .orElseThrow(() -> new RecordNotFoundException("Тип этикетки не найден: " + labelStationNameData.getLabelName()));
        var labelValues = labelValueRepository.findByLabelType_Name(labelStationNameData.getLabelName());
        createActiveLabel(station, labelType);
        return new LabelLastStation(labelType.getName(), labelValueMapper.toLabelValueDto(labelValues));
    }

    private void createActiveLabel(Station station, LabelType labelType) {
        var activeLabelNew = new LabelStationActive();
        activeLabelNew.setStation(station);
        activeLabelNew.setLabelType(labelType);
        labelStationActiveRepository.save(activeLabelNew);
    }

    @Transactional
    public LabelLastStation selectLabelTypeManual(@Valid LabelValuesManualRequest labelValuesManualRequest) {
        var isBoilerExists = boilerRepository.existsBySerialNumber(labelValuesManualRequest.getSerialNumber());
        if (!isBoilerExists) {
            throw new RecordNotFoundException("Котел с серийным номером " + labelValuesManualRequest.getSerialNumber() + " не найден.");
        }
        return selectLabelType(new LabelStationNameData(labelValuesManualRequest.getLabelName(), labelValuesManualRequest.getStationName()));
    }
}
