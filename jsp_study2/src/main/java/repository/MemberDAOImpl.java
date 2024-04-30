package repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.MemberController;
import domain.MemberVO;
import orm.DatabaseBuilder;

public class MemberDAOImpl implements MemberDAO {
	
	private static final Logger Log = LoggerFactory.getLogger(MemberController.class);
	
	private SqlSession sql;
	
	public MemberDAOImpl() {
		new DatabaseBuilder();
		sql = DatabaseBuilder.getFactory().openSession();
	}
	

	@Override
	public int register(MemberVO mvo) {
		Log.info("join dao in!");
		int isOk = sql.insert("MemberMapper.insert",mvo);
		if(isOk >0) {sql.commit();}
		return isOk;
	}


	@Override
	public MemberVO getID(MemberVO mvo) {
		Log.info("login dao in!");
		return sql.selectOne("MemberMapper.login",mvo);
	}


	@Override
	public int lastlogin(String id) {
		Log.info("lastlogin dao in!");
		int isOk = sql.update("MemberMapper.last",id);
		if(isOk >0) {sql.commit();}
		return isOk;
	
	}


	@Override
	public List<MemberVO> selectList() {
		Log.info("selectList dao in!");
		return sql.selectList("MemberMapper.list");
	}


	@Override
	public int update(MemberVO mvo) {
		Log.info("update dao in!");
		int isOk = sql.update("MemberMapper.update",mvo);
		if(isOk >0) {sql.commit();}
		return isOk;
	}


	@Override
	public int delete(String id) {
		Log.info("delete dao in!");
		int isOk = sql.update("MemberMapper.delete",id);
		if(isOk >0) {sql.commit();}
		return isOk;
	}

}
