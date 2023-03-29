package com.miniproj.etc;

public class PagingInfo {
	
	private int viewPostCntPerPage; // 한 페이지당 보여줄 글의 갯수	
	private int pageNo; // 현재 페이지
	private int tatalPageCnt; // 총 페이지 수
	private int startRowIndex; // n번 페이지에서 보여주기 시작할 row 인덱스 번호
	private int tatalPostCnt; // 전체 글의 갯수 
	
	private int pageCntPerBlock = 10; // 1개의 페이징 블럭에 3개의 페이지를 보여준다 
	private int pageBlockofCurrentPage; // 현재 페이지가 속한 페이징 블럭
	private int startNumofCurrentPagingBlock; // 현재 페이징 블럭의 시작번호
	private int endNumofCurrentPagingBlock; // 현재 페이징 블럭의 끝번호
	
	
	public int getViewPostCntPerPage() {
		return this.viewPostCntPerPage;
	}
	public void setViewPostCntPerPage(int viewPostCntPerPage) {
		this.viewPostCntPerPage = viewPostCntPerPage;
	}
	
	public int getPageNo() {
		return this.pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public int getTatalPageCnt() {
		return this.tatalPageCnt;
	}
	public void setTatalPageCnt(int tatalPostCnt, int viewPostCntPerPage) { 
		//3) 총 페이지의 수 : 게시판 글 수 / 하나의 페이지에 보여줄 글의 수
		// 나누어 떨어지지 않았으면 + 1
		if ((tatalPostCnt % viewPostCntPerPage) == 0) {
			this.tatalPageCnt = tatalPostCnt / viewPostCntPerPage ;
		} else {
			this.tatalPageCnt = tatalPostCnt / viewPostCntPerPage + 1 ;
		}
		
		
	}
	
	
	public int getStartRowIndex() {
		return this.startRowIndex;
	}
	public void setStartRowIndex(int pageNo) {
		// 보여주기 시작할 row(글) index 번호 = 한페이지에 보여줄 글의 갯수 * (현재 페이지 -1)
		this.startRowIndex = this.viewPostCntPerPage * (pageNo -1);
	}
	
	
	public int getTatalPostCnt() {
		return this.tatalPostCnt;
	}
	public void setTatalPostCnt(int tatalPostCnt) {
		this.tatalPostCnt = tatalPostCnt;
	}
	
	

	
	// ---------------------------- 페이징 블럭 ----------------------------------------------------------
	

	
	public int getPageCntPerBlock() {
		return this.pageCntPerBlock;
	}
	public void setPageCntPerBlock(int pageCntPerBlock) {
		// 1개의 블럭에 몇개 페이지를 둘 것인가
		this.pageCntPerBlock = pageCntPerBlock;
	}
	
	
	public int getPageBlockofCurrentPage() {
		// 현재 페이지가 속한 페이지 블럭
		return pageBlockofCurrentPage;
	}
	public void setPageBlockofCurrentPage(int pageNo) {
		// 현재 페이지 블럭 : 현재 페이지 번호 / 총 페이징 블럭(pageCntPreBlock)
		if (pageNo % pageCntPerBlock == 0) {
			this.pageBlockofCurrentPage = pageNo / this.pageCntPerBlock;
		} else {
			this.pageBlockofCurrentPage = (int)(Math.ceil((double)pageNo / this.pageCntPerBlock));
		}
	}
	
	
	public int getStartNumofCurrentPagingBlock() {
		return startNumofCurrentPagingBlock;
	}
	public void setStartNumofCurrentPagingBlock(int pageBlockofCurrentPage) {
		// 현재 페이징 블럭 시작 번호 : ((현재 페이징 블럭 - 1) * 페이징 블럭(pageCntPreBlock)) + 1
		this.startNumofCurrentPagingBlock = ((pageBlockofCurrentPage - 1) * this.pageCntPerBlock) + 1 ; 
	}
	
	
	public int getEndNumofCurrentPagingBlock() {
		return endNumofCurrentPagingBlock;
	}
	
	public void setEndNumofCurrentPagingBlock(int startNumofCurrentPagingBlock) {
		// 현재 페이징 블럭의 끝 번호 : 현재 페이징 블럭의 시작 번호 + 페이징 블럭 - 1
		endNumofCurrentPagingBlock = (startNumofCurrentPagingBlock + this.pageCntPerBlock) - 1;
		
		if (this.endNumofCurrentPagingBlock > this.tatalPageCnt) {
			this.endNumofCurrentPagingBlock = this.tatalPageCnt;
		}
	}
	

	
	@Override
	public String toString() {
		return "PagingInfo [viewPostCntPerPage=" + viewPostCntPerPage + ", pageNo=" + pageNo + ", tatalPageCnt="
				+ tatalPageCnt + ", startRowIndex=" + startRowIndex + ", tatalPostCnt=" + tatalPostCnt
				+ ", pageCntPerBlock=" + pageCntPerBlock + ", PageBlockofCurrentPage=" + pageBlockofCurrentPage
				+ ", startNumofCurrentPagingBlock=" + startNumofCurrentPagingBlock + ", endNumofCurrentPagingBlock="
				+ endNumofCurrentPagingBlock + "]";
	}
	
	
	
}
