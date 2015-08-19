<%@page import="com.anverm.login.util.LoginUtil" %>

<%
  String userName = (String)session.getAttribute("userName");
  String callbackURL = LoginUtil.getCallbackURL(request);
  String signOutUrl = LoginUtil.getSignOutURL(session, callbackURL);
%>

<div>
  <div><h1>Application Dashboard</h1></div>
  <div><h4><%=userName %></h4></div>
  <div><h5><a href="<%=signOutUrl %>" >Logout</a></h4></div>
</div>