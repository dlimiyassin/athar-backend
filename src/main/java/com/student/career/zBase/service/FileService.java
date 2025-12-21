package com.student.career.zBase.service;

import org.springframework.core.io.Resource;

import java.net.MalformedURLException;

public interface FileService {

    Resource getImageByPath(String path) throws MalformedURLException;

    void deleteImageByPath(String path);
}
