package com.miniproj.etc;

import java.io.File;
import java.util.UUID;

import org.apache.commons.fileupload.FileItem;

public class FileNameProcessing {
	// 업로드 된 파일의 이름을 중복되지 않게 반환
	public static String getNewFileName(FileItem fi, String realPath) {
		long tmpFileSize = fi.getSize(); // 파일 사이즈
		String tmpFileName = fi.getName(); // 유저가 업로드한 파일의 이름 (확장자 포함)
		String newFileName = ""; // 실제 저장되는 파일명
		
		if (tmpFileSize > 0) { 
			int cnt = 0; // 중복된 파일의 갯수
			// 중복된 파일이름이 몇개까지 있을지 모르니 while문 사용
			while (DuplicateFileName(tmpFileName, realPath)) { // 파일이 중복되면 실행됨
				cnt++;
				tmpFileName = makeNewFileNameWithNumbering(tmpFileName, cnt);
			}
			
			newFileName = tmpFileName;
			
			System.out.println(newFileName);
		}
		
		return newFileName;
		
	}

	// tmpFileNamed의 파일이 realPath에 존재한다면 true, 아니면 false 반환
	private static boolean DuplicateFileName(String tmpFileName, String realPath) {
		boolean result = false;
	
		File tmpFileNamePath = new File(realPath);
		File[] files = tmpFileNamePath.listFiles();
		for (File f : files) {
			if (f.getName().equals(tmpFileName)) { // 파일명이 중복된다
				result = true;
			}
		}
	
		return result;
	}

	
	private static String makeNewFileNameWithNumbering(String tmpFileName, int cnt) {
		// "파일명(번호).확장자"
		String newFileName = "";
		String ext = tmpFileName.substring(tmpFileName.lastIndexOf(".") + 1); // 확장자 잘라내기
		String oldFileNameWithoutExt = tmpFileName.substring(0, tmpFileName.lastIndexOf(".")); // 확장자 없는 파일 이름
		
		int openPos = oldFileNameWithoutExt.indexOf("(");
		if (openPos == -1) { // 괄호가 없다면 => 처음 중복 됨.
			newFileName = oldFileNameWithoutExt + "(" + cnt + ")." + ext;
		} else { // 괄호가 있다. => 이자하(n).jpg 이 있음
			newFileName = oldFileNameWithoutExt.substring(0, openPos) + "(" + cnt + ")." + ext;
		}
		
		
		return newFileName;
	}
	


}



