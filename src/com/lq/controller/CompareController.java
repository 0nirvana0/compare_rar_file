package com.lq.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lq.util.ExcelDataWriter;

public class CompareController {
	private ExcelDataWriter writer = new ExcelDataWriter();

	public List<File> compare(File compareFile, List<File> files) {
		List<File> outFiles = new ArrayList<File>();
		String compareFileName = compareFile.getName();
		String fileName = "";
		for (File file : files) {
			fileName = file.getName();
			if (fileName.equalsIgnoreCase(compareFileName)) {
				outFiles.add(file);
			}

		}
		return outFiles;
	}

	public void writeExcel(List<Object[]> comparedData, String path) {
		writer.writeOut(comparedData, path);
	}
}
