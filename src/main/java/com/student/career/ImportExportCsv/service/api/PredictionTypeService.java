package com.student.career.ImportExportCsv.service.api;


import com.student.career.ImportExportCsv.ws.dto.PredictionTypeDto;

import java.util.List;

public interface PredictionTypeService {

    List<PredictionTypeDto> findAllActive();

    PredictionTypeDto save(PredictionTypeDto dto);

    PredictionTypeDto findByCode(String code);
}
