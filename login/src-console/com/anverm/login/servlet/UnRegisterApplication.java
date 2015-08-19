/*******************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * There is no copyright registered for following awesome work.
 * Whatever contributed here is not private but do no copy. Instead
 * collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

import com.anverm.login.model.ApplicationManager;
import com.anverm.login.util.LoginUtil;
import com.anverm.webutil.CommonUtil;

/**
 * Servlet implementation class UnRegisterApplication
 */
/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 13-Nov-2014
 */
@WebServlet("/console/UnRegisterApplication")
public class UnRegisterApplication extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = Logger.getLogger(UnRegisterApplication.class);
  
  /**
   * @see HttpServlet#HttpServlet()
   */
  public UnRegisterApplication() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String applicationId = request.getParameter("appId");
    if(CommonUtil.isEmpty(applicationId)){
      logger.error("Application id was not found. Not deleting application details.");
      response.sendError(HttpStatus.SC_BAD_REQUEST, "Please contact system administrator");
      return;
    }
    
    String userName = LoginUtil.getUserName(request);
    if(CommonUtil.isEmpty(userName)){
      logger.error("username was not found. Not deleting application details.");
      response.sendError(HttpStatus.SC_BAD_REQUEST, "Please contact system administrator");
      return;
    }
    
    int appId = -1;
    try{
      appId = Integer.parseInt(applicationId);
    }catch(NumberFormatException e){
      logger.error("Exception occurred while parsing application id", e);
      response.sendError(HttpStatus.SC_BAD_REQUEST, "Please contact system administrator");
      return;      
    }
    
    try {
      ApplicationManager.getInstance().unRegisterApplication(userName, appId);
      response.sendRedirect("/console/dashboard");
    } catch (SQLException e) {
      logger.error("Exception occurred while deleting application details", e);
      response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Please contact system administrator");
      return;
    }
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

}
