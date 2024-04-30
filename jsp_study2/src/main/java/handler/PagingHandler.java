package handler;

import domain.PagingVO;

public class PagingHandler {
	 // list 하단에 나오는 페이지네이션 핸들링 클래스
	private int startPage; //현재 화면에서 보여줄 시작페이지네이션 번호
	private int endPage; //현재화면에서 보여줄 끝 페이지네이션 번호 10 20 30...
	private int realEndPage; //실제 전체 리스트의 마지막 페이지번호
	private boolean prev; // 이전페이지의 존재여부
	private boolean next; // 다음페이지의 존재여부
	
	private PagingVO pgvo; // 파라미터로 현재 사용자가 클릭한 값을 받아오기
	private int totalCount; //DB에서 검색해오기 - 컨트롤러에서 받기
	
	public PagingHandler(PagingVO pgvo, int totalCount) {
		this.pgvo = pgvo;
		this.totalCount = totalCount;
		
		//1~10/11~20/21~30
		//pageNo / 10 => 0.1(올림)=>1 *10 ->10
		// 11 => 20/10 => 1.1(올림)=>2*10 =>20
		// 정수 / 정수 = 정수 0.1 형변환 필요
		
		
		this.endPage = (int)Math.ceil(pgvo.getPageNo()/(double)10)*10;
		
		this.startPage = this.endPage -9;
		//전체게시글수 / 한 화면에 표시되는 게시글수
		// 103 103/10 10.3 =>11 (올림)
		// 나머지 게시글이 하나라도 있다면 1페이지가 더 생겨야 함
		
		this.realEndPage = (int)Math.ceil(totalCount / (double)pgvo.getQty());
		
		//1~10 / 11~20
		// 실제 리얼 마지막 페이지가 11페이지라면 11끝
		// 진짜 끝페이지가 엔드페이지와 같지않을경우
		if(this.realEndPage < this.endPage) {
			this.endPage = this.realEndPage;
		}
		
		// 이전 다음 유무
		this.prev = this.startPage > 1;
		this.next = this.endPage < this.realEndPage;
		
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getRealEndPage() {
		return realEndPage;
	}

	public void setRealEndPage(int realEndPage) {
		this.realEndPage = realEndPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public PagingVO getPgvo() {
		return pgvo;
	}

	public void setPgvo(PagingVO pgvo) {
		this.pgvo = pgvo;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString() {
		return "PagingHandler [startPage=" + startPage + ", endPage=" + endPage + ", realEndPage=" + realEndPage
				+ ", prev=" + prev + ", next=" + next + ", pgvo=" + pgvo + ", totalCount=" + totalCount + "]";
	}
	
	
	
	
	
}
