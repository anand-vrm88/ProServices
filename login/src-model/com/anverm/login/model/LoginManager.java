/*******************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER. There is no copyright registered for
 * following awesome work. Whatever contributed here is not private but do no copy. Instead
 * collaborate and spread knowledge.
 *******************************************************************************/
package com.anverm.login.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.anverm.login.session.ApplicationData;
import com.anverm.login.session.User;
import com.anverm.login.session.UserSession;
import com.anverm.webutil.db.DBUtil;

/**
 * @author anand <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$ Created on: 02-Oct-2014
 *         LoginManager.java
 */
public class LoginManager {
  private static final Logger logger = Logger.getLogger(LoginManager.class);
  private DataSource loginDS;
  private static LoginManager loginManager;
  private int TOKEN_EXPIRY_TIME = 24 * 60 * 60;

  private LoginManager(DataSource loginDS) throws ServletException {
    this.loginDS = loginDS;
  }

  public static LoginManager init(DataSource loginDS) throws ServletException {
    loginManager = new LoginManager(loginDS);
    return loginManager;
  }

  public static LoginManager getInstance() {
    if (loginManager == null) {
      throw new IllegalStateException("LoginManager was not initialized.");
    }
    return loginManager;
  }

  /*
   * public Response login(String username, String password){ JSONObject json = new JSONObject();
   * return Response.ok().entity(json).build(); }
   */

  public UserSession login(String userName, int appId, String password) throws SQLException {
    try {
      // 1.User Authentication
      if (authenticateUser(userName, appId, password)) {
        // 2.Create session
        return createSession(appId, userName);
      }else{
        //throw invalid credentials exception.
      }
    } catch (SQLException e) {
      logger.error("Exception occurred while loging-in user", e);
      //throw login service internal error exception.
      throw e;
    }
    return null;
  }

  public boolean authenticate(int tokenId, String token) throws SQLException {
    Connection conn = null;
    try {
      conn = loginDS.getConnection();
      String persistedToken = SQLHandler.getToken(conn, tokenId);
      if(token != null && token.length() > 0 && token.equals(persistedToken)){
        return true;
      }
    } catch (SQLException e) {
      logger.error("Exception occurred while authenticating token: " + token, e);
      throw e;
    } finally {
      DBUtil.closeConnectionIgnoreException(conn);
    }
    return false;
  }  
  
  public void logout(int sessionId) throws SQLException {
    Connection conn = null;
    try {
      conn = loginDS.getConnection();
      SQLHandler.removeSession(conn, sessionId);
    } catch (SQLException e) {
      logger.error("Exception occurred while loging-out user", e);
      throw e;
    } finally{
      DBUtil.closeConnectionIgnoreException(conn);
    }
  } 

  public boolean authenticate(String authToken) throws SQLException {
    boolean isTokenValid = false;
    Connection conn = null;
    try {
      conn = loginDS.getConnection();
      isTokenValid = SQLHandler.validateToken(conn, authToken);
    } catch (SQLException e) {
      logger.error("Exception occurred while authenticating token: " + authToken, e);
      throw e;
    } finally {
      DBUtil.closeConnectionIgnoreException(conn);
    }
    return isTokenValid;
  }
  
  public ApplicationData getApplicationDetails(int appId) throws SQLException{
    Connection conn = null;
    try {
      conn = loginDS.getConnection();
      return SQLHandler.getApplicationDetails(conn, appId);
    } catch (SQLException e) {
      logger.error("Exception occurred while retrieving application details: " + appId, e);
      throw e;
    } finally {
      DBUtil.closeConnectionIgnoreException(conn);
    }    
  }

  private boolean authenticateUser(String username, int appId, String password) throws SQLException {
    Connection conn = null;
    try {
      conn = loginDS.getConnection();
      String dbPassword = SQLHandler.getPassword(conn, username, appId);
      dbPassword = dbPassword == null ? "" : dbPassword;
      if (dbPassword.equals(password)) {
        return true;
      }
    } catch (SQLException e) {
      logger.error("Exception occurred while validating login crendentials for username: " + username, e);
      throw e;
    } finally {
      DBUtil.closeConnectionIgnoreException(conn);
    }
    return false;
  }

