package com.student.career.zBase.db;

import com.student.career.bean.*;
import com.student.career.bean.enums.FieldType;
import com.student.career.bean.enums.NiveauEtude;
import com.student.career.bean.enums.QuestionType;
import com.student.career.bean.enums.TargetType;
import com.student.career.bean.enums.Universite;
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

    public DatabaseSeeder(
            UserDao userDao,
            RoleDao roleDao,
            StudentService studentService,
            AcademicProfileFieldRepository academicProfileFieldRepository,
            PasswordEncoder passwordEncoder,
            SurveyService surveyService // Inject SurveyService
    ) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.studentService = studentService;
        this.academicProfileFieldRepository = academicProfileFieldRepository;
        this.passwordEncoder = passwordEncoder;
        this.surveyService = surveyService;
    }

    @Override
    public void run(ApplicationArguments args) {


        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_TEACHER");
        createRoleIfNotExists("ROLE_STUDENT");


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
                    teacher.setFirstName("Teacher");
                    teacher.setLastName("One");
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
                    studentUser.setFirstName("Student");
                    studentUser.setLastName("One");
                    studentUser.setEmail("student@studentcareer.com");
                    studentUser.setPassword(passwordEncoder.encode("student"));
                    studentUser.setEnabled(true);
                    studentUser.setStatus(UserStatus.ACTIF);

                    Set<Role> roles = new HashSet<>();
                    roleDao.findByName("ROLE_STUDENT").ifPresent(roles::add);
                    studentUser.setRoles(roles);

                    userDao.save(studentUser);

                    Diplome licence = new Diplome();
                    licence.setUniversite(Universite.SOLTAN_MOLAY_SLIMANE);
                    licence.setEcole("FST");
                    licence.setFiliere("Licence");
                    licence.setIntitule("Licence en Science Physique");
                    licence.setNiveauEtude(NiveauEtude.BAC);
                    licence.setYear(null);
                    licence.setNote(null);

                    AcademicProfile profile = new AcademicProfile();
                    profile.setCurrentDiploma(licence);
                    profile.setCustomAttributes(Map.of("gpa", 3.5));

                    Diplome bac = new Diplome();
                    bac.setUniversite(Universite.SIDI_MOHAMED_BEN_ABDELLAH);
                    bac.setEcole("Lycee Hassan 2");
                    bac.setFiliere("Science Physique");
                    bac.setIntitule("Bac en Science Physique");
                    bac.setYear(2022);
                    bac.setNiveauEtude(NiveauEtude.BAC);
                    bac.setNote(14.22);

                    profile.getDiplomes().add(bac);

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

                    Diplome licence = new Diplome();
                    licence.setUniversite(Universite.SOLTAN_MOLAY_SLIMANE);
                    licence.setEcole("FST");
                    licence.setFiliere("Licence");
                    licence.setIntitule("Licence en Mathématiques Appliquées");
                    licence.setNiveauEtude(NiveauEtude.BAC);
                    licence.setYear(null);
                    licence.setNote(null);

                    AcademicProfile profile = new AcademicProfile();
                    profile.setCurrentDiploma(licence);
                    profile.setCustomAttributes(Map.of("gpa", 3.2));

                    Diplome bac = new Diplome();
                    bac.setUniversite(Universite.SOLTAN_MOLAY_SLIMANE);
                    bac.setEcole("Lycee Mohammed V");
                    bac.setFiliere("Sciences Mathématiques");
                    bac.setIntitule("Bac en Sciences Mathématiques");
                    bac.setYear(2021);
                    bac.setNiveauEtude(NiveauEtude.BAC);
                    bac.setNote(15.10);

                    profile.getDiplomes().add(bac);

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

                    Diplome licence = new Diplome();
                    licence.setUniversite(Universite.SOLTAN_MOLAY_SLIMANE);
                    licence.setEcole("FST");
                    licence.setFiliere("Licence");
                    licence.setIntitule("Licence en Informatique");
                    licence.setNiveauEtude(NiveauEtude.BAC);
                    licence.setYear(null);
                    licence.setNote(null);

                    AcademicProfile profile = new AcademicProfile();
                    profile.setCurrentDiploma(licence);
                    profile.setCustomAttributes(Map.of("gpa", 3.8));

                    Diplome bac = new Diplome();
                    bac.setUniversite(Universite.SOLTAN_MOLAY_SLIMANE);
                    bac.setEcole("Lycee Al Khawarizmi");
                    bac.setFiliere("Sciences Mathématiques A");
                    bac.setIntitule("Bac Sciences Mathématiques A");
                    bac.setYear(2020);
                    bac.setNiveauEtude(NiveauEtude.BAC);
                    bac.setNote(16.45);

                    profile.getDiplomes().add(bac);

                    Student student = new Student();
                    student.setUserId(studentUser.getId());
                    student.setAcademicProfile(profile);

                    studentService.save(student);
                }
        );


        /* ===================== ACADEMIC PROFILE FIELDS ===================== */
        createAcademicFieldIfNotExists(
                "disabled",
                "Disabled",
                FieldType.BOOLEAN,
                true
        );
        createAcademicFieldIfNotExists(
                "birthdate",
                "Birthdate",
                FieldType.DATE,
                false
        );
        createAcademicFieldIfNotExists(
                "motivation",
                "Motivation",
                FieldType.TEXT,
                false
        );
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
        List<Survey> existingSurveys = surveyService.findByTeacher(teacherId);
        if (!existingSurveys.isEmpty()) {
            return; // Surveys already created
        }

        // Survey 1: Semester Feedback
        Survey semesterFeedback = new Survey();
        semesterFeedback.setTitle("Semester Feedback");
        semesterFeedback.setDescription("Students evaluate this semester's courses and teaching methods.");
        semesterFeedback.setCreatedByTeacherId(teacherId);
        semesterFeedback.setTarget(TargetType.ALL_STUDENTS);

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
        courseQuality.setTarget(TargetType.ALL_STUDENTS);

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
}