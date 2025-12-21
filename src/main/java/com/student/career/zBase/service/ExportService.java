package com.student.career.zBase.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface ExportService {

    void generatePdf(HttpServletResponse response, List<?> list, List<String> showFields) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    void generateExcel(HttpServletResponse response, List<?> list, List<String> showFields) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException;

    void generateCsv(HttpServletResponse response, List<?> list, List<String> showFields) throws IOException;
}
