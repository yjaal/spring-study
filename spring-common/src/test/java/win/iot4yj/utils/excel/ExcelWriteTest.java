package win.iot4yj.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class ExcelWriteTest {

    /**
     * 基本写
     */
    @Test
    public void write03Test() throws Exception {
        //1、创建一个excel
        Workbook excel03 = new HSSFWorkbook();
        //2、创建一个sheet
        Sheet sheet1 = excel03.createSheet("第一个sheet");
        //3、创建一个行，第一行
        Row row1 = sheet1.createRow(0);
        //4、创建第一列，这样就取到了第一个单元格
        Cell cell11 = row1.createCell(0);
        cell11.setCellValue("第一个单元格");

        OutputStream out = new FileOutputStream(new File("./03excel.xls"));
        excel03.write(out);
        out.close();
    }

    /**
     * 基本写
     */
    @Test
    public void write07Test() throws Exception {
        //1、创建一个excel
        Workbook excel07 = new XSSFWorkbook();
        //2、创建一个sheet
        Sheet sheet1 = excel07.createSheet("第一个sheet");
        //3、创建一个行，第一行
        Row row1 = sheet1.createRow(0);
        //4、创建第一列，这样就取到了第一个单元格
        Cell cell11 = row1.createCell(0);
        cell11.setCellValue("第一个单元格");
        // 07版本到excel和03版的格式不同
        OutputStream out = new FileOutputStream(new File("./07excel.xlsx"));
        excel07.write(out);
        out.close();
    }

    /**
     * 大文件写，03版本的excel最多只能处理65536行，会将所有数据加载到内存，然后一次性写入磁盘, 写入较快， 但是容易内存溢出
     */
    public void excel03Big() throws Exception {
        long begin = System.currentTimeMillis();
        Workbook excel03 = new HSSFWorkbook();
        Sheet sheet = excel03.createSheet();
        for (int rownNum = 0; rownNum < 65536; rownNum++) {
            Row row = sheet.createRow(rownNum);
            for (int cellNum = 0; cellNum < 10; cellNum++) {
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(cellNum);
            }
        }
        FileOutputStream out = new FileOutputStream("./excel03big.xls");
        excel03.write(out);
        out.close();
    }

    /**
     * 这里大文件是一行一行写入的，比较耗时，也很消耗内存
     */
    public void excel07Big() throws Exception {
        long begin = System.currentTimeMillis();
        Workbook excel07 = new XSSFWorkbook();
        Sheet sheet = excel07.createSheet();
        for (int rownNum = 0; rownNum < 65537; rownNum++) {
            Row row = sheet.createRow(rownNum);
            for (int cellNum = 0; cellNum < 10; cellNum++) {
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(cellNum);
            }
        }
        FileOutputStream out = new FileOutputStream("./excel07big.xlsx");
        excel07.write(out);
        out.close();
    }

    /**
     * 07版大文件写入优化，每次将一定数量的内容写入到一个临时文件中
     */
    public void excel07ProBig() throws Exception {
        long begin = System.currentTimeMillis();
        Workbook excel07 = new SXSSFWorkbook();
        Sheet sheet = excel07.createSheet();
        for (int rownNum = 0; rownNum < 65537; rownNum++) {
            Row row = sheet.createRow(rownNum);
            for (int cellNum = 0; cellNum < 10; cellNum++) {
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(cellNum);
            }
        }
        FileOutputStream out = new FileOutputStream("./excel07proBig.xlsx");
        excel07.write(out);
        //清除临时文件
        ((SXSSFWorkbook)excel07).dispose();
        out.close();
    }
}
