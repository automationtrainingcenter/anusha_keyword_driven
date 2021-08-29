package utilities;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelHelper {
    private Workbook book;
    private Sheet sheet;

    // open excel file to read the data
    public void openExcel(String folderName, String fileName, String sheetName) {
        try {
            // create an object FileInputStream class
            FileInputStream fis = new FileInputStream(DriverConfig.getFilePath(folderName, fileName));
            // verify the extension of file name
            if(fileName.endsWith("xls")){
                book = new HSSFWorkbook(fis);
            } else if(fileName.endsWith("xlsx")){
                book = new XSSFWorkbook(fis);
            } else {
                System.out.println("Invalid file extension");
            }
            this.sheet = book.getSheet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // find the number of rows
    public int getRows(){
        return this.sheet.getLastRowNum();
    }

    // find the number of columns
    public int getColumns(){
        return this.sheet.getRow(0).getLastCellNum();
    }

    // read the data from the given row and column
    public String readData(int rnum, int cnum) {
        String data = "";
        try {
            Cell cell = this.sheet.getRow(rnum).getCell(cnum);
            CellType cellType = cell.getCellType();

            switch (cellType){
                case STRING:
                    data = cell.getStringCellValue();
                    break;
                case BOOLEAN:
                    data = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case NUMERIC:
                    int i = (int) cell.getNumericCellValue();
                    data = Integer.toString(i);
                    break;
                default:
                    data = "";
                    break;
            }

        } catch (NullPointerException e) {

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return data;
    }


    // close the book
    public void closeBook() {
        try {
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        ExcelHelper excel = new ExcelHelper();
//        excel.openExcel("resources", "TestCase.xlsx", "testcases");
//        int rows = excel.getRows();
//        int columns = excel.getColumns();
//        System.out.println(rows);
//        for(int i = 0; i<=rows; i++) {
//            for(int j=0; j<columns; j++){
//                System.out.println(excel.readData(i, j));
//            }
//        }
//        excel.closeBook();
//    }

}
