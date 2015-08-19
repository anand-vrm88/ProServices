package com.anverm.login.servlet;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.anverm.login.model.ApplicationManager;
import com.anverm.login.model.LoginManager;

/**
 * Servlet implementation class InitServlet
 */
public class InitServlet extends HttpServlet {
  private static final Logger logger = Logger.getLogger(InitServlet.class);
  private static final long serialVersionUID = 1L;
  private LoginManager loginManager;
  private ApplicationManager applicationManager;

  /**
   * 
   */
  @Override
  public void init() throws ServletException {
    super.init();
    Context envContext = null;
    try {
      envContext = new InitialContext();
      Context initContext  = (Context)envContext.lookup("java:/comp/env");
      DataSource loginDS = (DataSource)initContext.lookup("jdbc/loginDS");
      //DataSource loginDS = (DataSource) ctx.lookup("java:comp/env/loginDS");
      loginManager = LoginManager.init(loginDS);
      applicationManager = ApplicationManager.init(loginDS);
    } catch (NamingException e) {
      logger.error("Exception occurrent while initializing login filter", e);
      throw new ServletException("Exception occurrent while initializing login filter", e);
    }
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    loginManager.destroy();
  }

  
  /**
   * 
   */
  @Override
  public void destroy() {
    super.destroy();
    if (loginManager != null) {
      loginManager.destroy();
    }
    if(applicationManager != null){
      applicationManager.destroy();
    }
  }
  
  

}
