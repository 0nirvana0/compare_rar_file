package com.lq.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.extract.ExtractArchive;
import com.github.junrar.io.ReadOnlyAccessFile;
import com.github.junrar.rarfile.FileHeader;
import com.lq.util.FileUtil;
import com.lq.util.FormatDuring;

public class Test {
	public static void main(String[] args) {
		long lStart = System.currentTimeMillis();
		String srcRarPath = "data/come.rar";
		String dstDirectoryPath = "data/come";
		FileUtil.deleteAllFilesOfDir(dstDirectoryPath);
		// testExtractArchive(srcRarPath, dstDirectoryPath);
		File outPath;
		try {
			outPath = File.createTempFile("COMPARE_FF", "00");
			System.out.println(outPath);
		} catch (IOException e) {

			e.printStackTrace();
		}

		System.out.println(FormatDuring.formatDuring(lStart, System.currentTimeMillis()));
	}

	public static void testRarUtil(String srcRarPath) {

		Archive archive = null;
		try {
			archive = new Archive(new File(srcRarPath));
			// archive.getMainHeader().print(); // 打印文件信息.
			System.out.println("Encrypted:" + archive.isEncrypted());

			List<FileHeader> files = archive.getFileHeaders();
			System.out.println("files size:" + files.size());
			FileHeader fh = null;

			for (int i = 0; i < files.size(); i++) {
				System.out.println(i + 1 + "----------------------------------");
				fh = files.get(i);

				// if (fh.isFileHeader() && fh.isUnicode()) {
				// System.out.println("unicode name: " + fh.getFileNameW());
				// } else {
				// System.out.println("FileName:" + fh.getFileNameString());
				// }
				// 防止文件名中文乱码问题的处理
				String fileName = fh.getFileNameW().isEmpty() ? fh.getFileNameString() : fh.getFileNameW();
				System.out.println("FileName:" + fileName);
				System.out.println("DataSize:" + fh.getDataSize());
				System.out.println("isEncrypted:" + fh.isEncrypted());
				System.out.println("isFileHeader:" + fh.isFileHeader());
				System.out.println("isUnicode:" + fh.isUnicode());
				System.out.println("packSize:" + fh.getFullUnpackSize() + " " + fh.getFullUnpackSize());
				System.out.println("isDirectory:" + fh.isDirectory());

			}

		} catch (RarException | IOException e) {

			e.printStackTrace();
		} finally {
			try {
				archive.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	public static void testExtractArchive(String srcRarPath, String dstDirectoryPath) {
		ExtractArchive extractArchive = new ExtractArchive();

		File rar = new File(srcRarPath);
		if (rar.isFile() && !srcRarPath.toLowerCase().endsWith(".rar")) {
			// System.out.println("非rar文件！");
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
			// System.out.println(fileName);
			testExtractArchive(fileName, fileName);
		}

	}

	public static void testRead(String srcRarPath) {
		File rar = new File(srcRarPath);
		ReadOnlyAccessFile readFile = null;
		try {
			readFile = new ReadOnlyAccessFile(rar);
			System.out.println(readFile.readLine());
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

		}

	}

}
