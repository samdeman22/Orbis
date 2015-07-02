package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFile {
	
	private File file;
	private FileInputStream fis;
	private FileOutputStream fos;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	private Row row;
	
	public ExcelFile (String dir){
		file = new File(dir);
	}
	
	//dir including the name of file to
	public static void createBlankFile(String dir) throws IOException{
		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Sheet1");
		sheet.setAutobreaks(true);
		FileOutputStream out = new FileOutputStream(dir);
		wb.write(out);
		out.close();
	}
	
	//the Map is just an array of object arrays (rows) with the row identifier as the row number
	public void setData(Map<String, Object[]> data){
		Object[] keys = data.keySet().toArray();
		int rowNum = sheet.getLastRowNum();
		Integer[] keyNums = new Integer[keys.length];
		String[] keyStrings = new String[keys.length];
		
		for (int i = 0; i < keys.length; i++){
			keyNums[i] = Integer.parseInt((String)keys[i]);
		}
		Arrays.sort(keyNums);
		for (int i = 0; i < keys.length; i++){
			keyStrings[i] = keyNums[i].toString();
		}
		
		for (String key : keyStrings){
			row = sheet.createRow(rowNum++);
			Object[] objarr = data.get(key);
			int cellNum = 0;
			for (Object obj : objarr){
				Cell cell = row.createCell(cellNum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Double)
					cell.setCellValue((Double) obj);
				else if (obj instanceof Integer)
					cell.setCellValue(new Double((double) ((Integer) obj).intValue()));
				else if (obj instanceof Float)
					cell.setCellValue( new Double((double) ((Float) obj).floatValue()) );
			}
		}
	}
	
	public Map<String, Object[]> getData() throws Exception{
		Map<String, Object[]> data = null;
		if (fis == null)
			throw new Exception("Null file input stream! Was pull() called before calling getData()?");
		else if (sheet == null)
			throw new Exception("Null spreadsheet! Was pull() called before calling getData()?");
		else {
			data = new HashMap<String, Object[]>();
			Iterator<Row> rowIterator = sheet.iterator();
			Integer rowNum = 0;
			while (rowIterator.hasNext()){
				Iterator<Cell> cellIterator = row.cellIterator();
				row = rowIterator.next();
				ArrayList<Object> objArr = new ArrayList<Object>();
				while (cellIterator.hasNext()){
					objArr.add(cellIterator.next());
				}
				data.put(rowNum.toString(), objArr.toArray());
			}
		}
		
		return data;
	}
	
	//pull should be called to retrieve new excel stuff for getData. Must be called at least once in order to retrieve from getdata() or set to setData()
	public void pull(){
		try{
			fis = new FileInputStream(file);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void push(){
		try {
			if (file != null){
				fos = new FileOutputStream(file);
				workbook.write(fos);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
