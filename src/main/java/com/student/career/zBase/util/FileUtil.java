package com.student.career.zBase.util;

import lombok.SneakyThrows;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileUtil {

    //    @Value("${file.path}")
    private final static String filePath = "uploads";
    private final static String folderPathConsignmentImages = "consignmentImages";
    private final static String folderPathProductImages = "productImages";

    public static synchronized String storeFileLocalImage(MultipartFile file, String prefix, boolean forProduct, boolean forConsignment) throws Exception {
        validateImageFile(file);
        String dir = System.getProperty("user.dir") + "/";
        if (forProduct) {
            dir = folderPathProductImages;
        } else if (forConsignment) {
            dir = folderPathConsignmentImages;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime zonedDateTime = DateUtil.toZonedDateTime(now);
        createDirectoryIfNotExists(dir);
        Assert.notNull(zonedDateTime, "zonedDateTime is null");
        String dirYear = getDirPath(dir, zonedDateTime.getYear());
        createDirectoryIfNotExists(dirYear);
        String dirMonth = getDirPath(dirYear, zonedDateTime.getMonthValue());
        createDirectoryIfNotExists(dirMonth);
        String dirDay = getDirPath(dirMonth, zonedDateTime.getDayOfMonth());
        createDirectoryIfNotExists(dirDay);
        String finalDir = dirDay + "/" + prefix;
        createDirectoryIfNotExists(finalDir);
        String fileToImport = finalDir + "/" + prefix + "_" + now.getHour() + "_" + now.getMinute() + "_" + now.getSecond() + "_" + now.getNano() + "." + Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
        file.transferTo(new File(fileToImport).toPath());
        return fileToImport;
    }

    private static String getDirPath(String dir, int value) {
        return dir + "/" + value;
    }

    private static void createDirectoryIfNotExists(String dir) {
        File directory = new File(dir);
        if (!directory.exists() && !directory.mkdir()) {
            throw new RuntimeException("Failed to create directory: " + dir);
        }
    }

    private static void validateImageFile(MultipartFile file) {
        if (!Objects.equals(file.getContentType(), "image/jpeg") && !Objects.equals(file.getContentType(), "image/png")) {
            throw new IllegalArgumentException("Invalid file format");
        }
        ;
    }

    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file");
        }
    }

    private static boolean isValidExcel(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    private static boolean isValidCsv(MultipartFile file) {
        return Objects.equals(file.getContentType(), "text/csv");
    }

    @SneakyThrows
    public static void deleteFiles(String filePath) {
        String[] strings = filePath.split("/");
        String directoryPath = filePath.replace(strings[strings.length - 1], "");
        directoryPath = directoryPath.substring(0, directoryPath.length() - 1);
        Path dirPath = Paths.get(directoryPath);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dirPath)) {
            for (Path path : directoryStream) {
                if (Files.isRegularFile(path)) {
                    Files.delete(path);
                }
            }
        }
    }

    // Utility method to trim trailing commas from each line in the CSV file
    private static void trimTrailingCommas(String csvFilePath) throws IOException {
        Path path = Paths.get(csvFilePath);
        List<String> lines = Files.readAllLines(path);
        List<String> trimmedLines = lines.stream()
                .map(line -> line.replaceAll(",+$", "")) // Remove trailing commas
                .collect(Collectors.toList());
        Files.write(path, trimmedLines);
    }
}
