package com.lq.util;

import java.io.File;

import com.github.junrar.extract.ExtractArchive;

public abstract class RarUtil {

	/**
	 * 解压所有RAR文件到指定文件夹下（包含所有压缩包里的RAR）
	 * 
	 * @param srcRarPath
	 * @param dstDirectoryPath
	 */
	public void unRarAllFile(String srcRarPath, String dstDirectoryPath) {
		ExtractArchive extractArchive = new ExtractArchive();

		File rar = new File(srcRarPath);
		if (rar.isFile() && !srcRarPath.toLowerCase().endsWith(".rar")) {
			return;
		}

		if (dstDirectoryPath.toLowerCase().endsWith(".rar")) {
			dstDirectoryPath = dstDirectoryPath.substring(0, dstDirectoryPath.length() - 4);
		}
		File destinationFolder = new File(dstDirectoryPath);
		if (!destinationFolder.exists()) {// 目标目录不存在时，创建该文件夹
			destinationFolder.mkdirs();
		}
		if (srcRarPath.toLowerCase().endsWith(".rar")) {
			extractArchive.extractArchive(rar, destinationFolder);
		}

		File[] files = destinationFolder.listFiles();
		String fileName = "";

		for (int i = 0; i < files.length; i++) {
			fileName = files[i].getPath();
			getFiles(files[i], files.length, i);
			unRarAllFile(fileName, fileName);
		}
	}

	/**
	 * 获取解压的所有文件
	 * 
	 * @param file
	 * @param fileLength
	 * @param i
	 */
	protected abstract void getFiles(File file, int folderSize, int i);
}
