package com.lq.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import com.github.junrar.extract.ExtractArchive;

public abstract class CompressUtil {
	public void unCompressAllFile(String srcRarPath, String dstDirectoryPath) {
		if (srcRarPath.toLowerCase().endsWith(".rar")) {
			unRarAllFile(srcRarPath, dstDirectoryPath);
		}
		if (srcRarPath.toLowerCase().endsWith(".zip")) {
			unZipAll(srcRarPath, dstDirectoryPath);
		}
	}

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
	 * 把zip文件解压到指定的文件夹
	 * 
	 * @param zipFilePath
	 *            zip文件路径, 如 "D:/test/aa.zip"
	 * @param saveFileDir
	 *            解压后的文件存放路径, 如"D:/test/" ()
	 */
	public static void unzip(File zipFilePath, File saveFileDir) {

		InputStream is = null;
		// can read Zip archives
		ZipArchiveInputStream zais = null;
		try {
			is = new FileInputStream(zipFilePath);

			byte[] b = new byte[3];
			is.read(b);
			is.close();
			String encoding;
			if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
				System.out.println(zipFilePath.getName() + "：编码为UTF-8");
				encoding = "UTF-8";
			} else {
				encoding = "GBK";
				// System.out.println(zipFilePath.getName() +
				// "：可能是GBK，也可能是其他编码");
			}

			is = new FileInputStream(zipFilePath);
			zais = new ZipArchiveInputStream(is, encoding);
			ArchiveEntry archiveEntry = null;
			// 把zip包中的每个文件读取出来
			// 然后把文件写到指定的文件夹
			while ((archiveEntry = zais.getNextEntry()) != null) {
				// 获取文件名
				String entryFileName = archiveEntry.getName();
				// 构造解压出来的文件存放路径
				String entryFilePath = saveFileDir + File.separator + entryFileName;
				byte[] content = new byte[(int) archiveEntry.getSize()];
				OutputStream os = null;
				try {
					// 把解压出来的文件写到指定路径
					File entryFile = new File(entryFilePath);
					if (archiveEntry.isDirectory()) { // 文件夹
						if (!entryFile.exists()) {
							entryFile.mkdirs();
						}
					} else {
						os = new BufferedOutputStream(new FileOutputStream(entryFile));
						int len = -1;
						while ((len = zais.read(content)) != -1) {
							os.write(content, 0, len);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (os != null) {
						os.flush();
						os.close();
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (zais != null) {
					zais.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 解压所有ZIP文件到指定文件夹下（包含所有压缩包里的ZIP）
	 * 
	 * @param srcZipPath
	 * @param dstDirectoryPath
	 */
	public void unZipAll(String srcZipPath, String dstDirectoryPath) {
		File zip = new File(srcZipPath);
		if (zip.isFile() && !srcZipPath.toLowerCase().endsWith(".zip")) {
			return;
		}

		if (dstDirectoryPath.toLowerCase().endsWith(".zip")) {
			dstDirectoryPath = dstDirectoryPath.substring(0, dstDirectoryPath.length() - 4);
		}

		File destinationFolder = new File(dstDirectoryPath);
		if (!destinationFolder.exists()) {
			destinationFolder.mkdirs();
		}
		if (srcZipPath.toLowerCase().endsWith(".zip")) {
			unzip(zip, destinationFolder);
		}

		File[] files = destinationFolder.listFiles();
		String fileName = "";
		for (int i = 0; i < files.length; i++) {
			fileName = files[i].getPath();
			getFiles(files[i], files.length, i);
			unZipAll(fileName, fileName);
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
