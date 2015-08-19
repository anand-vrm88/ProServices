<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="head-container.jsp" %>
<title>Applications Registration Console</title>
</head>
<body>
<%@include file="header.jsp" %>

<form action="/console/registerApplication" method="post">
  <table>
	<tr><td>Application Name</td><td> <input type="text" name="appName"></td></tr>
	<tr><td>Host Name</td><td> <input type="text" name="hostName"></td></tr>
	<tr><td>Port Number</td><td> <input type="text" name="portNumber"></td></tr>
	<tr><td>Context Path</td><td> <input type="text" name="appContext"></td></tr>
	<tr><td>Public Resource</td><td> <input type="text" name="publicResource"></td></tr>
	<tr><td colspan="2" align="right"><input type="submit" value="Register"></td></tr>
  </table>
</form>

<%@include file="footer.jsp" %>
</body>
</html>