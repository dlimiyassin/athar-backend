package com.student.career.dao;

import com.student.career.bean.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SurveyRepository extends MongoRepository<Survey, String> {

    List<Survey> findByCreatedByTeacherId(String teacherId);
}
