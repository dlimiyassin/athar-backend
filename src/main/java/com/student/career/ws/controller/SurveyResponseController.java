package com.student.career.ws.controller;

import com.student.career.bean.SurveyResponse;
import com.student.career.service.api.SurveyResponseService;
import com.student.career.ws.dto.SurveyResponseDto;
import com.student.career.ws.transformer.SurveyResponseTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/survey-responses")
public class SurveyResponseController {

    private final SurveyResponseService surveyResponseService;
    private final SurveyResponseTransformer transformer;

    public SurveyResponseController(
            SurveyResponseService surveyResponseService,
            SurveyResponseTransformer transformer
    ) {
        this.surveyResponseService = surveyResponseService;
        this.transformer = transformer;
    }

    @PostMapping
    public SurveyResponseDto submit(@RequestBody SurveyResponseDto dto) {
        SurveyResponse saved = surveyResponseService.submit(
                transformer.toEntity(dto)
        );
        return transformer.toDto(saved);
    }

    @GetMapping("/survey/{surveyId}")
    public List<SurveyResponseDto> findBySurvey(@PathVariable String surveyId) {
        return surveyResponseService.findBySurvey(surveyId)
                .stream()
                .map(transformer::toDto)
                .toList();
    }

    @GetMapping("/survey/{surveyId}/student/{studentId}")
    public ResponseEntity<SurveyResponseDto> findBySurveyAndStudent(
            @PathVariable String surveyId,
            @PathVariable String studentId
    ) {
        return surveyResponseService.findBySurveyAndStudent(surveyId, studentId)
                .map(transformer::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
