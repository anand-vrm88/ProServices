/*******************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * There is no copyright registered for following awesome work.
 * Whatever contributed here is not private but do no copy. Instead
 * collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.anverm.login.model.SQLHandler;
import com.anverm.login.util.LoginUtil;
import com.anverm.webutil.db.DBUtil;
import com.anverm.webutil.net.NetworkUtil;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 03-Oct-2014
 */
@WebFilter("/*")
public class LoginServiceFilter implements Filter {
  private static final Logger logger = Logger.getLogger(LoginServiceFilter.class);
  private DataSource loginDS = null;

  public void init(FilterConfig fConfig) throws ServletException {
    Context envContext = null;
    try {
      Context ctx = new InitialContext();
      envContext = new InitialContext();
      Context initContext  = (Context)envContext.lookup("java:/comp/env");
      loginDS = (DataSource)initContext.lookup("jdbc/loginDS");
    } catch (NamingException e) {
      logger.error("Exception occurrent while initializing login filter", e);
      throw new ServletException("Exception occurrent while initializing login filter", e);
    }
  }
    
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    Cookie authTokenCookie = null;
    boolean isSessionValid = false;
    Cookie[] cookies = req.getCookies();
    if(cookies != null)
    for (Cookie cookie : cookies) {
      if (com.anverm.login.util.LoginUtil.Auth_COOKIE_NAME.equals(cookie.getName())) {
        isSessionValid = true;
        authTokenCookie = cookie;
      }
    }

    if (isSessionValid) {
      String callbackURL = req.getParameter("callback");
      if (callbackURL != null && NetworkUtil.isURLValid(callbackURL)) {
        res.sendRedirect(callbackURL);
        return;
      }
      String referer = req.getHeader("referer");
      if (referer != null && NetworkUtil.isURLValid(referer)) {
        res.sendRedirect(referer);
        return;
      }
      int appId = com.anverm.login.util.LoginUtil.getAppId(authTokenCookie.getValue());

      Connection conn = null;
      try {
        conn = loginDS.getConnection();
        String appURL = SQLHandler.getAppURL(conn, appId);
        if (appURL != null && !"".equals(appURL)) {
          res.sendRedirect(appURL);
          return;
        }
      } catch (SQLException e) {
        logger.error("Exception occurred while analytizing request for login server", e);
        throw new ServletException("Exception occurred while analytizing request for login server", e);
      } finally {
        DBUtil.closeConnectionIgnoreException(conn);
      }
    }
    chain.doFilter(request, response);
  }
	
    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

}
