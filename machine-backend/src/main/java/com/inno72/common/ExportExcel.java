package com.inno72.common;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 用于将数据导入到excel表格中
 * 
 * @author zhangwenjie
 *
 * @param <T>
 */
public class ExportExcel<T> {

	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 * @SuppressWarnings("unchecked") 屏蔽某些编译时的警告信息
	 */
	// 每次设置导出数量
	public static int NUM = CommonConstants.ONE_THOUSAND;
	public static String title = DateUtil.nowStr();

	/**
	 * 导出Excel的方法
	 * 
	 * @param title
	 *            excel中的sheet名称
	 * @param headers
	 *            表头
	 * @param result
	 *            结果集
	 * @param out
	 *            输出流
	 * @param pattern
	 *            时间格式
	 * @throws Exception
	 */
	public void exportExcel(String[] headers, String[] columns, List<T> result,
			OutputStream out, String fileName) throws Exception {

		File zip = new File(fileName + ".zip");// 压缩文件
		int n = 0;
		if (null != result && result.size() > 0) {
			if (result.size() % NUM == 0) {
				n = result.size() / NUM;
			} else {
				n = result.size() / NUM + 1;
			}
		} else {
			n = 1;
		}
		List<String> fileNames = new ArrayList();// 用于存放生成的文件名称s
		// 文件流用于转存文件
		for (int j = 0; j < n; j++) {
			List<T> result1 = null;
			// 切取每1000为一个导出单位，存储一个文件
			// 对不足1000做处理；
			if (null != result && result.size() > 0) {
				if (j == n - 1) {
					if (result.size() % NUM == 0) {
						result1 = result.subList(NUM * j, NUM * (j + 1));
					} else {
						result1 = result.subList(NUM * j,
								NUM * j + result.size() % NUM);
					}
				} else {
					result1 = result.subList(NUM * j, NUM * (j + 1));
				}
			}
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName + j);
			// 设置表格默认列宽度为18个字节
			sheet.setDefaultColumnWidth((short) 18);
			String file = fileName + "-" + j + ".xls";
			fileNames.add(file);
			FileOutputStream o = new FileOutputStream(file);
			// 生成一个样式
			HSSFCellStyle style = workbook.createCellStyle();
			// 设置这些样式
			style.setFillForegroundColor(HSSFColor.GOLD.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 生成一个字体
			HSSFFont font = workbook.createFont();
			font.setColor(HSSFColor.VIOLET.index);
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			// 把字体应用到当前的样式
			style.setFont(font);

			// 指定当单元格内容显示不下时自动换行
			style.setWrapText(true);

			// 声明一个画图的顶级管理器
			// HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

			// 产生表格标题行
			// 表头的样式
			HSSFCellStyle titleStyle = workbook.createCellStyle();// 创建样式对象
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
			titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			// 设置字体
			HSSFFont titleFont = workbook.createFont(); // 创建字体对象
			titleFont.setFontHeightInPoints((short) 17); // 设置字体大小
			titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
			// titleFont.setFontName("黑体"); // 设置为黑体字
			titleStyle.setFont(titleFont);
			// sheet.addMergedRegion(new
			// Region(0,(short)0,0,(short)(headers.length-1)));//指定合并区域
			// HSSFRow rowHeader = sheet.createRow(0);
			// HSSFCell cellHeader = rowHeader.createCell(0);
			// //只能往第一格子写数据，然后应用样式，就可以水平垂直居中
			// HSSFRichTextString textHeader = new
			// HSSFRichTextString(fileName+j);
			// cellHeader.setCellStyle(titleStyle);
			// cellHeader.setCellValue(textHeader);

			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
				HSSFRichTextString text = new HSSFRichTextString(headers[i]);
				cell.setCellValue(text);
			}
			// 遍历集合数据，产生数据行
			if (result1 != null) {
				int index = 1;
				for (T t : result1) {
					row = sheet.createRow(index);
					index++;
					for (int i = 0; i < columns.length; i++) {
						HSSFCell cell = row.createCell(i);
						String fieldName = columns[i];
						String getMethodName = "get"
								+ fieldName.substring(0, 1).toUpperCase()
								+ fieldName.substring(1);
						Class tCls = t.getClass();
						Method getMethod = tCls.getMethod(getMethodName,
								new Class[] {});
						Object value = getMethod.invoke(t, new Class[] {});
						String textValue = null;
						
						if (value == null) {
							textValue = "";
						} else {
							// 其它数据类型都当作字符串简单处理
							textValue = value.toString();
						}

						//针对个别属性做相应处理
						if(fieldName.equals("investType")){
							if(value != null && value.equals(2)){
								textValue = "复投";
							}else if(value != null && value.equals(3)){
								textValue = "首投";
							}else if(value != null && value.equals(9)){
								textValue = "使用体验金";
							}else{
								textValue = "-";
							}
						}
						
						if (textValue != null) {
							Pattern p = Pattern.compile("^//d+(//.//d+)?$");
							Matcher matcher = p.matcher(textValue);
							if (matcher.matches()) {
								// 是数字当作double处理
								cell.setCellValue(Double.parseDouble(textValue));
							} else {
								HSSFRichTextString richString = new HSSFRichTextString(
										textValue);
								cell.setCellValue(richString);
							}
						}
					}
				}
			}
			workbook.write(o);
		}
		File srcfile[] = new File[fileNames.size()];
		for (int i = 0, n1 = fileNames.size(); i < n1; i++) {
			srcfile[i] = new File(fileNames.get(i));
		}
		ZipFiles(srcfile, zip);
		FileInputStream inStream = new FileInputStream(zip);
		byte[] buf = new byte[4096];
		int readLength;
		while (((readLength = inStream.read(buf)) != -1)) {
			out.write(buf, 0, readLength);
		}
		inStream.close();
	}

	// 获取文件名字
	// public static String getFileName(){
	// // 文件名获取
	// Date date = new Date();
	// SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	// String f = title + format.format(date);
	// return f;
	// }
	// 压缩文件
	public static void ZipFiles(File[] srcfile, File zipfile) {
		byte[] buf = new byte[1024];
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipfile));
			for (int i = 0; i < srcfile.length; i++) {
				FileInputStream in = new FileInputStream(srcfile[i]);
				out.putNextEntry(new ZipEntry(srcfile[i].getName()));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 设置响应头 */
	public void setResponseHeader(String[] headers, String[] columns,
			List<T> result, HttpServletResponse response, String fileName) {
		try {
			String titles = title + fileName;
			response.reset();// 清空输出流
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(titles.getBytes("GB2312"), "8859_1") + ".zip");
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
			exportExcel(headers, columns, result, response.getOutputStream(),
					fileName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}