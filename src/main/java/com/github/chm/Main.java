package com.github.chm;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenhuaming on 16/7/13.
 */
public class Main {
	public static void main(String[] args) {
		Set<String> set1 = new HashSet<>();
		Set<String> set2 = new HashSet<>();
		Set<String> set3 = new HashSet<>();
		InputStream in = Main.class.getClassLoader().getResourceAsStream("HFRZ_车款表最新.xlsx");
		Workbook workbook = null;
		OutputStreamWriter out = null;
		try {
			String sqlTemplate = "insert into bms_data_dictionary (type_list_id, type_list_group,type_list_name,type_list_code, type_list_pinying, type_list_jianpin,type_list_code_autoLogo,type_list_code_subLogo,type_list_remark)"
					+ "values" + "(%s, '%s', '%s', %d, '%s', '%s', %d, %d, '%s');";

			workbook = WorkbookFactory.create(in);
			Sheet sheet = workbook.getSheetAt(0);
			int rowCount = sheet.getLastRowNum() + 1;
			for (int i = 3; i < rowCount; i++) {
				Row row = sheet.getRow(i);
				String autoName = "";
				String subName = "";
				String yearName = "";
				int autoCode = (int) row.getCell(1).getNumericCellValue();
				int subCode = (int) row.getCell(3).getNumericCellValue();
				int yearCode = (int) row.getCell(5).getNumericCellValue();
				if (row.getCell(2) != null) {
					autoName = row.getCell(2).getStringCellValue();
					autoName = autoName == null ? "" : autoName.trim();
				}
				if (autoCode == 0) {
					autoName = "其它";
				}
				if (row.getCell(4) != null) {
					try {
						subName = row.getCell(4).getStringCellValue();
					} catch (IllegalStateException e) {
						subName = String.valueOf((int)row.getCell(4).getNumericCellValue());
					}
					subName = subName == null ? "" : subName.trim();
				}
				if (subCode == 0) {
					subName = "其它";
				}
				if (row.getCell(6) != null) {
					yearName = row.getCell(6).getStringCellValue();
					yearName = yearName == null ? "" : yearName.trim();
					yearName = yearName.substring(yearName.lastIndexOf("-") + 1, yearName.length());
				}
				if (yearCode == 0) {
					yearName = "其它";
				}
				autoName = autoName.trim();
				subName = subName.trim();
				yearName = yearName.trim();
				String s1 = String.format(sqlTemplate, "S_data_dictionary.nextval", "autologos", autoName, autoCode,
						getPinyin(autoName), getJianpin(autoName), null, null, getRemark(autoName));
				String s2 = String.format(sqlTemplate, "S_data_dictionary.nextval", "sublogos", subName, subCode, "",
						"", autoCode, null, null);
				String s3 = String.format(sqlTemplate, "S_data_dictionary.nextval", "years", yearName, yearCode, "",
						"", autoCode, subCode, null);
				set1.add(s1);
				set2.add(s2);
				set3.add(s3);
				// System.out.println(i+" "+autoName + "\t\t" + autoCode +
				// "\t\t" + subName + "\t" + subCode + "\t\t" + yearName +
				// "\t\t"+ yearCode);
			}
			System.out.println(set1.size());
			System.out.println(set2.size());
			System.out.println(set3.size());
			out = new OutputStreamWriter(new FileOutputStream("sql.txt"), "UTF-8");
			out.write("delete from bms_data_dictionary where type_list_group='autologos' or type_list_group='sublogos' or type_list_group='sublogos';"+"\n");
			for(String str : set1){
				out.write(str);
				out.write("\n");
			}
			for(String str : set2){
				out.write(str);
				out.write("\n");
			}
			for(String str : set3){
				out.write(str);
				out.write("\n");
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {

			}
		}
	}

	private static String getPinyin(String in) {
		in = in.replaceAll("长", "常");
		return PingyinTool.getPingYin(in).toUpperCase();
	}

	private static String getJianpin(String in) {
		in = in.replaceAll("长", "常");
		return PingyinTool.getPinYinHeadChar(in).toUpperCase();
	}

	private static String getRemark(String in) {
		in = in.replaceAll("长", "常");
		return PingyinTool.getPinYinHeadChar(in).substring(0, 1).toUpperCase();
	}
}
