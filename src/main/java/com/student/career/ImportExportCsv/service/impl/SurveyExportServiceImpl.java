package com.student.career.ImportExportCsv.service.impl;

import com.student.career.bean.AcademicProfile;
import com.student.career.bean.Diploma;
import com.student.career.bean.Student;
import com.student.career.bean.enums.StudyLevel;
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
public class SurveyExportServiceImpl implements SurveyExportService {

    private final StudentRepository studentRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final AnonymizationUtil anonymizationUtil;

    public SurveyExportServiceImpl(
            StudentRepository studentRepository,
            SurveyRepository surveyRepository,
            SurveyResponseRepository surveyResponseRepository,
            AnonymizationUtil anonymizationUtil
    ) {
        this.studentRepository = studentRepository;
        this.surveyRepository = surveyRepository;
        this.surveyResponseRepository = surveyResponseRepository;
        this.anonymizationUtil = anonymizationUtil;
    }

    @Override
    public void exportStudentsSurveyCsv(HttpServletResponse response) {
        try {
            // Set CSV headers
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=students_survey_export.csv");

            // Fetch students
            List<Student> students = studentRepository.findAll();
            System.out.println("Total students fetched: " + students.size());

            if (students.isEmpty()) {
                System.out.println("No students found. Aborting export.");
                return;
            }

            List<String> studentIds = students.stream().map(Student::getId).toList();
            System.out.println("Student IDs count: " + studentIds.size());

            // Prepare survey question labels
            Set<String> questionLabels = new LinkedHashSet<>();
            surveyRepository.findAll().forEach(survey -> {
                if (survey.getQuestions() != null) {
                    survey.getQuestions().forEach(q -> questionLabels.add(q.getLabel()));
                }
            });
            System.out.println("Total unique survey questions: " + questionLabels.size());

            // Prepare survey answers map
            Map<String, Map<String, String>> studentAnswersMap = new HashMap<>();
            surveyResponseRepository.findByStudentIdIn(studentIds)
                    .forEach(resp -> {
                        Map<String, String> answers = studentAnswersMap.computeIfAbsent(resp.getStudentId(), k -> new HashMap<>());
                        if (resp.getAnswers() != null) {
                            resp.getAnswers().forEach(a -> answers.put(a.getQuestionLabel(), a.getValue()));
                        }
                    });
            System.out.println("Survey responses mapped for " + studentAnswersMap.size() + " students");

            // CSV headers
            List<String> headers = new ArrayList<>(List.of(
                    "student_id", "sexe", "date de naissance",
                    "Année d'obtention du Bac", "Note d Examen National du BAC", "Option du Bac",
                    "Année d'obtention de la licence", "Moyenne de la licence", "Spécialité de la licence", "Etablissement de l'enseignement supérieur"
            ));
            headers.addAll(questionLabels);

            // Write CSV
            try (CSVPrinter printer = new CSVPrinter(response.getWriter(), CSVFormat.DEFAULT.withHeader(headers.toArray(String[]::new)))) {
                System.out.println("Writing CSV for students...");
                for (Student student : students) {
                    try {
                        AcademicProfile profile = student.getAcademicProfile();
                        Diploma currentDiploma = profile != null ? profile.getCurrentDiploma() : null;

                        Diploma bac = null;
                        if (profile != null && profile.getDiplomas() != null) {
                            bac = profile.getDiplomas().stream()
                                    .filter(d -> StudyLevel.BAC.equals(d.getStudyLevel()))
                                    .findFirst()
                                    .orElse(null);
                        }

                        List<String> row = new ArrayList<>();

                        // Fixed columns
                        row.add(anonymizationUtil.anonymizeStudentId(student.getId()));
                        row.add(profile != null && profile.getGender() != null ? String.valueOf(profile.getGender()) : "Unknown");
                        row.add(profile != null && profile.getCustomAttributes().get("birthdate") != null
                                ? String.valueOf(profile.getCustomAttributes().get("birthdate"))
                                : "Unknown");

                        // BAC info
                        row.add(bac != null && bac.getYear() != null ? bac.getYear().toString() : "");
                        row.add(bac != null && bac.getGrade() != null ? bac.getGrade().toString() : "");
                        row.add(bac != null ? String.valueOf(bac.getStudyField()) : "");

                        // Current diploma
                        row.add(currentDiploma != null && currentDiploma.getYear() != null ? currentDiploma.getYear().toString() : "");
                        row.add(currentDiploma != null && currentDiploma.getGrade() != null ? currentDiploma.getGrade().toString() : "");
                        row.add(currentDiploma != null ? String.valueOf(currentDiploma.getStudyField()) : "");
                        row.add(currentDiploma != null ? currentDiploma.getSchool() : "");

                        // Survey answers
                        Map<String, String> answers = studentAnswersMap.getOrDefault(student.getId(), Map.of());
                        for (String qLabel : questionLabels) {
                            row.add(answers.getOrDefault(qLabel, ""));
                        }

                        printer.printRecord(row);
                    } catch (Exception e) {
                        System.out.println("Failed to write student " + student.getId() + ": " + e.getMessage());
                    }
                }
            }

            System.out.println("CSV export completed successfully.");

        } catch (Exception e) {
            System.out.println("Export failed: " + e.getMessage());
            throw new RuntimeException("Failed to export CSV", e);
        }
    }
}
