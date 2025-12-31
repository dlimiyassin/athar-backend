package com.student.career.ws.controller;

import com.student.career.bean.Survey;
import com.student.career.service.api.SurveyService;
import com.student.career.ws.dto.SurveyDto;
import com.student.career.ws.transformer.SurveyTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;
    private final SurveyTransformer transformer;

    public SurveyController(
            SurveyService surveyService,
            SurveyTransformer transformer
    ) {
        this.surveyService = surveyService;
        this.transformer = transformer;
    }

    @GetMapping()
    public List<SurveyDto> findAll() {
        return surveyService.findAll().stream()
                .map(transformer::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public SurveyDto findById(@PathVariable String id) {
        return transformer.toDto(surveyService.findById(id));
    }

    @GetMapping("/teacher")
    public List<SurveyDto> findByTeacher() {
        return surveyService.findByTeacher()
                .stream()
                .map(transformer::toDto)
                .toList();
    }

    @PostMapping
    public SurveyDto create(@RequestBody SurveyDto dto) {
        Survey created = surveyService.save(
                transformer.toEntity(dto)
        );
        return transformer.toDto(created);
    }


    @PutMapping
    public SurveyDto update(@RequestBody SurveyDto dto) {
        Survey created = surveyService.update(
                transformer.toEntity(dto)
        );
        return transformer.toDto(created);
    }

    @GetMapping("/answered-by-student")
    public List<SurveyDto> findSurveyAnsweredByStudent() {
        return surveyService.findSurveyAnsweredByStudent()
                .stream()
                .map(transformer::toDto)
                .toList();
    }

    @GetMapping("/not-answered-by-student")
    public List<SurveyDto> findSurveyNotAnsweredByStudent() {
        return surveyService.findSurveyNotAnsweredByStudent()
                .stream()
                .map(transformer::toDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    public void deleteSurveyById(@PathVariable String id) {
        surveyService.deleteByID(id);
    }

}
