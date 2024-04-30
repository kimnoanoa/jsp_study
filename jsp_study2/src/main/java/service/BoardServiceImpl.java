package service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import domain.PagingVO;
import repository.BoardDAO;
import repository.BoardDAOImpl;

public class BoardServiceImpl implements BoardService {
	
	private static final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);
	private BoardDAO bdao;
	
	
	public BoardServiceImpl() {
		bdao = new BoardDAOImpl();
	}

	@Override
	public int register(BoardVO bvo) {
		log.info("insert service in!");
		// insert, update,delete commit 필수
		return bdao.insert(bvo);
	}

	@Override
	public List<BoardVO> getList(PagingVO pgvo) {
		log.info("list service in!");
		return bdao.selectList(pgvo);
	}

	@Override
	public BoardVO getDetail(int bno) {
		log.info("detail service in!");
		return bdao.selectOne(bno);
	}

	@Override
	public int update(BoardVO bvo) {
		log.info("update service in!");
		return bdao.update(bvo);
	}

	@Override
	public int delete(int bno) {
		CommentServiceImpl csv = new CommentServiceImpl();
		int isOk = csv.removeAll(bno);
		log.info("comment removeAll >>{}",isOk);
		return bdao.delete(bno);
	}

	@Override
	public int getTotal(PagingVO pgvo) {
		log.info("total service in!");
		return bdao.getTotal(pgvo);
	}

	@Override
	public String getFileName(int bno) {
		log.info("getFileName service in!");
		return bdao.getFileName(bno);
	}

	@Override
	public int readCountUpdate(int bno) {
		// TODO Auto-generated method stub
		return bdao.readCountUpdate(bno);
	}
	
	
	

}
