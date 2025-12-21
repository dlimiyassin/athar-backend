package com.student.career.zBase.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.librepdf.openpdf.fonts.Liberation;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class ExportServiceImpl implements ExportService {

    public static final String CSV_CONTENT_TYPE = "text/csv";
    public static final String CSV_FORMAT = "csv";
    public static final String HEADER_KEY = "Content-Disposition";
    public static final String PDF_CONTENT_TYPE = "application/pdf";
    public static final String PDF_FORMAT = "pdf";
    public static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String EXCEL_FORMAT = "xlsx";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;


    @Override
    public void generatePdf(HttpServletResponse response, List<?> list, List<String> fieldsShow) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        String fileName = prepareResponse(response, list, PDF_CONTENT_TYPE, PDF_FORMAT);

        // Creating the Object of Document
        Document document = new Document(PageSize.A4.rotate());

        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());

        // Opening the created document to modify it
        document.open();

        // Creating font
        // Setting font style and size
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTitle.setSize(20);

        // Creating paragraph
        Paragraph paragraph = new Paragraph("List Of " + fileName, fontTitle);

        // Aligning the paragraph in document
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        // Adding the created paragraph in document
        document.add(paragraph);

        // Creating a table of 3 columns

        int length = getLength(list);
        String[] headers = getHeader(list, length);
        PdfPTable table = new PdfPTable(headers.length);

        // Setting width of table, its columns and spacing
        table.setWidthPercentage(100f);
        int[] widths = new int[headers.length];
        Arrays.fill(widths, 8);
        table.setWidths(widths);
        table.setSpacingBefore(5);
        table.setSpacingAfter(5);

        // Create Table Cells for table header
        PdfPCell cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);

        // Setting the background color and padding
        cell.setBackgroundColor(CMYKColor.decode("#1DA8D8"));
        cell.setPadding(5);

        // Creating font with the calculated font size
        Font font = FontFactory.getFont(Liberation.MONO_BOLD.name());
        font.setSize(4);
        font.setColor(CMYKColor.WHITE);

        // Adding Cell to table
        writeHeaderInPdf(headers, cell, font, table);
        // Create a counter
        int rowCounter = 0;
        // Iterating over the list
        writeDataInPdf(list, headers.length, rowCounter, table, fieldsShow);
        // Adding the created table to document
        document.add(table);

        // Closing the document
        document.close();
    }

    private void writeHeaderInPdf(String[] headers, PdfPCell cell, Font font, PdfPTable table) {
        Stream.of(headers)
                .forEach(headerTitle -> {
                    cell.setPhrase(new Phrase(headerTitle, font));
                    table.addCell(cell);
                });
    }

    private void writeDataInPdf(List<?> list, int length, int rowCounter, PdfPTable table, List<String> fieldsShow) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (Object o : list) {
            for (int i = 1; i <= length; i++) {
                Field field = o.getClass().getDeclaredFields()[i];
                field.setAccessible(true);
                Object value = field.get(o);
                // Create a new cell for each value
                PdfPCell valueCell = new PdfPCell();
                createPdfCell(value, valueCell, rowCounter, fieldsShow);
                // Add the cell to the table
                table.addCell(valueCell);
            }
            // Increment the counter after each row
            rowCounter++;
        }
    }

    private void createPdfCell(Object value, PdfPCell valueCell, int rowCounter, List<String> fieldsShow) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        value = cellValue(value, fieldsShow);
        Font font = FontFactory.getFont(Liberation.SANS_BOLD.name());
        font.setSize(6f);
        font.setColor(CMYKColor.BLACK);
        valueCell.setPhrase(new Phrase(value.toString(), font));
        valueCell.setBorder(PdfPCell.NO_BORDER);
        valueCell.setPadding(3);
        // Set the background color based on the row number
        if (rowCounter % 2 == 0) {
            valueCell.setBackgroundColor(CMYKColor.LIGHT_GRAY);
        } else {
            valueCell.setBackgroundColor(CMYKColor.WHITE);
        }
    }

    @Override
    public void generateExcel(HttpServletResponse response, List<?> list, List<String> fieldsShow) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {

        String fileName = prepareResponse(response, list, EXCEL_CONTENT_TYPE, EXCEL_FORMAT);
        // Create a new Excel workbook and sheet
        workbook = new XSSFWorkbook();
        writeHeaderLine(fileName, list, fieldsShow);
        writeDataLines(list, fieldsShow);


        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
        } finally {
            workbook.close();
        }
    }


    private void writeHeaderLine(String fileName, List<?> list, List<String> fieldsShow) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        sheet = workbook.createSheet(fileName);
        Row row = sheet.createRow(0);

        // Create cell style for header
        CellStyle headerStyle = createHeaderStyle();

        int length = getLength(list);
        String[] headers = getHeader(list, length);
        int coloumCount = 0;
        for (String header : headers) {
            createCell(row, coloumCount++, header, headerStyle, fieldsShow);
        }

        // Autosize columns after all data is written
        for (int i = 0; i < length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle createHeaderStyle() {
        CellStyle headerStyle = this.workbook.createCellStyle();
        XSSFFont headerFont = this.workbook.createFont();
        headerFont.setFontHeight(16);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    private int getLength(List<?> list) {
        int length = list.getFirst().getClass().getDeclaredFields().length - 1;
        CharSequence[] listOfCollection = {"List", "Set", "Map", "HashMap", "HashSet"};
        for (Field field : list.getFirst().getClass().getDeclaredFields()) {
            field.setAccessible(true);
            for (CharSequence charSequence : listOfCollection) {
                if (field.getType().getSimpleName().contains(charSequence)) {
                    length--;
                    break;
                }
            }
        }
        return length;
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style, List<String> fieldsShow)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        value = cellValue(value, fieldsShow);
        cell.setCellValue(value.toString());
        cell.setCellStyle(style);
    }

    public Object cellValue(Object value, List<String> fieldsShow) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return switch (value) {
            case String s -> s;
            case Long l -> l;
            case BigDecimal bd -> bd;
            case LocalDateTime localDateTime -> localDateTime.toString();
            case Boolean b -> b;
            case Integer i -> i;
            case Double d -> d;
            case Float f -> f;
            case Enum<?> e -> e;
            case null -> "Null";
            default -> {
                Object result = "Unknown";
                for (String fieldShow : fieldsShow) {
                    String[] strings = fieldShow.split("\\.");
                    if (strings[0].equals(value.getClass().getSimpleName())) {
                        value = value.getClass().getMethod("get" + strings[1]).invoke(value);
                        result = value.toString();
                        break;
                    }
                }
                yield result;
            }
        };
    }

    private void writeDataLines(List<?> list, List<String> fieldShow) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        int rowCount = 1;

        // Create cell style for data
        CellStyle dataStyle = createDataStyle();

        int length = getLength(list);
        String[] header = getHeader(list, length);
        length = header.length;
        for (Object o : list) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (int i = 1; i <= length; i++) {
                Field field = o.getClass().getDeclaredFields()[i];
                field.setAccessible(true);
                Object value = field.get(o);

                createCell(row, columnCount++, value, dataStyle, fieldShow);

            }
        }
    }


    private CellStyle createDataStyle() {
        CellStyle dataStyle = workbook.createCellStyle();
        XSSFFont dataFont = workbook.createFont();
        dataFont.setFontHeight(14);
        dataStyle.setFont(dataFont);
        return dataStyle;
    }

    private String[] getHeader(List<?> list, int length) {
        String[] headers = new String[length];
        if (length > 0) {
            CharSequence[] listOfCollection = {"List", "Set", "Map", "HashMap", "HashSet"};
            parentFor:
            for (int i = 1; i <= length; i++) {
                for (CharSequence charSequence : listOfCollection) {
                    if (list.getFirst().getClass().getDeclaredFields()[i].getType().getSimpleName().contains(charSequence)) {
                        continue parentFor;
                    }
                }
                String name = list.getFirst().getClass().getDeclaredFields()[i].getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                if (name.contains("Dto")) {
                    name = name.replace("Dto", "");
                    String firstChar = String.valueOf(name.charAt(0));
                    name = name.substring(1);
                    for (char c : name.toCharArray()) {
                        if (Character.isUpperCase(c)) {
                            name = name.replace(c + "", " " + c);
                        }
                    }
                    name = firstChar + name;
                }
                headers[i - 1] = name;
            }
        }
        return headers;
    }


    @Override
    public void generateCsv(HttpServletResponse response, List<?> list, List<String> showFields) throws IOException {
        prepareResponse(response, list, CSV_CONTENT_TYPE, CSV_FORMAT);
        int length = getLength(list);
        String[] header = getHeader(list, length);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < header.length; i++) {
            sb.append(header[i]);
            if (i < header.length - 1) {
                sb.append(",");
            } else {
                sb.append("\n");
            }
        }

        response.getWriter().write(String.valueOf(sb));
        for (Object object : list) {
            String row = generateCSVRow(object, showFields, header.length);
            response.getWriter().write(row + "\n");
        }
    }

    private String generateCSVRow(Object object, List<String> showFields, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            Field field = object.getClass().getDeclaredFields()[i];
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                value = cellValue(value, showFields);
                sb.append(value);
                if (i < length) {
                    sb.append(",");
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                log.error("Error generating CSV row", e);
                throw new RuntimeException("Error generating CSV row");
            }
        }
        return sb.toString();
    }


    String prepareResponse(HttpServletResponse response, List<?> list, String contentType, String format) {
        String fileName = list.getFirst().getClass().getSimpleName();
        fileName = fileName.replace("Dto", "s");

        response.setContentType(contentType);
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formattedDateTime = currentDateTime.format(formatter);
        String headerValue = "attachment; filename=" + fileName + "_" + formattedDateTime + "." + format;
        response.setHeader(HEADER_KEY, headerValue);
        return fileName;
    }
}

