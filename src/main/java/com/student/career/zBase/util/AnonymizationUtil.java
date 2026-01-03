package com.student.career.zBase.util;


import com.student.career.ImportExportCsv.beans.StudentAnonymizationMap;
import com.student.career.ImportExportCsv.dao.StudentAnonymizationMapRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
@Component
public class AnonymizationUtil {

    @Value("${export.anonymization.salt}")
    private String salt;

    private final StudentAnonymizationMapRepository mapRepository;

    public AnonymizationUtil(StudentAnonymizationMapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    public String anonymizeStudentId(String studentId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = studentId + ":" + salt;
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            String anonymousId = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(hash)
                    .substring(0, 16);

            // persist mapping if not exists
            mapRepository.findByAnonymousId(anonymousId)
                    .orElseGet(() -> {
                        StudentAnonymizationMap map =
                                new StudentAnonymizationMap();
                        map.setStudentId(studentId);
                        map.setAnonymousId(anonymousId);
                        return mapRepository.save(map);
                    });

            return anonymousId;

        } catch (Exception e) {
            throw new IllegalStateException("Anonymization failed", e);
        }
    }

    public String resolveStudentId(String anonymousId) {
        return mapRepository.findByAnonymousId(anonymousId)
                .map(StudentAnonymizationMap::getStudentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unknown anonymous student id: " + anonymousId
                        )
                );
    }
}
