package ma.cimr.agmbackend.assembly;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ma.cimr.agmbackend.action.Action;

@Service
public class AssemblyExcelServiceImpl implements AssemblyExcelService {

	@Override
	public List<Action> readActionsFromExcel(MultipartFile file) throws IOException {
		List<Action> actions = new ArrayList<>();

		try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

			for (Row row : sheet) {
				if (row.getRowNum() < 7) { // Skip the header rows, adjust as needed
					continue;
				}

				Cell firstCell = row.getCell(1);
				if (firstCell == null || firstCell.getCellType() == CellType.BLANK) {
					// Skip empty rows
					continue;
				}

				Action action = new Action();
				action.setName(getCellValue(row.getCell(1))); // Assuming the description is in the 2nd column (index 1)
				actions.add(action);
			}
		}

		return actions;
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
