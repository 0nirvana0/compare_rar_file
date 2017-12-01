package com.lq.main;

import java.io.File;
import java.util.List;

import com.lq.util.ExcelDataGetter;
import com.lq.util.ExcelDataWriter;
import com.lq.util.FormatDuring;

public class Main {
	private ExcelDataGetter getter = new ExcelDataGetter();
	private ExcelDataWriter writer = new ExcelDataWriter();

	// 筛选没close
	public void dealNoClose(String filePath, String outPath) {
		List<Object[]> allRecord = getter.getData(filePath, 1);
		// List<Object[]> notClose = filter.getNotClose(allRecord);
		// writer.writeOut(notClose, outPath + "/data1.xlsx");
	}

	public boolean createFolder(String path) {
		File file = new File(path);
		boolean flag = false;
		if (!file.exists()) {
			flag = file.mkdirs();
		} else {
			flag = true;
		}
		return flag;
	}

	public static void main(String[] args) {
		Main main = new Main();
		long lStart = System.currentTimeMillis();
		String path = "data/demo.xlsx";
		String outPath = "data/out/";
		//main.dealAll(path, outPath);
		System.out.println(FormatDuring.formatDuring(lStart, System.currentTimeMillis()));
	}
}
