package com.student.career.ImportExportCsv.service.impl;

import com.student.career.bean.AcademicProfile;
import com.student.career.bean.AcademicProfileField;
import com.student.career.bean.Diploma;
import com.student.career.bean.Student;
import com.student.career.bean.enums.StudyLevel;
import com.student.career.dao.AcademicProfileFieldRepository;
import com.student.career.dao.StudentRepository;
import com.student.career.dao.SurveyRepository;
import com.student.career.dao.SurveyResponseRepository;
import com.student.career.ImportExportCsv.service.api.SurveyExportService;
import com.student.career.zBase.util.AnonymizationUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SurveyExportServiceImpl
        implements SurveyExportService {

    private final StudentRepository studentRepository;
    private final AcademicProfileFieldRepository academicProfileFieldRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final AnonymizationUtil anonymizationUtil;

    public SurveyExportServiceImpl(
            StudentRepository studentRepository,
            AcademicProfileFieldRepository academicProfileFieldRepository,
            SurveyRepository surveyRepository,
            SurveyResponseRepository surveyResponseRepository, AnonymizationUtil anonymizationUtil
    ) {
        this.studentRepository = studentRepository;
        this.academicProfileFieldRepository = academicProfileFieldRepository;
        this.surveyRepository = surveyRepository;
        this.surveyResponseRepository = surveyResponseRepository;
        this.anonymizationUtil = anonymizationUtil;
    }

    @Override
    public void exportStudentsSurveyCsv(HttpServletResponse response) {

        try {
            response.setContentType("text/csv");
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=students_survey_export.csv"
            );

            List<Student> students = studentRepository.findAll();
            List<String> studentIds = students.stream()
                    .map(Student::getId)
                    .toList();

            /* =========================
               1. Dynamic academic fields
               ========================= */
            List<AcademicProfileField> academicFields =
                    academicProfileFieldRepository.findAll();

            List<String> academicFieldLabels = academicFields.stream()
                    .map(AcademicProfileField::getLabel)
                    .toList();

            /* =========================
               2. Dynamic question labels
               ========================= */
            Set<String> questionLabels = new LinkedHashSet<>();

            surveyRepository.findAll().forEach(survey -> {
                if (survey.getQuestions() != null) {
                    survey.getQuestions().forEach(q ->
                            questionLabels.add(q.getLabel())
                    );
                }
            });

            /* =========================
               3. Header
               ========================= */
            List<String> headers = new ArrayList<>();

            headers.addAll(List.of(
                    "student_id",
                    "sexe",
                    "date de naissance",
                    // bac
                    "Année d'obtention du Bac",
                    "Note d Examen National du BAC",
                    "Option du Bac",
                    // bac+3
                    "Année d'obtention de la licence",
                    "Moyenne de la licence",
                    "Spécialité de la licence",
                    "Etablissement de l'enseignement supérieur"
            ));

            //headers.addAll(academicFieldLabels);
            headers.addAll(questionLabels);

            /* =========================
               4. Prepare answers lookup
               ========================= */
            Map<String, Map<String, String>> studentAnswersMap =
                    new HashMap<>();

            surveyResponseRepository
                    .findByStudentIdIn(studentIds)
                    .forEach(responseEntity -> {

                        Map<String, String> answers =
                                studentAnswersMap.computeIfAbsent(
                                        responseEntity.getStudentId(),
                                        k -> new HashMap<>()
                                );

                        if (responseEntity.getAnswers() != null) {
                            responseEntity.getAnswers().forEach(a ->
                                    answers.put(
                                            a.getQuestionLabel(),
                                            a.getValue()
                                    )
                            );
                        }
                    });

            /* =========================
               5. Write CSV
               ========================= */
            try (CSVPrinter printer = new CSVPrinter(
                    response.getWriter(),
                    CSVFormat.DEFAULT.withHeader(
                            headers.toArray(String[]::new)
                    )
            )) {

                for (Student student : students) {

                    AcademicProfile profile =
                            student.getAcademicProfile();

                    Diploma diploma = profile != null
                            ? profile.getCurrentDiploma()
                            : null;

                    Diploma bac = profile != null
                            ? profile.getDiplomas().stream().filter(d -> d.getStudyLevel().equals(StudyLevel.BAC)).toList().getFirst()
                            : null;

                    List<String> row = new ArrayList<>();


                    // Fixed columns
                    row.add(anonymizationUtil.anonymizeStudentId(student.getId()));
                    row.add(
                            profile != null && profile.getGender() != null
                                    ? String.valueOf(profile.getGender())
                                    : "Unknown");
                    row.add(
                            profile != null && profile.getCustomAttributes().get("birthdate") != null
                                    ? String.valueOf(profile.getCustomAttributes().get("birthdate"))
                                    : "Unknown"
                    );

                    // BAC
                    row.add(bac != null && bac.getYear() != null
                            ? bac.getYear().toString()
                            : "");
                    row.add(bac != null && bac.getGrade() != null
                            ? bac.getGrade().toString()
                            : "");
                    row.add(bac != null ? String.valueOf(bac.getStudyField()) : "");


                    // CURRENT DIPLOMA
                    row.add(diploma != null && diploma.getYear() != null
                            ? diploma.getYear().toString()
                            : "");
                    row.add(diploma != null && diploma.getGrade() != null
                            ? diploma.getGrade().toString()
                            : "");
                    row.add(diploma != null ? String.valueOf(diploma.getStudyField()) : "");
                    row.add(diploma != null ? diploma.getSchool() : "");


                    // Custom academic attributes
//                    Map<String, Object> customAttrs =
//                            profile != null
//                                    ? profile.getCustomAttributes()
//                                    : Map.of();
//
//                    for (AcademicProfileField field : academicFields) {
//                        Object value = customAttrs.get(field.getName());
//                        row.add(value != null ? value.toString() : "");
//                    }

                    // Survey answers
                    Map<String, String> answers =
                            studentAnswersMap.getOrDefault(
                                    student.getId(),
                                    Map.of()
                            );

                    for (String qLabel : questionLabels) {
                        row.add(answers.getOrDefault(qLabel, ""));
                    }

                    printer.printRecord(row);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to export CSV", e
            );
        }
    }
}
