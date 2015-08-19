<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Random"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.anverm.analytics.logger.BasicAnalyticsLogger" %>
<%@page import="java.util.Random" %>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Smart Idea</title>
</head>
<body>
This is Smart Idea default page.
<%!  private int generateSessionId(){
  int min = 1000000;
  int max = 9999999;
  Random generator = new Random();
  int randomNumber = generator.nextInt(max - min) + min;
  if(randomNumber == min) {
      // Since the random number is between the min and max values, simply add 1
      return min + 1;
  }
  else {
      return randomNumber;
  }
} %>

<%
  int visitCorrelId = generateSessionId();
  long endUserId = generateSessionId();
  String userAgent = request.getHeader("User-Agent");
  String referer = request.getHeader("Referer");
  //SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
  //Date date = format.parse(request.getHeader("Date"));
  long logTime = System.currentTimeMillis();
  BasicAnalyticsLogger.getInstance().logPageVisitAnalytics(visitCorrelId, endUserId, userAgent, referer, logTime);
%>
</body>
</html>