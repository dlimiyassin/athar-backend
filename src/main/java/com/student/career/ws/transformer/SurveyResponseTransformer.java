package com.student.career.ws.transformer;

import com.student.career.bean.Answer;
import com.student.career.bean.SurveyResponse;
import com.student.career.ws.dto.AnswerDto;
import com.student.career.ws.dto.SurveyResponseDto;
import com.student.career.zBase.transformer.AbstractTransformer;
import com.student.career.zBase.util.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class SurveyResponseTransformer
        extends AbstractTransformer<SurveyResponse, SurveyResponseDto> {

    @Override
    public SurveyResponse toEntity(SurveyResponseDto dto) {
        if (dto == null) {
            return null;
        }
        SurveyResponse response = new SurveyResponse();
        response.setId(dto.id());
        response.setSurveyId(dto.surveyId());
        response.setStudentId(dto.studentId());
        response.setAnswers(
                dto.answers() != null
                        ? dto.answers().stream().map(this::toAnswerEntity).collect(Collectors.toList())
                        : null
        );
        return response;
    }

    @Override
    public SurveyResponseDto toDto(SurveyResponse entity) {
        if (entity == null) {
            return null;
        }
        return new SurveyResponseDto(
                entity.getId(),
                entity.getSurveyId(),
                entity.getStudentId(),
                entity.getAnswers() != null
                        ? entity.getAnswers().stream().map(this::toAnswerDto).collect(Collectors.toList())
                        : null,
                DateUtil.dateTimeToString(LocalDateTime.from(entity.getSubmittedAt()))
        );
    }

    private Answer toAnswerEntity(AnswerDto dto) {
        Answer a = new Answer();
        a.setQuestionId(dto.questionId());
        a.setValue(dto.value());
        return a;
    }

    private AnswerDto toAnswerDto(Answer entity) {
        return new AnswerDto(
                entity.getQuestionId(),
                entity.getValue()
        );
    }
}
