<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="com.anverm.login.model.ApplicationManager" %>
<%@page import="com.anverm.login.session.ApplicationData" %>
<%@page import="com.anverm.login.util.LoginUtil" %>
<%@page import="com.anverm.webutil.CommonUtil" %>
<%@page import="org.apache.commons.httpclient.HttpStatus" %>

<%
  String applicationId = request.getParameter("appId");
  if(CommonUtil.isEmpty(applicationId)){
    response.sendError(HttpStatus.SC_BAD_REQUEST, "AppId was not found.");
    return;
  }
  
  int appId = -1;
  try{
    appId = Integer.parseInt(applicationId);
  }catch(NumberFormatException e){
    response.sendError(HttpStatus.SC_BAD_REQUEST, "AppId is invalid = "+applicationId);
    return;
  }
  
  ApplicationData applicationDetails = ApplicationManager.getInstance().getApplicationDetails(appId);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="head-container.jsp" %>
<title>Application Edit Console</title>
</head>
<body>
<%@include file="header.jsp" %>

<form action="/console/editApplication" method="post">
  <table>
  <tr><td>App Id</td><td> <%=applicationDetails.getAppId() %> <input type="hidden" name="appId" value="<%=applicationDetails.getAppId() %>"> </td></tr>
  <tr><td>Application Name</td><td> <input type="text" name="appName" value="<%=applicationDetails.getAppName() %>"></td></tr>
  <tr><td>Host Name</td><td> <input type="text" name="hostName" value="<%=applicationDetails.getDomainName() %>"></td></tr>
  <tr><td>Port Number</td><td> <input type="text" name="portNumber" value="<%=applicationDetails.getPortNumber() %>"></td></tr>
  <tr><td>Context Path</td><td> <input type="text" name="appContext" value="<%=applicationDetails.getAppContext() %>"></td></tr>
  <tr><td>Public Resource</td><td> <input type="text" name="publicResource" value="<%=applicationDetails.getPublicResource() %>"></td></tr>
  <tr><td colspan="2" align="right"><input type="submit" value="Save"></td></tr>
  </table>
</form>

<%@include file="footer.jsp" %>
</body>
</html>