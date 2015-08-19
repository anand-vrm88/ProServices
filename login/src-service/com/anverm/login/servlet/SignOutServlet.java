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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.apache.log4j.Logger;

import com.anverm.login.model.LoginManager;
import com.anverm.login.session.User;
import com.anverm.login.session.UserSession;
import com.anverm.login.util.LoginConstants;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 03-Oct-2014
 */
@WebServlet("/service/SignOut")
public class SignOutServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = Logger.getLogger(SignOutServlet.class);

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String id = request.getParameter("tokenId");
    String callbackURL = request.getParameter("callback");
    LoginManager loginManager = LoginManager.getInstance();
    try {
      int tokenId = Integer.parseInt(id);
      loginManager.logout(tokenId);
      response.sendRedirect(callbackURL);
    } catch (Exception e) {
      logger.error("Exception occurred while signing-out user for tokenId: " + id, e);
      response.sendError(Response.SC_INTERNAL_SERVER_ERROR, "Please contact system administrator.");
      return;
    }
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }
}
