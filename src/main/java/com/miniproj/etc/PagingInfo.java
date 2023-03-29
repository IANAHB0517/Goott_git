package com.miniproj.etc;

public class PagingInfo {
	private int viewPostCntPerPage;  // 한 페이지당 보여줄 글의 갯수	
	private int pageNo;   // 현재 페이지
	private int totalPostCnt;  // 전체 글의 갯수
	private int totalPageCnt;   // 총 페이지 수
	private int startRowInd;    // x번 페이지에서 보여주기 시작할 row 인덱스 번호
	
	private int pageCntPerBlock = 10;  // 1개의 블럭에 3개의 페이지를 보여준다
	private int pageBlockOfCurrentPage;  // 현재 페이지가 속한 페이징 블럭
	private int startNumOfCurrentPagingBlock;  // 현재 페이징블럭의 시작번호
	private int endNumOfCurrentPagingBlock;   // 현재 페이징블럭의 끝번호
	
	public int getViewPostCntPerPage() {
		return this.viewPostCntPerPage;
	}
	
	public void setViewPostCntPerPage(int viewPostCntPerPage) { // 한 페이지당 보여줄 글의 갯수
		this.viewPostCntPerPage = viewPostCntPerPage;
	}
	
	
	public int getPageNo() {
		return this.pageNo;
	}
	
	public void setPageNo(int pageNo) {  //  현재 페이지
		this.pageNo = pageNo;
	}
	
	public int getTotalPageCnt() {
		return this.totalPageCnt;
	}
	
	public void setTotalPageCnt(int totalPostCnt, int viewPostCntPerPage) { // 총 페이지 수
		// 총 페이지의 수 : 게시판글 수 / 하나의 페이지에 보여줄 글의 수
		//  나누어 떨어지지 않으면 +1
		if((totalPostCnt % viewPostCntPerPage) == 0) {
			this.totalPageCnt = totalPostCnt / viewPostCntPerPage;
		} else {
			this.totalPageCnt = (totalPostCnt / viewPostCntPerPage) + 1;
		}
	}
	
	public int getStartRowInd() {
		return this.startRowInd;
	}
	
	public void setStartRowInd(int pageNo) {
		// 보여주기시작할row index번호 = 한페이지에 보여줄 글의 갯수 * (현재페이지 -1)
		this.startRowInd = this.viewPostCntPerPage * (pageNo - 1);
	}

	public int getTotalPostCnt() {
		return this.totalPostCnt;
	}

	public void setTotalPageCnt(int totalPageCnt) {
		this.totalPageCnt = totalPageCnt;
	}
	
	public void setTotalPostCnt(int totalPostCnt) {
		this.totalPostCnt = totalPostCnt;
	}

	
	//------------------------------- 페이징 블럭 --------------------------------------
		
	
	public int getPageCntPerBlock() {
		// 1개의블럭에 몇개 페이지를 둘것인가
		return this.pageCntPerBlock;
	}

	public void setPageCntPerBlock(int pageCntPerBlock) {
		this.pageCntPerBlock = pageCntPerBlock;
	}

	public int getPageBlockOfCurrentPage() {
		// 현재 페이지가 속한 페이징 블럭
		return pageBlockOfCurrentPage;
	}

	public void setPageBlockOfCurrentPage(int pageNo) {
		// 현재 페이지가 속한 페이징 블럭 : 현재페이지번호 / pageCntPerBlock
		// 나누어 떨어지지 않으면 올림
		if (pageNo % this.pageCntPerBlock == 0) {
			this.pageBlockOfCurrentPage = pageNo / this.pageCntPerBlock; 
		} else {
			this.pageBlockOfCurrentPage = (int)(Math.ceil((double)pageNo / this.pageCntPerBlock));
		}
	}

	public int getStartNumOfCurrentPagingBlock() {
		return startNumOfCurrentPagingBlock;
	}

	public void setStartNumOfCurrentPagingBlock(int pageBlockOfCurrentPage) {
		// 현재 페이징 블럭 시작번호 : ((현재 페이징블럭-1) * pageCntPerBlock ) + 1
		this.startNumOfCurrentPagingBlock = ((pageBlockOfCurrentPage - 1) * this.pageCntPerBlock) + 1;
	}

	public int getEndNumOfCurrentPagingBlock() {
		return endNumOfCurrentPagingBlock;
	}

	public void setEndNumOfCurrentPagingBlock(int startNumOfCurrentPagingBlock) {
		// 현재 페이징 블럭 끝번호 : (현재 페이징블럭 시작번호 + pageCntPerBlock) - 1
		this.endNumOfCurrentPagingBlock = (startNumOfCurrentPagingBlock + this.pageCntPerBlock) - 1;
		
		if (this.endNumOfCurrentPagingBlock > this.totalPageCnt) {
			this.endNumOfCurrentPagingBlock = this.totalPageCnt;
		}
	}

	@Override
	public String toString() {
		return "PagingInfo [viewPostCntPerPage=" + viewPostCntPerPage + ", pageNo=" + pageNo + ", totalPostCnt="
				+ totalPostCnt + ", totalPageCnt=" + totalPageCnt + ", startRowInd=" + startRowInd
				+ ", pageCntPerBlock=" + pageCntPerBlock + ", pageBlockOfCurrentPage=" + pageBlockOfCurrentPage
				+ ", startNumOfCurrentPagingBlock=" + startNumOfCurrentPagingBlock + ", endNumOfCurrentPagingBlock="
				+ endNumOfCurrentPagingBlock + "]";
	}

	
	

	
	
	
	
}
