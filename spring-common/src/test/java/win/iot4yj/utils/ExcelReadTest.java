package win.iot4yj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.DateTimeException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.junit.Test;

public class ExcelReadTest {

    /**
     * 基本读
     */
    @Test
    public void read03Test() throws Exception {
        FileInputStream in = new FileInputStream("./03excel.xls");
        Workbook excel03 = new HSSFWorkbook(in);
        Sheet sheet1 = excel03.getSheet("第一个sheet");
        Row row1 = sheet1.getRow(0);
        Cell cell11 = row1.getCell(0);
        String str = cell11.getStringCellValue();
        System.out.println(str);
        in.close();
    }

    /**
     * 基本读
     */
    @Test
    public void read07Test() throws Exception {
        FileInputStream in = new FileInputStream("./07excel.xlsx");
        Workbook excel03 = new XSSFWorkbook(in);
        Sheet sheet1 = excel03.getSheet("第一个sheet");
        Row row1 = sheet1.getRow(0);
        Cell cell11 = row1.getCell(0);
        String str = cell11.getStringCellValue();
        System.out.println(str);
        in.close();
    }

    @Test
    public void types03Test() throws Exception {
        FileInputStream in = new FileInputStream("./03excel.xls");
        Workbook excel03 = new HSSFWorkbook(in);
        Sheet sheet1 = excel03.getSheet("第一个sheet");
        Row rowTitle = sheet1.getRow(0);
        //获取表头
        for (int cellNum = 0; cellNum < rowTitle.getPhysicalNumberOfCells(); cellNum++) {
            Cell cell = rowTitle.getCell(cellNum);
            if (null != cell) {
                int cellType = cell.getCellType();
                String cellValue = cell.getStringCellValue();
                System.out.print(cellValue + " | ");
            }
        }
        System.out.println();

        //获取内容
        int rowCount = sheet1.getPhysicalNumberOfRows();
        for (int rowNum = 0; rowNum < rowCount; rowNum++) {
            Row rowData = sheet1.getRow(rowNum);
            if (null != rowData) {
                //读取列
                int cellCount = rowTitle.getPhysicalNumberOfCells();
                for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                    System.out.print("[" + (rowNum + 1) + "-" + (cellNum + 1) + "]");
                    Cell cell = rowData.getCell(cellNum);
                    if (cell != null) {
                        int cellType = cell.getCellType();
                        String cellValue = "";
                        switch (cellType) {
                            case HSSFCell.CELL_TYPE_STRING:
                                System.out.println("[String]");
                                cellValue = cell.getStringCellValue();
                                break;
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    System.out.println("[date]");
                                    cellValue = new DateTime(cell.getDateCellValue()).toString();
                                } else {
                                    //不是日期格式，需要注意数字到长度
                                    System.out.println("[number]");
                                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                    cellValue = cell.toString();
                                }
                                break;
                            case HSSFCell.CELL_TYPE_BLANK:
                                System.out.println("[Blank]");
                                break;
                            case HSSFCell.CELL_TYPE_BOOLEAN:
                                System.out.println("[Boolean]");
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_ERROR:
                                System.out.println("[error]");
                                break;
                        }
                        System.out.println(cellValue);
                    }
                }
            }
        }
        in.close();
    }


    /**
     * 获取excel中的公式，并计算出值
     */
    @Test
    public void calculate07Test() throws Exception {
        FileInputStream in = new FileInputStream("./07excel.xlsx");
        Workbook excel07 = new XSSFWorkbook(in);
        Sheet sheet = excel07.getSheetAt(0);
        Row row = sheet.getRow(4);
        Cell cell = row.getCell(0);
        FormulaEvaluator evaluator = new XSSFFormulaEvaluator((XSSFWorkbook) excel07);
        int cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_FORMULA:
                String formula = cell.getCellFormula();
                System.out.println(formula);

                //计算
                CellValue evaluate = evaluator.evaluate(cell);
                String cellValue = evaluate.formatAsString();
                System.out.println(cellValue);
                break;
        }
        in.close();
    }
}
