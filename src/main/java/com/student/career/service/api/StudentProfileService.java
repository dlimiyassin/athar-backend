package com.student.career.service.api;

import com.student.career.bean.AcademicProfile;
import com.student.career.bean.Student;

public interface StudentProfileService {

    Student getByUserId(String userId);

    Student updateAcademicProfile(
            String studentId,
            AcademicProfile profile
    );
}

