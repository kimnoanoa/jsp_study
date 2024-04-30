<%@  page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri ="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>My JSP project test~!</h1>

<c:if test ="${ses.id eq null }">
	<form action="/memb/login" method ="post">
		id : <input type ="text" name = "id">
		pwd : <input type ="password" name = "pwd">
		<button type="submit">로그인</button>

	</form>
	</c:if>


<div>
<c:if test ="${ses.id ne null }">
	<a href="/memb/modify"><button>회원정보수정</button></a>
	<a href="/memb/list"><button>회원리스트</button></a>
	<a href="/memb/logout"><button>로그아웃</button></a> <br>
${ses.id } 님이 로그인하셨습니다. <br>
계정 생성일 : ${ses.regdate } / 마지막 접속일 " ${ses.lastlogin }" <br>
</c:if>
</div>

<a href="/brd/register"><button>글쓰기</button></a>
<a href="/brd/list"><button>리스트 보기</button></a> <br>
<a href="/memb/join"><button>회원가입하기</button></a> <br>
<a href="/brd/list"><button>내가 작성한 글 보기</button></a> <br>

<script type="text/javascript">
const msg_login = `<c:out value ="${msg_login }"></c:out>`;
console.log(msg_login);
if (msg_login === '-1'){
	alert("로그인 정보가 일치하지 않습니다.");
}
const msg_update = `<c:out value ="${msg_update}"/>`;
console.log(msg_update);
if (msg_update === 'ok'){
	alert("회원정보 수정완료! 다시 로그인하세요.");
}

const msg_delete = `<c:out value ="${msg_delete}"/>`;
console.log(msg_delete);
if (msg_delete === 'ok'){
	alert("회원 탈퇴가 완료되었습니다.");
}



</script>
</body>
</html>