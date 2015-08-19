/*******************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * There is no copyright registered for following awesome work.
 * Whatever contributed here is not private but do no copy. Instead
 * collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

import com.anverm.login.model.LoginManager;
import com.anverm.login.model.SQLHandler;
import com.anverm.login.session.ApplicationData;
import com.anverm.login.util.LoginUtil;

/**
 * Servlet implementation class ConfigurationFileDownloader
 */

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 13-Nov-2014
 */
@WebServlet("/console/ConfFileDownloader")
public class ConfigurationFileDownloader extends HttpServlet {
  private static final Logger logger = Logger.getLogger(ConfigurationFileDownloader.class);
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int appId = Integer.parseInt(request.getParameter("appId"));
    try {
      ApplicationData applicationData = LoginManager.getInstance().getApplicationDetails(appId);
      StringBuilder clientConf = new StringBuilder("login.service.url="+LoginUtil.getLoginServiceURL()+"\n");
      clientConf.append("appId="+applicationData.getAppId()+"\n");
      clientConf.append("anonymous.resource="+applicationData.getPublicResource()+"\n");
      clientConf.append("domain.name="+applicationData.getDomainName());
      response.setContentType("application/octet-stream");
      response.setContentLength(clientConf.length());
      response.setHeader("Content-Disposition", "attachment; filename=\"client.props\"");
      PrintWriter pw = new PrintWriter(response.getOutputStream());
      pw.println(clientConf);
      pw.flush();
      pw.close();
      System.out.println("Completed.");
    } catch (SQLException e) {
      logger.error("Exception occurred while downloading configuration file", e);
      response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Please contact system adminstrator");
    }
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    throw new ServletException(new OperationNotSupportedException("Only GET http method is allowed."));
  }
}
