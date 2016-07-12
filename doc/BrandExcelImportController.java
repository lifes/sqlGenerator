package com.hikvision.kapu.modules.config.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.hikvision.kapu.common.ConstParameter;
import com.hikvision.kapu.modules.config.domain.DataDictionary;
import com.hikvision.kapu.modules.config.service.IDataDictionaryService;
import com.hikvision.kapu.tools.PingyinTool;

/**
 * 品牌excel导入功能
 * @author luofan 2016年1月6日 下午7:44:21
 * @version V1.0
 * @modify: {原因} by luofan 2016年1月6日 下午7:44:21
 */
@Controller
@RequestMapping("/web")
public class BrandExcelImportController {
	
	@Autowired
	private IDataDictionaryService dateDictionaryService;
	
	private final Logger log = LoggerFactory.getLogger(BrandExcelImportController.class);
	
	@RequestMapping("/BrandExcelImport/import")
	@ResponseBody
	public void exportImport(HttpServletRequest request) {
		try {
			dateDictionaryService.deleteByGroup("autologos");
			dateDictionaryService.deleteByGroup("sublogos");
			dateDictionaryService.deleteByGroup("years");

			//=================== V2.0.0车辆品牌数据字典=========================
			StringBuilder templatePath = new StringBuilder();                                                                    
			templatePath.append(request.getSession().getServletContext().getRealPath("template")).append(File.separator).append("brand_code.xls");
			FileInputStream fileInputStream = new FileInputStream(templatePath.toString());
			Workbook workbook = WorkbookFactory.create(fileInputStream);
			Sheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getLastRowNum()+1;
			for (int i = 2; i < rows; i++) {
				Row row = sheet.getRow(i);
				if (row!=null && row.getCell(1) != null) {
					String autoName = row.getCell(2).getStringCellValue();
					if (StringUtils.isNotBlank(autoName)) {
						Integer autoCode = (int)row.getCell(1).getNumericCellValue();
						DataDictionary dataDictionary = new DataDictionary();
						autoName = autoName.trim();
						dataDictionary.setName(autoName.trim());
						dataDictionary.setCode(autoCode);
						dataDictionary.setGroup("autologos");
						//对长进行特殊处理
						if(autoName.contains("长")){
							if(autoName.substring(0, 1).contains("长")){
								dataDictionary.setRemark("chang".substring(0, 1).toUpperCase());
								dataDictionary.setJianPin(PingyinTool.getPinYinHeadChar(autoName).toUpperCase());
							}else{
								dataDictionary.setRemark(PingyinTool.getPinYinHeadChar(autoName).substring(0, 1).toUpperCase());
								dataDictionary.setJianPin(PingyinTool.getPinYinHeadChar(autoName).toUpperCase());
							}
							dataDictionary.setPinYing(PingyinTool.getPingYin(autoName).toUpperCase().replaceAll("ZHANG", "CHANG"));
						}else{
							dataDictionary.setRemark(PingyinTool.getPinYinHeadChar(autoName).substring(0, 1).toUpperCase());
							dataDictionary.setPinYing(PingyinTool.getPingYin(autoName).toUpperCase());
							dataDictionary.setJianPin(PingyinTool.getPinYinHeadChar(autoName).toUpperCase());
						}
						dateDictionaryService.save(dataDictionary);
					}
				}
			}
			
			//导入子品牌
			Integer lastAutoCode = 0;
			for (int i = 2; i < rows; i++) {
				Row row = sheet.getRow(i);
				if (row!=null && row.getCell(1) != null) {
					Integer autoCode = 0;
					if(row.getCell(1).getCellType() == 1){
						autoCode = NumberUtils.toInt(row.getCell(1).getStringCellValue());
					}else{
						autoCode = 	(int)row.getCell(1).getNumericCellValue();
					}
					String autoName = row.getCell(2).getStringCellValue();
					if (autoCode != 0 && StringUtils.isNotBlank(autoName)) {
						lastAutoCode = autoCode;
					}
					if (row.getCell(4) != null) {
						Integer subCode = (int)row.getCell(4).getNumericCellValue();
						String subName = "";
						if(row.getCell(5).getCellType() == 0){
							subName = String.valueOf(row.getCell(5).getNumericCellValue());
						}else{
							subName = row.getCell(5).getStringCellValue();
						}
						if (StringUtils.isNotBlank(subName)) {
							DataDictionary dataDictionary = new DataDictionary();
							subName = subName.trim();
							subName = subName.substring(subName.indexOf("-") + 1, subName.length());
							dataDictionary.setName(subName);
							dataDictionary.setCode(subCode);
							dataDictionary.setGroup("sublogos");
							dataDictionary.setAutoLogoCode(lastAutoCode);
							dateDictionaryService.save(dataDictionary);
						}
					}
				}
			}
			
			
			//导入年贷款
			Integer lastSubLogo = 0;
			lastAutoCode = 0;
			for (int i = 2; i < rows; i++) {
				Row row = sheet.getRow(i);
				if (row!=null && row.getCell(1) != null) {
					Integer autoCode = 0;
					if(row.getCell(1).getCellType() == 1){
						autoCode = NumberUtils.toInt(row.getCell(1).getStringCellValue());
					}else{
						autoCode = 	(int)row.getCell(1).getNumericCellValue();
					}
					String autoName = row.getCell(2).getStringCellValue();
					if (autoCode != 0 && StringUtils.isNotBlank(autoName)) {
						lastAutoCode = autoCode;
					}
					
					if(row.getCell(4)!=null){
						Integer subLogo = (int)row.getCell(4).getNumericCellValue();
						String subLogoName = row.getCell(5).getStringCellValue();
						if(subLogo!=0 && StringUtils.isNotBlank(subLogoName)){
							lastSubLogo = subLogo;
						}
					}
					
					if(lastSubLogo!=0){
						if(row.getCell(7)!=null){
							Integer niandaiCode = (int)row.getCell(7).getNumericCellValue();
							String niandaiName = "";
							if(row.getCell(8).getCellType() == 0){
								niandaiName = String.valueOf(row.getCell(8).getNumericCellValue());
							}else{
								niandaiName = row.getCell(8).getStringCellValue();
							}
							
							if (StringUtils.isNotBlank(niandaiName)) {
								DataDictionary dataDictionary = new DataDictionary();
								niandaiName = niandaiName.trim();
								niandaiName = niandaiName.substring(niandaiName.lastIndexOf("-") + 1, niandaiName.length());
								dataDictionary.setName(niandaiName);
								dataDictionary.setCode(niandaiCode);
								dataDictionary.setGroup("years");
								dataDictionary.setSubLogoCode(lastSubLogo);
								dataDictionary.setAutoLogoCode(lastAutoCode);
								dateDictionaryService.save(dataDictionary);
							}
						}
						
					}
				}
			}
			
			//=================== V1.7.5车辆品牌数据字典=========================
//			StringBuilder templatePath = new StringBuilder();
//			templatePath.append(request.getSession().getServletContext().getRealPath("template")).append(File.separator).append("brand_code.xls");
//			FileInputStream fileInputStream = new FileInputStream(templatePath.toString());
//			Workbook workbook = WorkbookFactory.create(fileInputStream);
//			Sheet sheet = workbook.getSheetAt(0);
//			Integer rows = sheet.getPhysicalNumberOfRows();
//			for (int i = 2; i < rows; i++) {
//				Row row = sheet.getRow(i);
//				if (row!=null && row.getCell(2) != null) {
//					int cellType = row.getCell(2).getCellType();
//					if(cellType !=3){
//						String autoName = row.getCell(3).getStringCellValue();
//						if (StringUtils.isNotBlank(autoName)&&cellType !=1) {
//							Integer autoCode = (int)row.getCell(2).getNumericCellValue();
//							DataDictionary dataDictionary = new DataDictionary();
//							autoName = autoName.trim();
//							dataDictionary.setName(autoName.trim());
//							dataDictionary.setCode(autoCode);
//							dataDictionary.setGroup("autologos");
//							dataDictionary.setRemark(PingyinTool.getPinYinHeadChar(autoName).substring(0, 1).toUpperCase());
//							dataDictionary.setPinYing(PingyinTool.getPingYin(autoName).toUpperCase());
//							dataDictionary.setJianPin(PingyinTool.getPinYinHeadChar(autoName).toUpperCase());
//							dateDictionaryService.save(dataDictionary);
//						}
//					}
//				}
//			}
//			Integer lastAutoCode = 0;
//			for (int i = 3; i < rows; i++) {
//				Row row = sheet.getRow(i);
//				if (row!=null && row.getCell(2) != null) {
//					int cellType = row.getCell(2).getCellType();
//					if(cellType!=3 && cellType!=1){
//						Integer autoCode = (int)row.getCell(2).getNumericCellValue();
//						String autoName = row.getCell(3).getStringCellValue();
//						if (autoCode != 0 && StringUtils.isNotBlank(autoName)) {
//							lastAutoCode = autoCode;
//						}
//					}
//					if (row!=null && row.getCell(5) != null) {
//						String subName = row.getCell(6).getStringCellValue();
//						if (StringUtils.isNotBlank(subName)) {
//							Integer subCode = (int)row.getCell(5).getNumericCellValue();
//							DataDictionary dataDictionary = new DataDictionary();
//							subName = subName.trim();
//							subName = subName.substring(subName.indexOf("-") + 1, subName.length());
//							dataDictionary.setName(subName);
//							dataDictionary.setCode(subCode);
//							dataDictionary.setGroup("sublogos");
//							dataDictionary.setAutoLogoCode(lastAutoCode);
//							dateDictionaryService.save(dataDictionary);
//						}
//					}
//				}
//			}
		} catch (InvalidFormatException e) {
			log.error("从excel初始化车辆主子品牌异常", e);
		} catch (IOException e) {
			log.error("从excel初始化车辆主子品牌异常", e);
		}
	}
	
