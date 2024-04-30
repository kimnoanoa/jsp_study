package service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.MemberVO;
import repository.MemberDAO;
import repository.MemberDAOImpl;

public class MemberServiceImpl implements MemberService {
	
	private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);
	
	private MemberDAO mdao;
	public MemberServiceImpl() {
		mdao= new MemberDAOImpl();
	}
	@Override
	public int register(MemberVO mvo) {
		log.info("register service in!");
		return mdao.register(mvo);
	}
	@Override
	public MemberVO login(MemberVO mvo) {
		log.info("login service in!");
		return mdao.getID(mvo);
	}
	@Override
	public int lastlogin(String id) {
		log.info("logout service in!");
		return mdao.lastlogin(id);
	}
	@Override
	public List<MemberVO> getList() {
		log.info("list service in!");
		return mdao.selectList();
	}
	@Override
	public int update(MemberVO mvo) {
		log.info("update service in!");
		return mdao.update(mvo);
	}
	@Override
	public int delete(String id) {
		log.info("delete service in!");
		return mdao.delete(id);
	}

}
