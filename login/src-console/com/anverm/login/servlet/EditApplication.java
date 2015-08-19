/*******************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * There is no copyright registered for following awesome work.
 * Whatever contributed here is not private but do no copy. Instead
 * collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

import com.anverm.login.model.ApplicationManager;
import com.anverm.login.util.ApplicationUtil;

/**
 * Servlet implementation class RegisterApplication
 */

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 13-Nov-2014
 */
@WebServlet("/console/editApplication")
public class EditApplication extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = Logger.getLogger(EditApplication.class);

  /**
   * Default constructor.
   */
  public EditApplication() {
    // TODO Auto-generated constructor stub
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.sendError(HttpStatus.SC_FORBIDDEN, "Get method is not allowed for this request.");
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int appId = -1;
    String appName = request.getParameter("appName");
    String hostName = request.getParameter("hostName");
    int portNumber = -1;
    String appContext = request.getParameter("appContext");
    String publicResource = request.getParameter("publicResource");
    try {
      appId = ApplicationUtil.getApplicationId(request, response);
      portNumber = Integer.parseInt(request.getParameter("portNumber"));
      ApplicationManager.getInstance().updateApplicationDetails(appId, appName, hostName, portNumber, appContext, publicResource);
      response.sendRedirect("/console/dashboard");
    } catch (Exception e) {
      logger.error("Exception occurred while editing application for app id = "+appId, e);
      response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Contact System Administrator if this error persists.");
    }
  }

}
