package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import domain.PagingVO;
import handler.FileRemoveHandler;
import handler.PagingHandler;
import net.coobird.thumbnailator.Thumbnails;
import service.BoardService;
import service.BoardServiceImpl;



@WebServlet({"/brd/*"})
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log =LoggerFactory.getLogger(BoardController.class);
	//jsp에서 받은 요청을 처리
	
     
	private RequestDispatcher rdp;
	private String destPage;
	private int isOk;
	
	private BoardService bsv;
	private String savePath; //파일업로드 저장경로
	
	public BoardController() {
		bsv = new BoardServiceImpl();
		
		
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
  
    
    

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("");
		
		String uri = request.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/")+1);
		
		log.info(path);
		
		switch(path) {
		case "register":
			destPage = "/board/register.jsp";
			break;
			
		case "insert":
			try {
				 // 파일을 업ㄹ드할 물리적인 경로 설정
				savePath = getServletContext().getRealPath("/_fileUpload");
				log.info(">>>> savePath >>>{}",savePath);
				
				File fileDir = new File(savePath);
				log.info(">>>> fileDir >>>{}",fileDir);
				
				
				DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
				fileItemFactory.setRepository(fileDir); //저장할 위치를 file 객체로 지정
				fileItemFactory.setSizeThreshold(1024*1024*3); //파일 저장을 위한 임시 메모리
				
				BoardVO bvo = new BoardVO();
				
				// multipart/form-data 형식으로 넘어온 request객체를 다루기 쉽게  변환해주는 역할
				
				ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
				
				List<FileItem> itemList = fileUpload.parseRequest(request);
				
				for(FileItem item : itemList) {
					//title, content, writer => text
					// imageFile => image
					switch(item.getFieldName()){
					case "title" :
						String title = item.getString("utf-8");
						bvo.setTitle(title);
						
						break;
					case "writer":
						bvo.setWriter(item.getString("utf-8"));
						
						break;
					case "content":
						bvo.setContent(item.getString("utf-8"));
						
						break;
					case "imageFile":
						//이미지파일 여부 체크
						if(item.getSize() >0) {
							// 파일이름추출
							//getName() :  순수 파일이름, 걍로
							
							String fileName = item.getName()
								.substring(item.getName().lastIndexOf(File.separator)+1);
							//File.separator : 파일경로 기호 =>운영체제마다 다를 수 잇어 자동변환
							//시스템의 시간을 이용하여 파일을 구분. 시간_dog.jpg
							fileName = System.currentTimeMillis()+"_"+fileName;
							
							File uploadFilePath = new File(fileDir+File.separator+fileName);
							log.info(">>>> uploadFilePath>> {} ",uploadFilePath);
							
							//저장
							try {
								item.write(uploadFilePath); //객체를 디스크에 쓰기
								bvo.setImageFile(fileName); //bvo에 저장
								//썸네일작업 : 리스트페이지에서 트래픽과다 사용방지
								Thumbnails.of(uploadFilePath).size(75, 75).toFile(new File(fileDir+File.separator+"_th_"+fileName));
								
								
							} catch (Exception e) {
								log.info(">>> fileWriter on disk error");
								e.printStackTrace();
							}
						}
						
						break;
						
					}
					
					
				}
				log.info(">>>> bvo >>>{}," ,bvo);
				
				
				
//				String title = request.getParameter("title");
//				String writer = request.getParameter("writer");
//				String content = request.getParameter("content");
//				
//				BoardVO bvo = new BoardVO(title,writer,content);
				int isOk = bsv.register(bvo);
				log.info("insert " + (isOk >0? "성공":"실패 =>")+ isOk);
				destPage = "/index.jsp";
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case "list" :
			try {
				// 페이징 객체 설정
				PagingVO pgvo = new PagingVO(); // 1/10 /0 /type /keyword
				
				
				if(request.getParameter("pageNo") != null) {
					
					int pageNo = Integer.parseInt(request.getParameter("pageNo"));
					int qty = Integer.parseInt(request.getParameter("qty"));
					String type =request.getParameter("type");
					String keyword =request.getParameter("keyword");
					pgvo = new PagingVO(pageNo,qty,type,keyword);
				}
				
				
				
//				List <BoardVO> list = bsv.getList();
//				log.info("list >>>> {}",list );
				
				// paging 반영한 리스트 추출
				List <BoardVO> list = bsv.getList(pgvo);
//				totalCount DB 에서 검색해서 가져오기
				int totalCount = bsv.getTotal(pgvo);
				log.info(">>>>> totalCount >>>>> {}",totalCount);
				
				
				PagingHandler ph = new PagingHandler(pgvo,totalCount);
				log.info(">>>>> ph >>>>> {}",ph);
				request.setAttribute("list", list);
				request.setAttribute("ph", ph);
				
				
				destPage = "/board/list.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case "detail":
			try {
				int bno = Integer.parseInt(request.getParameter("bno"));
				if (path.equals("detail")) {
					int isOk = bsv.readCountUpdate(bno);
				}
				BoardVO bvo = bsv.getDetail(bno);
				log.info("detail bvo >>>{}",bvo);
				request.setAttribute("bvo", bvo);
				destPage = "/board/detail.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
			
		case "modify":
			try {
				int bno = Integer.parseInt(request.getParameter("bno"));
				BoardVO bvo = bsv.getDetail(bno);
				log.info("modify bvo >>>{}",bvo);
				request.setAttribute("bvo", bvo);
				destPage ="/board/modify.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case "update":
			try {
				savePath = getServletContext().getRealPath("/_fileUpload");
				File fileDir = new File(savePath);
				
				DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
				fileItemFactory.setRepository(fileDir);
				fileItemFactory.setSizeThreshold(1024*1024*3);
				
				BoardVO bvo = new BoardVO();
				ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
				
				List<FileItem> itemList = fileUpload.parseRequest(request);
				
				String old_file = null;
				for(FileItem item : itemList) {
					switch(item.getFieldName()) {
					case "bno":
						bvo.setBno(Integer.parseInt(item.getString("utf-8")));
						break;
					case "title":
						bvo.setTitle(item.getString("utf-8"));
						break;
					case "content":
						bvo.setContent(item.getString("utf-8"));
						break;
					case "imageFile":
						//기존파일 => 있을수도 있고, 없을수도 있음.
						old_file = item.getString("utf-8");
						break;
					case "newFile":
						//새로 추가된 파일
						if(item.getSize() >0) {
							//새로운 등록파일이 있다면?
							if(old_file != null) {
								// old_file 삭제작업
								// fileremoveHandler
								FileRemoveHandler fileHandler = new FileRemoveHandler();
								isOk = fileHandler.deleteFile(path, old_file);
							}
							// 새로운파일 등록작업
							String fileName = item.getName()
									.substring(item.getName().lastIndexOf(File.separator)+1);
							log.info(">>>> new File Name >>>{}",fileName);
							
//							log.info("uft-8");
							
							fileName = System.currentTimeMillis()+"_"+fileName;
							File uploadFilePath = new File(fileDir + File.separator+fileName);
							
							try {
								item.write(uploadFilePath);
								bvo.setImageFile(fileName);
								Thumbnails.of(uploadFilePath)
								.size(75,75)
								.toFile(new File(fileDir + File.separator+"_th_"+fileName));
							} catch (Exception e) {
								log.info("File Writer Update Error");
								e.printStackTrace();
							}
							
						}else {
							//기존파일은 있지만 새로운 이미지파일이 없다면,,
							bvo.setImageFile(old_file); //기존객체  bvo에 담기
							
							
						}
						break;
					}
				}
				int isOk = bsv.update(bvo);
				log.info("1212121212>>>>{}",bvo);
				log.info("update" + (isOk>0? "성공" :"실패")+isOk);
				destPage ="list";
				
				
				
				
//				int bno = Integer.parseInt(request.getParameter("bno"));
//				String title = request.getParameter("title");
//				String content = request.getParameter("content");
//				
//				BoardVO bvo = new BoardVO(bno,title,content);
//				int isOk = bsv.update(bvo);
//				
//				log.info("update" + (isOk>0? "성공" :"실패")+isOk);
//				destPage ="list";
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case "delete" :
			try {
				savePath = getServletContext().getRealPath("/_fileUpload");

				int bno = Integer.parseInt(request.getParameter("bno"));
				
				//댓글 ,파일도 같이삭제
				//bno 주고 파일이름찾아오기(DB에서)
				// 찾아온 이름이 있다면 FileRemoveHandler 를 이용하여 삭제
				String imageFileName = bsv.getFileName(bno);
				
				log.info(">> imageFileName>> {}",imageFileName);
				if(imageFileName != null) {
					int isDel =0;
					FileRemoveHandler fh = new FileRemoveHandler();
					isDel = fh.deleteFile(savePath, imageFileName);
				}
				
				//bno로 댓글삭제요청=> serviceImpl 에서 cdao에게 요청
				
				int isOk = bsv.delete(bno);
	            log.info("delete " + (isOk > 0 ? "성공" : "실패  => ") + isOk);
	            destPage = "list";
				
//				
//				int isOk = bsv.delete(bno);
//				
//				log.info("delete" + (isOk>0? "성공" :"실패")+isOk);
//				destPage ="list";
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			break;
			
			
			
			
			// 목적지 주소(destPage)로 데이터를 전달(RequestDispatcher)
		}
		rdp = request.getRequestDispatcher(destPage);
		//요청에 필요한 객체를 가지고 destPage에 적힌 경로로 이동
		rdp.forward(request, response);
		
	}



	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		service(request, response);
	}
	

}
