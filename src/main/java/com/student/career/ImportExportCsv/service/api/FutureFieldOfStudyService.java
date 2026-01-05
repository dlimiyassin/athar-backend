package com.student.career.ImportExportCsv.service.api;


import com.student.career.ImportExportCsv.ws.dto.FutureFieldOfStudyDto;

import java.util.List;

public interface FutureFieldOfStudyService {

    List<FutureFieldOfStudyDto> findAll();

    List<FutureFieldOfStudyDto> findAllActive();

    FutureFieldOfStudyDto save(FutureFieldOfStudyDto dto);

    FutureFieldOfStudyDto findByCode(Integer code);

    void deleteByID(String id);
}