	@RequestMapping("/BrandExcelImport/test")
	public String test(HttpServletRequest request) throws Exception {
		StringBuilder templatePath = new StringBuilder();
		templatePath.append(request.getSession().getServletContext().getRealPath("template")).append(File.separator).append("result.xls");
		FileInputStream fileInputStream = new FileInputStream(templatePath.toString());
		Workbook workbook = WorkbookFactory.create(fileInputStream);
		Sheet sheet = workbook.getSheetAt(0);
		Integer rows = sheet.getPhysicalNumberOfRows();
		for (int i = 0; i < rows; i++) {
			Row row = sheet.getRow(i);
			if (row!=null && row.getCell(4) != null) {
				int autoLogo = (int)row.getCell(4).getNumericCellValue();
				String autoBrandName = dateDictionaryService.queryNameByGroupAndCode(ConstParameter.DATA_DICTIONARY_GROUP_AUTO_BRAND, autoLogo);
				if(StringUtils.isBlank(autoBrandName)){
					autoBrandName = "未找到";
				}
				String vehicleSublogoName = "";
				if (row!=null && row.getCell(5) != null) {
					int subLogo = (int)row.getCell(5).getNumericCellValue();
					vehicleSublogoName = dateDictionaryService.querySubLogo(ConstParameter.DATA_DICTIONARY_GROUP_SUB_LOGS, autoLogo, subLogo);
					if(StringUtils.isBlank(vehicleSublogoName)){
						vehicleSublogoName = "未找到";
					}
				}
				row.getCell(4).setCellValue(autoBrandName);
				row.getCell(5).setCellValue(vehicleSublogoName);
			}
		}
		FileOutputStream fos = new FileOutputStream(templatePath.toString());
		workbook.write(fos);
		return null;
	}
}
