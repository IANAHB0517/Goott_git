package com.miniproj.vodto;

import java.sql.Timestamp;

public class BoardVo {
private int no;
private String writer;
private String title;
private Timestamp postDate;
private String content;
private String imgFile;
private int readCount;
private int likeCount;
private int ref;
private int step;
private int refOrder;

public BoardVo(int no, String writer, String title, Timestamp postDate,String content ,String imgFile, int readCount,int likeCount, int ref,
		int step, int refOrder) {
	super();
	this.no = no;
	this.writer = writer;
	this.title = title;
	this.postDate = postDate;
	this.content = content;
	this.imgFile = imgFile;
	this.readCount = readCount;
	this.likeCount = likeCount;
	this.ref = ref;
	this.step = step;
	this.refOrder = refOrder;
}


public int getLikeCount() {
	return likeCount;
}


public void setLikeCount(int likeCount) {
	this.likeCount = likeCount;
}


public String getContent() {
	return content;
}


public void setContent(String content) {
	this.content = content;
}


public int getNo() {
	return no;
}

public void setNo(int no) {
	this.no = no;
}

public String getWriter() {
	return writer;
}

public void setWriter(String writer) {
	this.writer = writer;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public Timestamp getPostDate() {
	return postDate;
}

public void setPostDate(Timestamp postDate) {
	this.postDate = postDate;
}

public String getImgFile() {
	return imgFile;
}

public void setImgFile(String imgFile) {
	this.imgFile = imgFile;
}

public int getReadCount() {
	return readCount;
}

public void setReadCount(int readCount) {
	this.readCount = readCount;
}

public int getRef() {
	return ref;
}

public void setRef(int ref) {
	this.ref = ref;
}

public int getStep() {
	return step;
}

public void setStep(int step) {
	this.step = step;
}

public int getRefOrder() {
	return refOrder;
}

public void setRefOrder(int refOrder) {
	this.refOrder = refOrder;
}


@Override
public String toString() {
	return "BoardVo [no=" + no + ", writer=" + writer + ", title=" + title + ", postDate=" + postDate + ", content="
			+ content + ", imgFile=" + imgFile + ", readCount=" + readCount + ", likeCount=" + likeCount + ", ref="
			+ ref + ", step=" + step + ", refOrder=" + refOrder + "]";
}





}
