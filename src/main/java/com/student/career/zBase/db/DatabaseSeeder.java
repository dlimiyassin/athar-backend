package com.student.career.zBase.db;

import com.student.career.bean.*;
import com.student.career.bean.enums.FieldType;
import com.student.career.bean.enums.NiveauEtude;
import com.student.career.bean.enums.Universite;
import com.student.career.dao.AcademicProfileFieldRepository;
import com.student.career.service.api.StudentService;
import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.bean.enums.UserStatus;
import com.student.career.zBase.security.dao.facade.RoleDao;
import com.student.career.zBase.security.dao.facade.UserDao;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class DatabaseSeeder implements ApplicationRunner {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final StudentService studentService;
    private final AcademicProfileFieldRepository academicProfileFieldRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(
            UserDao userDao,
            RoleDao roleDao,
            StudentService studentService,
            AcademicProfileFieldRepository academicProfileFieldRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.studentService = studentService;
        this.academicProfileFieldRepository = academicProfileFieldRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {

        /* ===================== ROLES ===================== */

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
                }
        );

        /* ===================== STUDENT ===================== */

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
                    licence.setUniversite(Universite.soltan_molay_slimane);
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
                    bac.setUniversite(Universite.soltan_molay_slimane);
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
                    licence.setUniversite(Universite.soltan_molay_slimane);
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
                    bac.setUniversite(Universite.soltan_molay_slimane);
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
                    licence.setUniversite(Universite.soltan_molay_slimane);
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
                    bac.setUniversite(Universite.soltan_molay_slimane);
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
}
