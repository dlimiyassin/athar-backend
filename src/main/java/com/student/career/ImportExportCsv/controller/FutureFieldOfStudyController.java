package com.student.career.ImportExportCsv.controller;


import com.student.career.ImportExportCsv.ws.dto.FutureFieldOfStudyDto;
import com.student.career.ImportExportCsv.service.api.FutureFieldOfStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fields-of-study")
@RequiredArgsConstructor
public class FutureFieldOfStudyController {

    private final FutureFieldOfStudyService service;

    @GetMapping("/active")
    public List<FutureFieldOfStudyDto> findActive() {
        return service.findAllActive();
    }

    @PostMapping
    public FutureFieldOfStudyDto save(@RequestBody FutureFieldOfStudyDto dto) {
        return service.save(dto);
    }

    @GetMapping("/code/{code}")
    public FutureFieldOfStudyDto findByCode(@PathVariable Integer code) {
        return service.findByCode(code);
    }
}
