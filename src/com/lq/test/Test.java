package com.lq.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

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
		// String srcRarPath = "data/come.rar";
		// String dstDirectoryPath = "data/come";

		// testExtractArchive(srcRarPath, dstDirectoryPath);
		String unzipfile = "data/zip/commons-compress-1.15.zip";
		String unzipdir = "data/zip/unpack";
		FileUtil.deleteAllFilesOfDir(unzipdir);
		decompressZip(unzipfile, unzipdir);
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

	/**
	 * 把文件压缩成zip格式
	 * 
	 * @param files
	 *            需要压缩的文件
	 * @param zipFilePath
	 *            压缩后的zip文件路径 ,如"D:/test/aa.zip";
	 */
	public static void compressFiles2Zip(File[] files, String zipFilePath) {
		if (files != null && files.length > 0) {
			if (isEndsWithZip(zipFilePath)) {
				ZipArchiveOutputStream zaos = null;
				try {
					File zipFile = new File(zipFilePath);
					zaos = new ZipArchiveOutputStream(zipFile);
					// Use Zip64 extensions for all entries where they are
					// required
					zaos.setUseZip64(Zip64Mode.AsNeeded);

					// 将每个文件用ZipArchiveEntry封装
					// 再用ZipArchiveOutputStream写到压缩文件中
					for (File file : files) {
						if (file != null) {
							ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
							zaos.putArchiveEntry(zipArchiveEntry);
							InputStream is = null;
							try {
								is = new BufferedInputStream(new FileInputStream(file));
								byte[] buffer = new byte[1024 * 5];
								int len = -1;
								while ((len = is.read(buffer)) != -1) {
									// 把缓冲区的字节写入到ZipArchiveEntry
									zaos.write(buffer, 0, len);
								}
								// Writes all necessary data for this entry.
								zaos.closeArchiveEntry();
							} catch (Exception e) {
								throw new RuntimeException(e);
							} finally {
								if (is != null)
									is.close();
							}

						}
					}
					zaos.finish();
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					try {
						if (zaos != null) {
							zaos.close();
						}
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}

			}

		}

	}

	/**
	 * 把zip文件解压到指定的文件夹
	 * 
	 * @param zipFilePath
	 *            zip文件路径, 如 "D:/test/aa.zip"
	 * @param saveFileDir
	 *            解压后的文件存放路径, 如"D:/test/"
	 */
	public static void decompressZip(String zipFilePath, String saveFileDir) {
		if (isEndsWithZip(zipFilePath)) {
			File file = new File(zipFilePath);
			if (file.exists()) {
				InputStream is = null;
				// can read Zip archives
				ZipArchiveInputStream zais = null;
				try {
					is = new FileInputStream(file);
					zais = new ZipArchiveInputStream(is);
					ArchiveEntry archiveEntry = null;
					// 把zip包中的每个文件读取出来
					// 然后把文件写到指定的文件夹
					while ((archiveEntry = zais.getNextEntry()) != null) {
						// 获取文件名
						String entryFileName = archiveEntry.getName();
						// 构造解压出来的文件存放路径
						String entryFilePath = saveFileDir + entryFileName;
						byte[] content = new byte[(int) archiveEntry.getSize()];
						zais.read(content);
						OutputStream os = null;
						try {
							// 把解压出来的文件写到指定路径
							File entryFile = new File(entryFilePath);
							os = new BufferedOutputStream(new FileOutputStream(entryFile));
							os.write(content);
						} catch (IOException e) {
							throw new IOException(e);
						} finally {
							if (os != null) {
								os.flush();
								os.close();
							}
						}

					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					try {
						if (zais != null) {
							zais.close();
						}
						if (is != null) {
							is.close();
						}
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	/**
	 * 判断文件名是否以.zip为后缀
	 * 
	 * @param fileName
	 *            需要判断的文件名
	 * @return 是zip文件返回true,否则返回false
	 */
	public static boolean isEndsWithZip(String fileName) {
		boolean flag = false;
		if (fileName != null && !"".equals(fileName.trim())) {
			if (fileName.endsWith(".ZIP") || fileName.endsWith(".zip")) {
				flag = true;
			}
		}
		return flag;
	}
}
