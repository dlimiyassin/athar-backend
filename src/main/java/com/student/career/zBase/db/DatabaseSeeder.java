package com.student.career.zBase.db;

import com.student.career.ImportExportCsv.beans.FutureFieldOfStudy;
import com.student.career.ImportExportCsv.beans.PredictionType;
import com.student.career.ImportExportCsv.beans.PredictionValueType;
import com.student.career.ImportExportCsv.dao.FieldOfStudyRepository;
import com.student.career.ImportExportCsv.dao.PredictionTypeRepository;
import com.student.career.bean.*;
import com.student.career.bean.enums.*;
import com.student.career.dao.AcademicProfileFieldRepository;
import com.student.career.service.api.StudentService;
import com.student.career.service.api.SurveyService;
import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.bean.enums.UserStatus;
import com.student.career.zBase.security.dao.facade.RoleDao;
import com.student.career.zBase.security.dao.facade.UserDao;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DatabaseSeeder implements ApplicationRunner {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final StudentService studentService;
    private final AcademicProfileFieldRepository academicProfileFieldRepository;
    private final PasswordEncoder passwordEncoder;
    private final SurveyService surveyService; // Add SurveyService
    private final FieldOfStudyRepository fieldOfStudyRepository;
    private final PredictionTypeRepository predictionTypeRepository;



    public DatabaseSeeder(
            UserDao userDao,
            RoleDao roleDao,
            StudentService studentService,
            AcademicProfileFieldRepository academicProfileFieldRepository,
            PasswordEncoder passwordEncoder,
            SurveyService surveyService, FieldOfStudyRepository fieldOfStudyRepository, PredictionTypeRepository predictionTypeRepository // Inject SurveyService
    ) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.studentService = studentService;
        this.academicProfileFieldRepository = academicProfileFieldRepository;
        this.passwordEncoder = passwordEncoder;
        this.surveyService = surveyService;
        this.fieldOfStudyRepository = fieldOfStudyRepository;
        this.predictionTypeRepository = predictionTypeRepository;
    }

    @Override
    public void run(ApplicationArguments args) {


        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_TEACHER");
        createRoleIfNotExists("ROLE_STUDENT");

        /* ===================== ACADEMIC PROFILE FIELDS ===================== */
        createAcademicFieldIfNotExists(
                "bilingual",
                "Bilingual",
                FieldType.BOOLEAN,
                false
        );
        createAcademicFieldIfNotExists(
                "birthdate",
                "Birthdate",
                FieldType.DATE,
                true
        );
        createAcademicFieldIfNotExists(
                "motivation",
                "Motivation",
                FieldType.TEXT,
                false
        );

        /* ===================== ADMIN ===================== */
        userDao.findByEmail("admin@studentcareer.com").ifPresentOrElse(
                u -> {},
                () -> {
                    User admin = new User();
                    admin.setFirstName("Admin");
                    admin.setLastName("System");
                    admin.setEmail("admin@studentcareer.com");
                    admin.setPassword(passwordEncoder.encode("admin"));
                    admin.setEnabled(true);
                    admin.setStatus(UserStatus.ACTIF);

                    Set<Role> roles = new HashSet<>();
                    roleDao.findByName("ROLE_ADMIN").ifPresent(roles::add);
                    admin.setRoles(roles);

                    userDao.save(admin);
                }
        );

        /* ===================== TEACHER ===================== */
        userDao.findByEmail("teacher@studentcareer.com").ifPresentOrElse(
                u -> {},
                () -> {
                    User teacher = new User();
                    teacher.setFirstName("Ahmed");
                    teacher.setLastName("Hachimi");
                    teacher.setEmail("teacher@studentcareer.com");
                    teacher.setPassword(passwordEncoder.encode("teacher"));
                    teacher.setEnabled(true);
                    teacher.setStatus(UserStatus.ACTIF);

                    Set<Role> roles = new HashSet<>();
                    roleDao.findByName("ROLE_TEACHER").ifPresent(roles::add);
                    teacher.setRoles(roles);

                    userDao.save(teacher);

                    // Create surveys for this teacher
                    createSampleSurveys(teacher.getId());
                }
        );

        /* ===================== STUDENTS ===================== */
        userDao.findByEmail("student@studentcareer.com").ifPresentOrElse(
                u -> {},
                () -> {
                    User studentUser = new User();
                    studentUser.setFirstName("Yassine");
                    studentUser.setLastName("Dlimi");
                    studentUser.setEmail("student@studentcareer.com");
                    studentUser.setPassword(passwordEncoder.encode("student"));
                    studentUser.setEnabled(true);
                    studentUser.setStatus(UserStatus.ACTIF);

                    Set<Role> roles = new HashSet<>();
                    roleDao.findByName("ROLE_STUDENT").ifPresent(roles::add);
                    studentUser.setRoles(roles);

                    userDao.save(studentUser);

                    Diploma licence = new Diploma();
                    licence.setUniversity(University.SOLTAN_MOLAY_SLIMANE);
                    licence.setSchool("FST");
                    licence.setStudyField(FieldOfStudy.ACCOUNTING);
                    licence.setTitle("Licence en Science Physique");
                    licence.setStudyLevel(StudyLevel.BAC);
                    licence.setYear(null);
                    licence.setGrade(null);

                    AcademicProfile profile = new AcademicProfile();
                    profile.setCurrentDiploma(licence);
                    profile.setCustomAttributes(Map.of("bilingual", true));
                    profile.setCustomAttributes(Map.of("birthdate", new Date()));
                    profile.setCustomAttributes(Map.of("motivation", "i'm happy"));


                    Diploma bac = new Diploma();
                    bac.setUniversity(University.SIDI_MOHAMED_BEN_ABDELLAH);
                    bac.setSchool("Lycee Hassan 2");
                    bac.setStudyField(FieldOfStudy.BUSINESS);
                    bac.setTitle("Bac en Science Physique");
                    bac.setYear(2022);
                    bac.setStudyLevel(StudyLevel.BAC);
                    bac.setGrade(14.22);

                    profile.getDiplomas().add(bac);

                    Student student = new Student();
                    student.setUserId(studentUser.getId());
                    student.setAcademicProfile(profile);

                    studentService.save(student);
                }
        );

        userDao.findByEmail("student2@studentcareer.com").ifPresentOrElse(
                u -> {},
                () -> {
                    User studentUser = new User();
                    studentUser.setFirstName("Student");
                    studentUser.setLastName("Two");
                    studentUser.setEmail("student2@studentcareer.com");
                    studentUser.setPassword(passwordEncoder.encode("student"));
                    studentUser.setEnabled(true);
                    studentUser.setStatus(UserStatus.ACTIF);

                    Set<Role> roles = new HashSet<>();
                    roleDao.findByName("ROLE_STUDENT").ifPresent(roles::add);
                    studentUser.setRoles(roles);

                    userDao.save(studentUser);

                    Diploma licence = new Diploma();
                    licence.setUniversity(University.SOLTAN_MOLAY_SLIMANE);
                    licence.setSchool("FST");
                    licence.setStudyField(FieldOfStudy.ELECTRICAL_ENGINEERING);
                    licence.setTitle("Licence en Mathématiques Appliquées");
                    licence.setStudyLevel(StudyLevel.BAC);
                    licence.setYear(null);
                    licence.setGrade(null);

                    AcademicProfile profile = new AcademicProfile();
                    profile.setCurrentDiploma(licence);
                    profile.setCustomAttributes(Map.of("bilingual", true));
                    profile.setCustomAttributes(Map.of("birthdate", new Date()));
                    profile.setCustomAttributes(Map.of("motivation", "i'm happy"));

                    Diploma bac = new Diploma();
                    bac.setUniversity(University.SOLTAN_MOLAY_SLIMANE);
                    bac.setSchool("Lycee Mohammed V");
                    bac.setStudyField(FieldOfStudy.MATHEMATICS);
                    bac.setTitle("Bac en Sciences Mathématiques");
                    bac.setYear(2021);
                    bac.setStudyLevel(StudyLevel.BAC);
                    bac.setGrade(15.10);

                    profile.getDiplomas().add(bac);

                    Student student = new Student();
                    student.setUserId(studentUser.getId());
                    student.setAcademicProfile(profile);

                    studentService.save(student);
                }
        );

        userDao.findByEmail("student3@studentcareer.com").ifPresentOrElse(
                u -> {},
                () -> {
                    User studentUser = new User();
                    studentUser.setFirstName("Student");
                    studentUser.setLastName("Three");
                    studentUser.setEmail("student3@studentcareer.com");
                    studentUser.setPassword(passwordEncoder.encode("student"));
                    studentUser.setEnabled(true);
                    studentUser.setStatus(UserStatus.ACTIF);

                    Set<Role> roles = new HashSet<>();
                    roleDao.findByName("ROLE_STUDENT").ifPresent(roles::add);
                    studentUser.setRoles(roles);

                    userDao.save(studentUser);

                    Diploma licence = new Diploma();
                    licence.setUniversity(University.SOLTAN_MOLAY_SLIMANE);
                    licence.setSchool("FST");
                    licence.setStudyField(FieldOfStudy.EDUCATION);
                    licence.setTitle("Licence en Informatique");
                    licence.setStudyLevel(StudyLevel.BAC);
                    licence.setYear(null);
                    licence.setGrade(null);

                    AcademicProfile profile = new AcademicProfile();
                    profile.setCurrentDiploma(licence);
                    profile.setCustomAttributes(Map.of("bilingual", true));
                    profile.setCustomAttributes(Map.of("birthdate", new Date()));
                    profile.setCustomAttributes(Map.of("motivation", "i'm happy"));

                    Diploma bac = new Diploma();
                    bac.setUniversity(University.SOLTAN_MOLAY_SLIMANE);
                    bac.setSchool("Lycee Al Khawarizmi");
                    bac.setStudyField(FieldOfStudy.SCIENCES_MATH_A);
                    bac.setTitle("Bac Sciences Mathématiques A");
                    bac.setYear(2020);
                    bac.setStudyLevel(StudyLevel.BAC);
                    bac.setGrade(16.45);

                    profile.getDiplomas().add(bac);

                    Student student = new Student();
                    student.setUserId(studentUser.getId());
                    student.setAcademicProfile(profile);

                    studentService.save(student);
                }
        );

        /* ===================== FIELD OF STUDY MASTER DATA ===================== */
        seedFieldOfStudies();

        /* ===================== PREDICTION TYPES ===================== */
        seedPredictionTypes();


    }

    /* ===================== HELPERS ===================== */
    private void createRoleIfNotExists(String roleName) {
        if (roleDao.findByName(roleName).isEmpty()) {
            roleDao.save(new Role(roleName));
        }
    }

    private void createAcademicFieldIfNotExists(
            String name,
            String label,
            FieldType type,
            boolean required
    ) {
        academicProfileFieldRepository.findByName(name).ifPresentOrElse(
                f -> {},
                () -> {
                    AcademicProfileField field = new AcademicProfileField();
                    field.setName(name);
                    field.setLabel(label);
                    field.setType(type);
                    field.setRequired(required);
                    academicProfileFieldRepository.save(field);
                }
        );
    }



    /* ===================== SURVEY CREATION ===================== */
    private void createSampleSurveys(String teacherId) {
        // Check if surveys already exist for this teacher
        List<Survey> existingSurveys = surveyService.findByTeacher();
        if (!existingSurveys.isEmpty()) {
            return; // Surveys already created
        }

        // Survey 1: Semester Feedback
        Survey semesterFeedback = new Survey();
        semesterFeedback.setTitle("Semester Feedback");
        semesterFeedback.setDescription("Students evaluate this semester's courses and teaching methods.");
        semesterFeedback.setCreatedByTeacherId(teacherId);
        semesterFeedback.setTarget(TargetType.ALL);

        List<Question> questions1 = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setId(UUID.randomUUID().toString());
        q1.setType(QuestionType.SCALE);
        q1.setLabel("How satisfied are you with this semester overall?");
        q1.setOptions(Arrays.asList("1", "2", "3", "4", "5"));
        questions1.add(q1);

        // Question 2
        Question q2 = new Question();
        q2.setId(UUID.randomUUID().toString());
        q2.setType(QuestionType.CHOICE);
        q2.setLabel("What was the most useful course this semester?");
        q2.setOptions(Arrays.asList(
                "Physics",
                "Mathematics",
                "Computer Science",
                "English",
                "Other"
        ));
        questions1.add(q2);

        // Question 3
        Question q3 = new Question();
        q3.setId(UUID.randomUUID().toString());
        q3.setType(QuestionType.TEXT);
        q3.setLabel("Any suggestions for improving next semester?");
        q3.setOptions(new ArrayList<>());
        questions1.add(q3);

        semesterFeedback.setQuestions(questions1);
        surveyService.save(semesterFeedback);

        // Survey 2: Course Quality Survey
        Survey courseQuality = new Survey();
        courseQuality.setTitle("Course Quality Survey");
        courseQuality.setDescription("Help us improve the course content and teaching methods.");
        courseQuality.setCreatedByTeacherId(teacherId);
        courseQuality.setTarget(TargetType.ALL);

        List<Question> questions2 = new ArrayList<>();

        // Question 1
        Question q4 = new Question();
        q4.setId(UUID.randomUUID().toString());
        q4.setType(QuestionType.SCALE);
        q4.setLabel("Rate the clarity of course materials (1-5):");
        q4.setOptions(Arrays.asList("1", "2", "3", "4", "5"));
        questions2.add(q4);

        // Question 2
        Question q5 = new Question();
        q5.setId(UUID.randomUUID().toString());
        q5.setType(QuestionType.CHOICE);
        q5.setLabel("Which resources did you find most helpful? (Select all that apply)");
        q5.setOptions(Arrays.asList(
                "Lecture slides",
                "Textbook",
                "Online videos",
                "Practice exercises",
                "Group discussions"
        ));
        questions2.add(q5);

        // Question 3
        Question q6 = new Question();
        q6.setId(UUID.randomUUID().toString());
        q6.setType(QuestionType.CHOICE);
        q6.setLabel("How challenging was the course?");
        q6.setOptions(Arrays.asList(
                "Too easy",
                "Just right",
                "Somewhat challenging",
                "Very challenging"
        ));
        questions2.add(q6);

        // Question 4
        Question q7 = new Question();
        q7.setId(UUID.randomUUID().toString());
        q7.setType(QuestionType.TEXT);
        q7.setLabel("Any additional comments about the course?");
        q7.setOptions(new ArrayList<>());
        questions2.add(q7);

        courseQuality.setQuestions(questions2);
        surveyService.save(courseQuality);
    }

    private void seedFieldOfStudies() {

        Map<Integer, String> fields = new LinkedHashMap<>();

        fields.put(1, "Mathematics");
        fields.put(2, "Physics");
        fields.put(3, "Chemistry");
        fields.put(4, "Biology");
        fields.put(5, "Computer Science");
        fields.put(6, "Electronics");
        fields.put(7, "Mechanical Engineering");
        fields.put(8, "Civil Engineering");
        fields.put(9, "Electrical Engineering");
        fields.put(10, "Software Engineering");
        fields.put(11, "Industrial Engineering");
        fields.put(12, "Telecommunications");
        fields.put(13, "Economics");
        fields.put(14, "Management");
        fields.put(15, "Business");
        fields.put(16, "Finance");
        fields.put(17, "Accounting");
        fields.put(18, "Marketing");
        fields.put(19, "Logistics and Transport");
        fields.put(20, "Medicine");
        fields.put(21, "Pharmacy");
        fields.put(22, "Dentistry");
        fields.put(23, "Nursing");
        fields.put(24, "Public Health");
        fields.put(25, "Law");
        fields.put(26, "Political Science");
        fields.put(27, "International Relations");
        fields.put(28, "Education Sciences");
        fields.put(29, "Psychology");
        fields.put(30, "Sociology");
        fields.put(31, "Literature");
        fields.put(32, "Languages");
        fields.put(33, "History");
        fields.put(34, "Geography");
        fields.put(35, "Philosophy");
        fields.put(36, "Agriculture");
        fields.put(37, "Agronomy");
        fields.put(38, "Environment");
        fields.put(39, "Agri-Food Science");
        fields.put(40, "Data Science");
        fields.put(41, "Artificial Intelligence");
        fields.put(42, "Cybersecurity");
        fields.put(43, "Other");

        fields.forEach((code, name) -> {
            fieldOfStudyRepository.findByCode(code)
                    .orElseGet(() -> {
                        FutureFieldOfStudy f = new FutureFieldOfStudy();
                        f.setCode(code);
                        f.setLabel(name);
                        f.setActive(true);
                        return fieldOfStudyRepository.save(f);
                    });
        });
    }


    private void seedPredictionTypes() {

        createPredictionTypeIfNotExists(
                "FIELD_OF_STUDY",
                "Future Field of Study",
                PredictionValueType.CATEGORY,
                "AI prediction for the most suitable field of study"
        );

        createPredictionTypeIfNotExists(
                "JOB_ELIGIBILITY",
                "Job Eligibility",
                PredictionValueType.BOOLEAN,
                "AI prediction indicating whether the student can get a job"
        );

        createPredictionTypeIfNotExists(
                "GRADE_PREDICTION",
                "Grade Prediction",
                PredictionValueType.NUMBER,
                "AI predicted grade for a course or module (0–20)"
        );
    }

    private void createPredictionTypeIfNotExists(
            String code,
            String label,
            PredictionValueType valueType,
            String description
    ) {
        predictionTypeRepository.findByCode(code)
                .orElseGet(() -> {
                    PredictionType type = new PredictionType();
                    type.setCode(code);
                    type.setLabel(label);
                    type.setValueType(valueType);
                    type.setDescription(description);
                    type.setActive(true);
                    return predictionTypeRepository.save(type);
                });
    }


}