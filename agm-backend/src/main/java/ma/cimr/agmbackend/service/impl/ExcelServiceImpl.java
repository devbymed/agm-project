package ma.cimr.agmbackend.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ma.cimr.agmbackend.model.Action;
import ma.cimr.agmbackend.service.ExcelService;

@Service
public class ExcelServiceImpl implements ExcelService {

	@Override
	public List<Action> readActionsFromExcel(MultipartFile file) throws IOException {
		List<Action> actions = new ArrayList<>();
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy");

		try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

			for (Row row : sheet) {
				if (row.getRowNum() < 7) { // Skip the header rows, adjust as needed
					continue;
				}

				Cell firstCell = row.getCell(0);
				if (firstCell == null || firstCell.getCellType() == CellType.BLANK) {
					// Skip empty rows
					continue;
				}

				Action action = new Action();
				action.setDescription(getCellValue(row.getCell(1)));
				String responsible = getCellValue(row.getCell(5));
				if (responsible == null || responsible.isEmpty()) {
					responsible = "Unknown"; // Provide a default value
				}
				action.setResponsible(responsible);

				// Check and parse the date fields, provide default if necessary
				String startDateStr = getCellValue(row.getCell(2));
				if (startDateStr != null && !startDateStr.isEmpty()) {
					action.setStartDate(parseDate(startDateStr, formatter1, formatter2));
				} else {
					action.setStartDate(LocalDate.now()); // Default start date to today
				}

				String endDateStr = getCellValue(row.getCell(3));
				if (endDateStr != null && !endDateStr.isEmpty()) {
					action.setEndDate(parseDate(endDateStr, formatter1, formatter2));
				} else {
					action.setEndDate(LocalDate.now().plusDays(1)); // Default end date to tomorrow
				}

				String realizationDateStr = getCellValue(row.getCell(4));
				if (realizationDateStr != null && !realizationDateStr.isEmpty()) {
					action.setRealizationDate(parseDate(realizationDateStr, formatter1, formatter2));
				} else {
					action.setRealizationDate(null); // Optional field, can be null
				}

				action.setProgressStatus("Not Started"); // Default value for now
				action.setObservation(getCellValue(row.getCell(7)));
				action.setDeliverable(getCellValue(row.getCell(6)));

				actions.add(action);
			}
		}

		return actions;
	}

	private LocalDate parseDate(String dateStr, DateTimeFormatter... formatters) {
		for (DateTimeFormatter formatter : formatters) {
			try {
				return LocalDate.parse(dateStr, formatter);
			} catch (Exception e) {
				// Continue to try the next formatter
			}
		}
		throw new RuntimeException("Failed to parse date: " + dateStr);
	}

	private String getCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}

		switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getLocalDateTimeCellValue().toLocalDate().toString();
				} else {
					return String.valueOf(cell.getNumericCellValue());
				}
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case FORMULA:
				return cell.getCellFormula();
			case BLANK:
				return null;
			default:
				return null;
		}
	}
}
