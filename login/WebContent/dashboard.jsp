<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="com.anverm.login.model.ApplicationManager" %>
<%@page import="com.anverm.login.session.ApplicationData" %>
<%@page import="com.anverm.login.util.LoginUtil" %>
<%@page import="java.util.List" %>
    
<%
  List<ApplicationData> userApplicationsList = ApplicationManager.getInstance().getUserApplicationsList((String)session.getAttribute("userName"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="head-container.jsp" %>
<title>Applications List Console</title>
</head>
<body>
  <%@include file="header.jsp"%>
  <div>
    <a href="/console/registerApp">Register Application</a>
  </div>
  <div>
    <p>Your applications details are as below:</p>

  <table border="1px" width="100%">
    <tr>
      <th>App Id</th>
      <th>App Name</th>
      <th>Domain</th>
      <th>Port</th>
      <th>App Context</th>
      <th>Public Resource</th>
      <th>Conf File</th>
      <th>Edit</th>
      <th>Remove App</th>
    </tr>
    <%
      for (ApplicationData applicationData : userApplicationsList) {
    %>
    <tr>
      <td><%=applicationData.getAppId()%></td>
      <td><%=applicationData.getAppName()%></td>
      <td><%=applicationData.getDomainName()%></td>
      <td><%=applicationData.getPortNumber()%></td>
      <td><%=applicationData.getAppContext()%></td>
      <td><%=applicationData.getPublicResource()%></td>
      <td>
        <a href="<%=LoginUtil.getLoginServiceURL() + "/console/ConfFileDownloader?appId=" + applicationData.getAppId()%>">
          Click to download
        </a>
      </td>
      <td>
        <a href="<%=LoginUtil.getLoginServiceURL() + "/console/editApp?appId=" + applicationData.getAppId()%>">
          Click to edit
        </a>
      </td>
      <td>
        <a href="<%=LoginUtil.getLoginServiceURL() + "/console/UnRegisterApplication?appId=" + applicationData.getAppId()%>">
          Click to delete
        </a>
      </td>
    </tr>
    <%
      }
    %>
    </table>
  </div>
  <%@include file="footer.jsp" %>
</body>
</html>