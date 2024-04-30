package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.MemberVO;
import service.MemberService;
import service.MemberServiceImpl;

@WebServlet("/memb/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger Log = LoggerFactory.getLogger(MemberController.class);
	private RequestDispatcher rdp;
	private String destPage;
	private int isOk;
	
	//service
	private MemberService msv;
	
	
       
    
    public MemberController() {
    	msv = new MemberServiceImpl();
    }
    
    

	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		response.setContentType("text/html; charset=utf-8");
		
		String uri = request.getRequestURI();
		Log.info(uri);
		
		String path = uri.substring(uri.lastIndexOf("/")+1);
		Log.info(path);
		
		switch(path) {
		case "join":
			destPage = "/member/join.jsp";
			break;
			
		case "register":
			try {
				String id = request.getParameter("id");
				String pwd = request.getParameter("pwd");
				String email = request.getParameter("email");
				int age = Integer.parseInt(request.getParameter("age"));
				String phone = request.getParameter("phone");
				
				MemberVO mvo = new MemberVO(id,pwd,email,age,phone);
				Log.info("join mvo >>>{}",mvo);
				isOk = msv.register(mvo);
				Log.info("join>>",(isOk>0)? "OK":"FAIL");
				destPage = "/index.jsp";
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case "login":
			
			try {
				
				String id = request.getParameter("id");
				String pwd = request.getParameter("pwd");
				MemberVO mvo = new MemberVO(id,pwd);
				
				Log.info("login>>>{}",mvo);
				MemberVO loginMvo = msv.login(mvo);
				
				Log.info("return loginMvo >>>>{} ",loginMvo);
				
				//loginMvo 객체가없다면 null;
				if (loginMvo != null) {
					//session 에 저장
					//연결된 세션 객체가 있다면 기존 객체를 가져오고, 없으면 생성
					HttpSession ses = request.getSession();
					ses.setAttribute("ses",loginMvo);
					ses.setMaxInactiveInterval(10*60);
					
				}else {
					//로그인 객체가 없다면
					// index.jsp로 메세지 전송
					request.setAttribute("msg_login", -1);
				}
				destPage = "/index.jsp";
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case "logout":
			try {
				
				HttpSession ses = request.getSession();
				MemberVO mvo = (MemberVO)ses.getAttribute("ses");
				Log.info("ses에서 추출한 mvo >>>>{} " ,mvo);
				
				isOk = msv.lastlogin(mvo.getId());
				Log.info("logout >>", (isOk>0)? "OK":"FAIL");
				ses.invalidate(); //세선 무효화
				destPage ="/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case "list":
			try {
			
			List<MemberVO> list = msv.getList();
			request.setAttribute("list", list);
			destPage = "/member/list.jsp";
				
			} catch (Exception e) {
				e.printStackTrace();
			
			}
			break;
			
		case "modify":
				destPage ="/member/modify.jsp";
			try {
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case "update" :
			try {
				String id = request.getParameter("id");
				String pwd = request.getParameter("pwd");
				String email = request.getParameter("email");
				int age = Integer.parseInt(request.getParameter("age"));
				String phone = request.getParameter("phone");
				
				MemberVO mvo = new MemberVO(id,pwd,email,age,phone);
				int isOk = msv.update(mvo);
				
				Log.info("update" +(isOk>0? "성공" :"실패 =>") +isOk );
				
				HttpSession ses = request.getSession();
				
				request.setAttribute("msg_login", -2);
				destPage = "/index.jsp";
				
				ses.invalidate();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
			
		case "delete":
			try {
				HttpSession ses = request.getSession();
				String id = ((MemberVO)ses.getAttribute("ses")).getId();
				int isOk= msv.delete(id);
				
				Log.info("delete >>>{}"+(isOk >0? "성공":"실패 =>" )+isOk);
				if(isOk>0) {
					request.setAttribute("msg_delete", "ok");
					
					ses.invalidate();
				}
				
				destPage = "/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case "searchOne":
			try {
				HttpSession ses = request.getSession();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
			
		}
		
		
		
		rdp = request.getRequestDispatcher(destPage);
		rdp.forward(request, response);
		
	}




	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		service(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		service(request, response);
	}

}
