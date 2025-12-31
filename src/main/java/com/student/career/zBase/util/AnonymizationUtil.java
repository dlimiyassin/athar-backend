package com.student.career.zBase.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class AnonymizationUtil {

    @Value("${export.anonymization.salt}")
    private String salt;

    public String anonymizeStudentId(String studentId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = studentId + ":" + salt;
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(hash)
                    .substring(0, 16); // short but safe
        } catch (Exception e) {
            throw new IllegalStateException("Anonymization failed", e);
        }
    }
}
