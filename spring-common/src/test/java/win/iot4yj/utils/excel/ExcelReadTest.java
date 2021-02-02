package win.iot4yj.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.DateTimeException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
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


    @Test
    public void readCardBin() throws Exception {

        Map<String, String> bankNoMap = new HashMap<>(16);
        bankNoMap.put("工商银行","102100099996");
        bankNoMap.put("农业银行","103100000026");
        bankNoMap.put("中国银行","104100000004");
        bankNoMap.put("建设银行","105100000017");
        bankNoMap.put("交通银行","301290000007");
        bankNoMap.put("兴业银行","309391000011");
        bankNoMap.put("光大银行","303100000006");
        bankNoMap.put("浦发银行","310290000013");
        bankNoMap.put("平安银行","313584099990");
        bankNoMap.put("广发银行","306581000003");
        bankNoMap.put("华夏银行","304100040000");
        bankNoMap.put("中信银行","302100011000");
        bankNoMap.put("邮储银行","403100000004");

        FileInputStream in = new FileInputStream("./cardbin.xls");
        Workbook excel03 = new HSSFWorkbook(in);
        Sheet sheet1 = excel03.getSheet("sheet3");

        FileOutputStream out = new FileOutputStream("./carbin_rdcn.sql");

        //获取内容
        int rowCount = sheet1.getPhysicalNumberOfRows();
        int rowSum = 0;
        for (int rowNum = 0; rowNum < rowCount; rowNum++) {
            rowSum++;
            Row rowData = sheet1.getRow(rowNum);
            if (null != rowData) {

                Cell cellCardBin = rowData.getCell(13);
                String cardBin = cellCardBin.getStringCellValue();

                Cell cellLength = rowData.getCell(12);
                int cardBinLength = Integer.parseInt(cellLength.getStringCellValue());

                Cell cellName = rowData.getCell(17);
                String bankName = cellName.getStringCellValue();

                String clearBankNo = bankNoMap.get(bankName);
                if (StringUtils.isEmpty(clearBankNo)) {
                    System.out.println("银行名称：" + bankName);
                    return;
                }

                String sql = "INSERT INTO wfts_fcts_cardbin_info (card_bin,bank_name,card_bin_length,card_type,clear_bank_no,webank_code,"
                    + "status,prodcut_type,cooper_branch_code,create_time,update_time) VALUES "
                    + "('" + cardBin + "','" + bankName + "'," + cardBinLength + ",'1','" + clearBankNo
                    + "','',NULL,'100000','100000',now(),now());\n";


                out.write(sql.getBytes());
            }
        }
        System.out.println("总行数：" + rowSum);
        in.close();
    }

}
