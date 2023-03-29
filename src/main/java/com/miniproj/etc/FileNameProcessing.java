package com.miniproj.etc;

import java.io.File;

import org.apache.commons.fileupload.FileItem;

public class FileNameProcessing {
	// 업로드 된 파일의 이름을 중복되지 않는 이름을 반환
	public static String getNewFileName(FileItem fi, String realPath) {
		long tmpFileSize = fi.getSize(); // 파일 사이즈
		String tmpFileName = fi.getName(); // 유저가 업로드한 파일의 이름(확장자 포함)
		String newFileName = ""; // 실제 저장되는 파일명
		if (tmpFileSize > 0) {
			int cnt = 0;
			while (duplicateFileName(tmpFileName, realPath)) { // 파일이 중복되면
				cnt++;
				tmpFileName = makeNewFileNameWithNumbering(tmpFileName, cnt);
			}

			newFileName = tmpFileName;

			System.out.println(newFileName);

		}

		return newFileName;

	}

	// tmpFileName의 파일이 realPath 존재한다면 true, 아니면 false반환
	private static boolean duplicateFileName(String tmpFileName, String realPath) {
		boolean result = false;

		File tmpFileNamePath = new File(realPath);
		File[] files = tmpFileNamePath.listFiles();
		for (File f : files) {
			if (f.getName().equals(tmpFileName)) { // 파일명이 중복 된다.
				result = true;
			}
		}

		return result;

	}
	
	
	private static String makeNewFileNameWithNumbering(String tmpFileName, int cnt) {
		// ex) "파일명(번호).확장자"
		String newFileName = "";
		String ext = tmpFileName.substring(tmpFileName.lastIndexOf(".") + 1);
		String oldFileNameWithoutExt = tmpFileName.substring(0, tmpFileName.lastIndexOf("."));

		int openPos = oldFileNameWithoutExt.indexOf("(");
		if (openPos == -1) { // ( 가 없다면 -> 처음 중복
			newFileName = oldFileNameWithoutExt + "(" + cnt + ")." + ext;
		} else { // 김태희(1).jpg 가 있다.
			newFileName = oldFileNameWithoutExt.substring(0, openPos) + "(" + cnt + ")." + ext;
		}

		return newFileName;
	}
}
