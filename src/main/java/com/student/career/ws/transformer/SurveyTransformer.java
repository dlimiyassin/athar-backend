package com.student.career.ws.transformer;

import com.student.career.bean.Question;
import com.student.career.bean.Survey;
import com.student.career.ws.dto.QuestionDto;
import com.student.career.ws.dto.SurveyDto;
import com.student.career.zBase.transformer.AbstractTransformer;
import com.student.career.zBase.util.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class SurveyTransformer
        extends AbstractTransformer<Survey, SurveyDto> {

    @Override
    public Survey toEntity(SurveyDto dto) {
        if (dto == null) {
            return null;
        }
        Survey survey = new Survey();
        survey.setId(dto.id());
        survey.setTitle(dto.title());
        survey.setDescription(dto.description());
        survey.setCreatedByTeacherId(dto.createdByTeacherId());
        survey.setTarget(dto.target());
        survey.setQuestions(
                dto.questions() != null
                        ? dto.questions().stream().map(this::toQuestionEntity).collect(Collectors.toList())
                        : null
        );
        return survey;
    }

    @Override
    public SurveyDto toDto(Survey entity) {
        if (entity == null) {
            return null;
        }
        return new SurveyDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCreatedByTeacherId(),
                entity.getQuestions() != null
                        ? entity.getQuestions().stream().map(this::toQuestionDto).collect(Collectors.toList())
                        : null,
                entity.getTarget(),
                DateUtil.dateTimeToString(LocalDateTime.from(entity.getCreatedAt()))
        );
    }

    private Question toQuestionEntity(QuestionDto dto) {
        Question q = new Question();
        q.setId(dto.id());
        q.setType(dto.type());
        q.setLabel(dto.label());
        q.setOptions(dto.options());
        return q;
    }

    private QuestionDto toQuestionDto(Question entity) {
        return new QuestionDto(
                entity.getId(),
                entity.getType(),
                entity.getLabel(),
                entity.getOptions()
        );
    }
}
