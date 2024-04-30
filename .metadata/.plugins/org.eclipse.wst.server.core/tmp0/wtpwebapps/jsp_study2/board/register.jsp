<%@  page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1> Board Register Page</h1>

<!--enctype ="multipart/form-data" : 이미지 파일을 가져갈 때 사용하는 타입  -->
<!-- 파일 : 종류/확장자 image/jpg imaage/png -->
<form action="/brd/insert" method="post" enctype ="multipart/form-data">
title : <input type="text" name="title"> <br>
writer : <input type="text" name="writer" value="${ses.id }"><br>
content : <textarea rows="10" cols="30" name="content"></textarea><br>
첨부파일 : <br> <input type ="file" name ="imageFile" accept="image/png, image/jpg, image/gif, image/jpeg"> <br>
button : <button  type="submit"  >전송</button>
</form>

</body>
</html>