package repository;

import java.util.List;

import domain.BoardVO;
import domain.PagingVO;

public interface BoardDAO {

	int insert(BoardVO bvo);

	List<BoardVO> selectList(PagingVO pgvo);

	BoardVO selectOne(int bno);

	int update(BoardVO bvo);

	int delete(int bno);

	int getTotal(PagingVO pgvo);

	String getFileName(int bno);

	int readCountUpdate(int bno);



}