  public User getUserDetails(String username) throws SQLException {
    User user = null;
    Connection conn = null;
    try {
      conn = loginDS.getConnection();
      user = SQLHandler.getUserDetails(conn, username);
    } catch (SQLException e) {
      logger.error("Exception occurred while fetching user details", e);
      throw e;
    } finally {
      DBUtil.closeConnectionIgnoreException(conn);
    }
    return user;
  }
  
  private UserSession createSession(int appId, String userName) throws SQLException {
    // 1.Generate random number and use it as sessionId.
    int sessionId = generateSessionId();

    // 2.Create token with appId, userName and sessionId.
    String token = createToken(appId, userName, sessionId);

    try {
   // 3.Persist session details.
      Connection conn = loginDS.getConnection();
      SQLHandler.persistSession(conn, sessionId, token, userName);
      
   // 4.Create UserSession Object and return it.
      return new UserSession(sessionId, token, userName);
    } catch (SQLException e) {
      logger.error("Exception occurred while creating user sesion", e);
      throw e;
    }
    
  }
  
  private int generateSessionId(){
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
  }
  
  private String createToken(int appId, String userName, int sessionId) {
    return appId + ":" + userName + ":" + sessionId;
  }
  
  /*  *//**
   * @param request HttpServletRequest object
   * @param response HttpServletResponse object
   * @param user a bean containing user details.
   * 
   * @return true if session was successfully created otherwise false.
   */
  /*
   * public boolean createSession(HttpServletRequest request, HttpServletResponse response, User
   * user){ String sessionId = null; Connection conn = null; try{ conn = loginDS.getConnection();
   * //Remove old session. HttpSession session = request.getSession(false); if(session != null){
   * session.invalidate(); } //Create new session. session = request.getSession(true);
   * 
   * sessionId = session.getId(); //Creating session token. String accessToken =
   * createAccessToken(sessionId, user);
   * 
   * SQLHandler.persistSession(conn, sessionId, accessToken, user);
   * 
   * //sessionId creation is used here just for creating unique, random //id which will be persisted
   * in DB and will be used to track user session. //So now deleting this session.
   * session.invalidate();
   * 
   * String appURL = SQLHandler.getAppURL(conn, user.getAppId());
   * 
   * //Store access token in response. setAccessTokenInResponse(response, accessToken, appURL);
   * }catch(SQLException e){ logger.error("Exception occurred while creating session", e); return
   * false; } catch (MalformedURLException e) {
   * logger.error("Exception occurred while creating session", e); return false; }finally{
   * DBUtil.closeConnectionIgnoreException(conn); } return true; }
   */

  private String createAccessToken(String sessionId, User user) {
    return sessionId + ":" + user.getAppId() + ":" + user.getProfileId();
  }

  private void setAccessTokenInResponse(HttpServletResponse response, String accessToken, String appURL) throws MalformedURLException {
    Cookie cookie = new Cookie("auth_token", accessToken);
    URL url = new URL(appURL);
    String hostName = url.getHost();
    cookie.setDomain(hostName);
    cookie.setPath("/");
    cookie.setMaxAge(TOKEN_EXPIRY_TIME);
    response.addCookie(cookie);
  }

  public void destroy() {
    Connection conn = null;
    try{
      conn = loginDS.getConnection();
      SQLHandler.destroyAllUserSessions(conn);
    } catch (SQLException e) {
      logger.error("Exception occurred while destroying login manager", e);
    }finally{
      DBUtil.closeConnectionIgnoreException(conn);
    }
  }
  
  public boolean validateAuthParams(int tokenId, String token, String userName){
    Connection conn = null;
    try{
      conn = loginDS.getConnection();
      return SQLHandler.validateAuthParams(conn, tokenId, token, userName);
    } catch (SQLException e) {
      logger.error("Exception occurred while destroying login manager", e);
    }finally{
      DBUtil.closeConnectionIgnoreException(conn);
    }
    return false;
  }

}
