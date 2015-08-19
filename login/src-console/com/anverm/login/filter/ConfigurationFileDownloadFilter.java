package com.anverm.login.filter;

import java.io.IOException;

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
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.anverm.login.util.LoginUtil;

/**
 * Servlet Filter implementation class ConfigurationFileDownloadFilter
 */
@WebFilter("/ConfigurationFileDownloadFilter")
public class ConfigurationFileDownloadFilter implements Filter {

  private static final Logger logger = Logger.getLogger(LoginServiceFilter.class);
  private DataSource loginDS = null;

  public void init(FilterConfig fConfig) throws ServletException {
    Context envContext = null;
    try {
      envContext = new InitialContext();
      Context initContext = (Context) envContext.lookup("java:/comp/env");
      loginDS = (DataSource) initContext.lookup("jdbc/loginDS");
    } catch (NamingException e) {
      logger.error("Exception occurrent while initializing login filter", e);
      throw new ServletException("Exception occurrent while initializing login filter", e);
    }
  }

  /**
   * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest)request;
    int tokenId = LoginUtil.getTokenId(req);
    
    chain.doFilter(request, response);
  }

  /**
   * @see Filter#destroy()
   */
  public void destroy() {
    // TODO Auto-generated method stub
  }

}
