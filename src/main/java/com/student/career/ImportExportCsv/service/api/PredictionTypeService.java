package com.student.career.ImportExportCsv.service.api;



import com.student.career.ImportExportCsv.beans.PredictionType;

import java.util.List;

public interface PredictionTypeService {

    List<PredictionType> findAllActive();

    PredictionType save(PredictionType dto);

    PredictionType findByCode(String code);

    PredictionType findById(String code);

    List<PredictionType> findAll();

    void deleteByID(String id);
}
