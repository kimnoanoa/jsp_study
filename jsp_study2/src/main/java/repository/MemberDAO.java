package repository;

import java.util.List;

import domain.MemberVO;

public interface MemberDAO {

	int register(MemberVO mvo);

	MemberVO getID(MemberVO mvo);

	int lastlogin(String id);

	List<MemberVO> selectList();

	int update(MemberVO mvo);

	int delete(String id);

}
