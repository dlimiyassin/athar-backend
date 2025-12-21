package com.student.career.zBase.service;

import com.student.career.zBase.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public Resource getImageByPath(String path) throws MalformedURLException {
        Path imagePath = Paths.get(path);
        Resource resource = new UrlResource(imagePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Image not found or not readable: " + path);
        }

    }

    @Override
    public void deleteImageByPath(String path) {
        FileUtil.deleteFile(path);
    }
}
