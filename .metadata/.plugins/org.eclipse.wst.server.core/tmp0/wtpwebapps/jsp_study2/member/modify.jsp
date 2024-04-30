<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h1>회원정보 수정 페이지</h1>

<form action="/memb/update" method="post">
<table border="1">
<tr>
	<th>id</th>
	<td><input type="text" name ="id" value="${ses.id }" readonly="readonly"></td>
</tr>
<tr>
	<th>password</th>
	<td><input type="text" name ="pwd" value="${ses.pwd}"></td>
</tr>
<tr>
	<th>email</th>
	<td><input type="text" name ="email" value="${ses.email }"></td>
</tr>
<tr>
	<th>age</th>
	<td><input type="number" name ="age" value="${ses.age }"></td>
</tr>
<tr>
	<th>phone</th>
	<td><input type="text" name ="phone" value="${ses.phone }"></td>
</tr>

</table>
<button type="submit">수정</button>
<a href ="/memb/delete"><button type="button" >삭제</button></a>
</form>
<a href ="/index.jsp"><button>목록</button></a>
</body>
</html>