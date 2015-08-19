/*******************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER. There is no copyright registered for
 * following awesome work. Whatever contributed here is not private but do no copy. Instead
 * collaborate and spread knowledge.
 *******************************************************************************/
package com.anverm.login.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.anverm.login.util.LoginConstants;
import com.anverm.login.util.LoginUtil;
import com.anverm.webutil.CommonUtil;

/**
 * @author anand <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$ Created on: 02-Oct-2014
 *         LoginFilter.java
 */
public class LoginClientFilter implements Filter {
  private static final Logger logger = Logger.getLogger(LoginClientFilter.class);

  /**
   * @see Filter#init(FilterConfig)
   */
  public void init(FilterConfig fConfig) throws ServletException {
    // TODO Auto-generated method stub
  }

  /**
   * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    
    if(isInitializtionRequired(req)){
      initializeSession(req, res);
      String url = LoginUtil.getCallbackURL(req);
      PrintWriter pw = new PrintWriter(res.getOutputStream());
      pw.print("<html><head></head><body><script>window.location=\""+url+"\";</script></body></html>");
      pw.flush();
      pw.close();
      return;
    }
    
    if (isAnonymousResource(req) || isSessionValid(req, res)) {
      chain.doFilter(request, response);
    } else {
      removeTokenFromCookie(req, res);
      invalidateSession(req);
      String callbackURL = LoginUtil.getCallbackURL(req);
      String signInURL = LoginUtil.getSignInURL(callbackURL);
      res.sendRedirect(signInURL);
    }
  }
  
  
  
  private void invalidateSession(HttpServletRequest request){
    HttpSession session = request.getSession(false);
    if(session != null){
      session.invalidate();
    }
  }
  
  private boolean isInitializtionRequired(HttpServletRequest request){
/*    HttpSession session = request.getSession(false);
    if(session != null && !CommonUtil.isEmpty(session.getAttribute("userName")) && session.getAttribute("tokenId") != null){
      return false;
    }*/
    String token = request.getParameter("token");
    String tokenId = request.getParameter("tokenId");
    String userName = request.getParameter("userName");
    boolean initializationExpected = !CommonUtil.isEmpty(token) && !CommonUtil.isEmpty(tokenId) && !CommonUtil.isEmpty(userName);
    return initializationExpected && LoginUtil.validateAuthParams(token, tokenId, userName);
  }
  
  private void initializeSession(HttpServletRequest request, HttpServletResponse response){
    HttpSession session = createSession(request);
    String token = request.getParameter("token");
    String userName = request.getParameter("userName");
    int tokenId = -1;
    try {
      tokenId = Integer.parseInt(request.getParameter("tokenId"));
      session.setAttribute("tokenId", tokenId);
      session.setAttribute("userName", userName);
      putTokenInResponse(response, token);
    } catch (NumberFormatException e) {
      logger.warn("Found corrupted tokenId in request parameter. Failed to initialize session.");
      throw e;
    }    
  }

  private HttpSession createSession(HttpServletRequest request){
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    return request.getSession(true);
  }
  
  private boolean isSessionValid(HttpServletRequest request, HttpServletResponse response) {
    String token = getToken(request, response);
    if(token == null){
      return false;
    }
    int tokenId = LoginUtil.getTokenId(request);
    if(tokenId < 0){
      return false;
    }
    return LoginUtil.authenticate(tokenId, token);
  }
  
  private boolean isAnonymousResource(HttpServletRequest request){
    String url = request.getRequestURL().toString();
    String sub = null;
    if(url.contains("?")){
      sub = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?") - 1) ;
    }else{
      sub = url.substring(url.lastIndexOf("/") + 1);
    }
    return sub.equals(LoginUtil.getAnonymousResource());
  }

  private String getToken(HttpServletRequest request, HttpServletResponse response) {
    return getTokenFromCookie(request);
  }

  private String getTokenFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null)
      for (Cookie cookie : cookies) {
        if (LoginConstants.Auth_COOKIE_NAME.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    return null;
  }

  private void putTokenInResponse(HttpServletResponse response, String token) {
    Cookie tokenCookie = new Cookie("token", token);
    tokenCookie.setDomain(LoginUtil.getDomainName());
    tokenCookie.setHttpOnly(true);
    tokenCookie.setPath("/");
    response.addCookie(tokenCookie);
  }
  
  private void removeTokenFromCookie(HttpServletRequest request, HttpServletResponse response) {
    String token = getTokenFromCookie(request);
    if(token == null || token.length() == 0){
      return;
    }
    Cookie tokenCookie = new Cookie("token", token);
    tokenCookie.setDomain(LoginUtil.getDomainName());
    tokenCookie.setHttpOnly(true);
    tokenCookie.setPath("/");
    tokenCookie.setMaxAge(0);
    response.addCookie(tokenCookie);
  }
  
  /**
   * @see Filter#destroy()
   */
  public void destroy() {
    // TODO Auto-generated method stub
  }
}
