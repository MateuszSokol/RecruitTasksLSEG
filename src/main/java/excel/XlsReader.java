package excel;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.text.DateFormatter;
import java.io.FileInputStream;
import java.util.Iterator;

public class XlsReader {
    private static final String NAME =  "C:\\spryng\\RecruitTasksLSEG-master\\src\\main\\java\\Excel file.xls";


    public void Reader (){
        String excelPiece ="";
        String [] subjects = new String[13];
        StringBuilder excelDate = new StringBuilder();
        String [] hours = new String[24];
        int iterator = 0;
        int arrayIterator= 0;
        int hourArrIterator = 0;
        int rowCounter =0;
        int hoursArrIndex = 0;
        int subjectIterator = 0;
        int cnt;

        try{
            FileInputStream file = new FileInputStream(NAME);
            Workbook wb = WorkbookFactory.create(file);
            DataFormatter dataFormatter = new DataFormatter();

            Iterator<Sheet> sheetIterator = wb.sheetIterator();

            while(sheetIterator.hasNext()){
                Sheet sheet = sheetIterator.next();
                Iterator<Row> rowIterator = sheet.iterator();

                while (rowIterator.hasNext()){
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.iterator();
                    rowCounter++;
                    cnt =0;

                    while (cellIterator.hasNext()){
                        Cell cell = cellIterator.next();
                        if(!cell.toString().equalsIgnoreCase("")){
                            String cellValue = dataFormatter.formatCellValue(cell);

                            if(iterator ==0){
                                excelPiece = cellValue.substring(cellValue.length()-10);
                                excelDate.append(excelPiece);
                            }
                            iterator++;
                            if(iterator >=4){
                                excelPiece = cellValue;
                                if(arrayIterator<subjects.length){
                                    subjects[arrayIterator] = excelPiece;
                                    arrayIterator ++;
                                }


                            }

                            if(rowCounter>=6){

                                cnt++;

                                if(cnt ==1){
                                    if(hourArrIterator<hours.length){
                                        int hoursValue = Integer.parseInt(cellValue)-1;
                                        String help = String.valueOf(hoursValue);
                                        hours[hourArrIterator] = help;
                                        hourArrIterator++;
                                        hoursArrIndex=hourArrIterator-1;
                                        if(Integer.parseInt(hours[hoursArrIndex]) <10){
                                            hours[hoursArrIndex] = "0"+hours[hoursArrIndex] + ":00";
                                        }else{
                                            hours[hoursArrIndex]= hours[hoursArrIndex] + ":00";
                                        }
                                    }

                                }

                                if(cnt!=1){
                                    for (int i = subjectIterator; i < subjects.length;) {

                                       subjects[i]= subjects[i].replace("\n","");
                                       int indexOfBracket = subjects[i].indexOf("(");
                                       int lastindexOfBracket = subjects[i].lastIndexOf(")");

                                       if(indexOfBracket>0 && lastindexOfBracket >1){
                                           String holder = subjects[i].substring(indexOfBracket,lastindexOfBracket+1);
                                           subjects[i] =subjects[i].replace(holder,"");
                                       }


                                        System.out.println(subjects[i] + "; " + excelDate +" "+ hours[hoursArrIndex] +"; "+excelPiece);
                                        break;
                                    }
                                    subjectIterator++;
                                }
                            }

                            if(subjectIterator == subjects.length-1){
                                subjectIterator = 0;
                            }

                        }
                    }

                }
            }

            wb.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
