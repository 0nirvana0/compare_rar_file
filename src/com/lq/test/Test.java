package com.lq.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.lq.util.FormatDuring;

public class Test {
	public static void main(String[] args) {
		long lStart = System.currentTimeMillis();
		testRarUtil();
		System.out.println(FormatDuring.formatDuring(lStart, System.currentTimeMillis()));
	}

	static String srcRarPath = "data/come.rar";

	public static void testRarUtil() {

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

				if (fh.isFileHeader() && fh.isUnicode()) {
					System.out.println("unicode name: " + fh.getFileNameW());
				} else {
					System.out.println("FileName:" + fh.getFileNameString());
				}
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
}
