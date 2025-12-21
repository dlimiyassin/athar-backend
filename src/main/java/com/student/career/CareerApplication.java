package com.student.career;
import com.student.career.bean.enums.FieldType;
import com.student.career.bean.*;
import com.student.career.bean.enums.NiveauEtude;
import com.student.career.bean.enums.Universite;
import com.student.career.dao.*;
import com.student.career.service.api.StudentService;
import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.bean.enums.UserStatus;
import com.student.career.zBase.security.dao.facade.RoleDao;
import com.student.career.zBase.security.dao.facade.UserDao;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class CareerApplication {

	private final RoleDao roleDao;

	public CareerApplication(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public static void main(String[] args) {
		SpringApplication.run(CareerApplication.class, args);
	}

	@Bean
	ApplicationRunner init(
			UserDao userDao,
			StudentService studentService,
			AcademicProfileFieldRepository academicProfileFieldRepository,
			PasswordEncoder passwordEncoder
	) {
		return args -> {

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

						AcademicProfile profile = new AcademicProfile();
						profile.setEcole("ENS");
						profile.setProgram("Computer Science");
						profile.setYear(3);
						profile.setCustomAttributes(
								Map.of("gpa", 3.5)
						);
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

			/* ===================== ACADEMIC PROFILE FIELDS ===================== */

			createAcademicFieldIfNotExists(
					academicProfileFieldRepository,
					"gpa",
					"Grade Point Average",
					FieldType.NUMBER,
					true
			);

			createAcademicFieldIfNotExists(
					academicProfileFieldRepository,
					"internshipCompleted",
					"Internship Completed",
					FieldType.BOOLEAN,
					false
			);

			createAcademicFieldIfNotExists(
					academicProfileFieldRepository,
					"major",
					"Major",
					FieldType.TEXT,
					false
			);

		};
	}

	/* ===================== HELPERS ===================== */

	private void createRoleIfNotExists(String roleName) {
		if (roleDao.findByName(roleName).isEmpty()) {
			roleDao.save(new Role(roleName));
		}
	}

	private void createAcademicFieldIfNotExists(
			AcademicProfileFieldRepository repo,
			String name,
			String label,
			FieldType type,
			boolean required
	) {
		repo.findByName(name).ifPresentOrElse(
				f -> {},
				() -> {
					AcademicProfileField field = new AcademicProfileField();
					field.setName(name);
					field.setLabel(label);
					field.setType(type);
					field.setRequired(required);
					repo.save(field);
				}
		);
	}
}
