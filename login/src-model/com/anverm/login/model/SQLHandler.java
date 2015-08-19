/*******************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * There is no copyright registered for following awesome work.
 * Whatever contributed here is not private but do no copy. Instead
 * collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.anverm.login.session.ApplicationData;
import com.anverm.login.session.UserSession;
import com.anverm.login.session.User;
import com.anverm.login.util.LoginConstants;
import com.anverm.webutil.CommonUtil;
import com.anverm.webutil.db.DBUtil;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 03-Oct-2014
 */
public class SQLHandler {
  private static final Logger logger = Logger.getLogger(SQLHandler.class);
  
  
  /**
   * @param conn an SQL connection to get data from.
   * @param sessionId id associated with currently logged-in user.
   * 
   * @return session details like logged-in user, application URL, 
   * session creation and last active time
   * 
   * @exception throws SQLException if something goes wrong while
   * fetching data from DB.
   */
  public static UserSession getLoggedInSession(Connection conn, String sessionId) throws SQLException {
    UserSession loggedInSession = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(SQLQueries.GET_LOGGED_IN_SESSION_DETAILS);
      ps.setString(1, sessionId);
      rs = ps.executeQuery();
      if (rs.next()) {
        String firstName = rs.getString(LoginConstants.QueryResults.FIRST_NAME);
        String lastName = rs.getString(LoginConstants.QueryResults.LAST_NAME);
        String emailId = rs.getString(LoginConstants.QueryResults.EMAIL_ID);
        String appURL = rs.getString(LoginConstants.QueryResults.APP_URL);
        long creationTime = rs.getTimestamp(LoginConstants.QueryResults.CREATION_TIME).getTime();
        long lastActiveTime = rs.getTimestamp(LoginConstants.QueryResults.LAST_ACTIVE_TIME).getTime();
        //loggedInSession = new UserSession(sessionId, appURL, new User(null, firstName, lastName, emailId, 0, 0, 0), creationTime, lastActiveTime);
      }
    } catch (SQLException e) {
      logger.error("Exception occured while fetching logged-in session details for sessionId: " + sessionId, e);
      throw e;
    } finally {
      DBUtil.closeResultSetIgnoreException(rs);
      DBUtil.closeStatementIgnoreException(ps);
    }
    return loggedInSession;
  }
  
  
  /**
   * @param conn an SQL connection to get data from.
   * @param appId an Id to get relevant data in DB.
   * 
   * @return a String type Application URL.
   * 
   * @throws SQLException when there is any exception while getting password from DB.
   */
  public static String getAppURL(Connection conn, int appId) throws SQLException {
    String appURL = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(SQLQueries.GET_APP_URL);
      ps.setInt(1, appId);
      rs = ps.executeQuery();
      if (rs.next()) {
        appURL = rs.getString(LoginConstants.QueryResults.APP_URL);
      }
    } finally {
      DBUtil.closeResultSetIgnoreException(rs);
      DBUtil.closeStatementIgnoreException(ps);
    }
    return appURL;
  }
  
  
  /**
   * @param conn an SQL connection to get data from.
   * @param username a String type name to get relevant data in DB. 
   * @param appId TODO
   * @throws SQLException when there is any exception while getting password from DB. 
   */
  public static String getPassword(Connection conn, String username, int appId) throws SQLException {
    String password = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(SQLQueries.GET_PASSWORD);
      ps.setString(1, username);
      ps.setInt(2, appId);
      rs = ps.executeQuery();
      if (rs.next()) {
        password = rs.getString(LoginConstants.QueryResults.PASSWORD);
      }
    } finally {
      DBUtil.closeResultSetIgnoreException(rs);
      DBUtil.closeStatementIgnoreException(ps);
    }
    return password;
  }
  
  public static void destroyAllUserSessions(Connection conn) throws SQLException{
    PreparedStatement ps = null;
    try{
      ps = conn.prepareStatement(SQLQueries.REMOVE_ALL_SESSION_DETAILS);
      ps.executeUpdate();
    } catch (SQLException e) {
      logger.error("Exception occurred while removing all session details", e);
      throw e;
    }finally{
      DBUtil.closeStatementIgnoreException(ps);
    }
  }
  
  /**
   * @param conn an SQL connection to get data from.
   * @param username it is String type name to get user details of. 
   * 
   * @throws SQLException when there is any exception while getting password from DB.
   */
  public static User getUserDetails(Connection conn, String username) throws SQLException{
    User user = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(SQLQueries.GET_USER_DETAILS);
      ps.setString(1, username);
      rs = ps.executeQuery();
      if(rs.next()){
        String firstName = rs.getString(LoginConstants.QueryResults.FIRST_NAME);
        String lastName = rs.getString(LoginConstants.QueryResults.LAST_NAME);
        String emailId = rs.getString(LoginConstants.QueryResults.EMAIL_ID);
        int appId = rs.getInt(LoginConstants.QueryResults.APP_ID);
        int groupId = rs.getInt(LoginConstants.QueryResults.GROUP_ID);
        int profileId = rs.getInt(LoginConstants.QueryResults.PROFILE_ID);
        user = new User(username, firstName, lastName, emailId, appId, groupId, profileId);
      }
    }finally{
      DBUtil.closeResultSetIgnoreException(rs);
      DBUtil.closeStatementIgnoreException(ps);
    }
    return user;
  }
  
  
  /**
   * @param conn an SQL connection to get data from.
   * @param sessionId a unique id to distinguish user sessions.
   * @param accessToken TODO
   * @param user a bean having details about user.
   * @return true if session was persisted successfully otherwise returns false.
   * 
   * @throws SQLException when there is any exception while getting password from DB.
   */
  public static boolean persistSession(Connection conn, String sessionId, String accessToken, User user) throws SQLException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(SQLQueries.INSERT_SESSION_DETAILS);
      ps.setString(1, sessionId);
      ps.setInt(2, user.getProfileId());
      ps.setInt(3, user.getAppId());
      ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
      ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
      ps.executeUpdate();
    } catch (SQLException e) {
      logger.error("Exception occurred while persisting session details ", e);
      return false;
    } finally {
      DBUtil.closeResultSetIgnoreException(rs);
      DBUtil.closeStatementIgnoreException(ps);
    }
    return true;
  }

  public static void persistSession(Connection conn, int sessionId, String token, String userName) throws SQLException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(SQLQueries.INSERT_SESSION_DETAILS);
      ps.setInt(1, sessionId);
      ps.setString(2, token);
      ps.setString(3, userName);
      ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
      ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
      ps.executeUpdate();
    } catch (SQLException e) {
      logger.error("Exception occurred while persisting session details ", e);
      throw e;
    } finally {
      DBUtil.closeResultSetIgnoreException(rs);
      DBUtil.closeStatementIgnoreException(ps);
    }
  }
  
  public static String getToken(Connection conn, int sessionId) throws SQLException {
    String token = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(SQLQueries.GET_TOKEN);
      ps.setInt(1, sessionId);
      rs = ps.executeQuery();
      if(rs.next()){
        token = rs.getString("TOKEN");
      }
    } catch (SQLException e) {
      logger.error("Exception occurred while persisting session details ", e);
    } finally {
      DBUtil.closeResultSetIgnoreException(rs);
      DBUtil.closeStatementIgnoreException(ps);
    }
    return token;
  }
  
  public static boolean removeSession(Connection conn, int sessionId) throws SQLException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(SQLQueries.REMOVE_SESSION_DETAILS);
      ps.setInt(1, sessionId);
      ps.executeUpdate();
    } catch (SQLException e) {
      logger.error("Exception occurred while persisting session details ", e);
      return false;
    } finally {
      DBUtil.closeResultSetIgnoreException(rs);
      DBUtil.closeStatementIgnoreException(ps);
    }
    return true;
  }
  
  public static boolean validateToken(Connection conn, String authToken) throws SQLException{
    boolean isTokenValid = false;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(SQLQueries.IS_TOKEN_VALID);
      ps.setString(1, authToken);
      rs = ps.executeQuery();
      if(rs.next()){
        isTokenValid = rs.getBoolean("IS_TOKEN_VALID");
      }
    }catch(SQLException e){
      logger.error("Exception occurred while validating token", e);
      throw e;
    }
    return isTokenValid;
  }
  
  public static List<ApplicationData> getUserApplicationsList(Connection conn, String userName) throws SQLException{
    List<ApplicationData> applicationData = new LinkedList<ApplicationData>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(SQLQueries.GET_USER_APPLICATIONS_DATA);
      ps.setString(1, userName);
      rs = ps.executeQuery();
      while(rs.next()){
        int appId = rs.getInt("APP_ID");
        String appName = rs.getString("APP_NAME");
        String domainName = rs.getString("DOMAIN_NAME");
        int portNumber = rs.getInt("PORT_NUMBER");
        String appContext = rs.getString("APP_CONTEXT");
        String publicResource = rs.getString("PUBLIC_RESOURCE");
        applicationData.add(new ApplicationData(appId, appName, domainName, portNumber, appContext, publicResource));
      }
      return applicationData;
    }catch(SQLException e){
      logger.error("Exception occurred while getting applications details list", e);
      throw e;
    }        
  }
  
  public static ApplicationData getApplicationDetails(Connection conn, int appId) throws SQLException{
    ApplicationData applicationData = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(SQLQueries.GET_APPLICATION_DETAILS);
      ps.setInt(1, appId);
      rs = ps.executeQuery();
      if(rs.next()){
        String appName = rs.getString("APP_NAME");
        String domainName = rs.getString("DOMAIN_NAME");
        int portNumber = rs.getInt("PORT_NUMBER");
        String appContext = rs.getString("APP_CONTEXT");
        String publicResource = rs.getString("PUBLIC_RESOURCE");
        applicationData = new ApplicationData(appId, appName, domainName, portNumber, appContext, publicResource);
      }
      return applicationData;
    } finally{
      DBUtil.closeResultSetIgnoreException(rs);
      DBUtil.closeStatementIgnoreException(ps);
    }
  }
  
  public static boolean validateAuthParams(Connection conn, int tokenId, String token, String userName) throws SQLException{
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(SQLQueries.GET_USERNAME_AND_TOKEN_FROM_SESSION);
      ps.setInt(1, tokenId);
      rs = ps.executeQuery();
      if(rs.next()){
        String userNameFromDB = rs.getString("USERNAME");
        String tokenFromDB = rs.getString("TOKEN");
        if(!CommonUtil.isEmpty(userNameFromDB) && !CommonUtil.isEmpty(tokenFromDB) && userNameFromDB.equals(userName) && tokenFromDB.equals(token)){
          return true;
        }
      }
      return false;
    }catch(SQLException e){
      logger.error("Exception occurred while validating token", e);
      throw e;
    }
  }
  
  public static int insertApplicationDetails(Connection conn, String appName, String hostName, int portNumber, String appContext, String publicResource) throws SQLException{
    int appId = -1;
    int i = 1;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(SQLQueries.INSERT_APPLICATION_DETAILS, Statement.RETURN_GENERATED_KEYS);
      ps.setString(i++, appName);
      ps.setString(i++, hostName);
      ps.setInt(i++, portNumber);
      ps.setString(i++, appContext);
      ps.setString(i++, publicResource);
      ps.executeUpdate();
      rs = ps.getGeneratedKeys();
      if(rs.next()){
        appId = rs.getInt(1);
      }
      return appId;
    }finally{
      DBUtil.closeResultSetIgnoreException(rs);
      DBUtil.closeStatementIgnoreException(ps);
    }
  }
  
  public static void insertUserAppDetail(Connection conn, String userName, int appId) throws SQLException{
    int i = 1;
    PreparedStatement ps = null;
    try{
      ps = conn.prepareStatement(SQLQueries.INSERT_USER_APP_DETAIL);
      ps.setString(i++, userName);
      ps.setInt(i++, appId);
      ps.executeUpdate();
    }finally{
      DBUtil.closeStatementIgnoreException(ps);
    }    
  }
  
  public static void removeApplicationDetails(Connection conn, int appId) throws SQLException{
    int i = 1;
    PreparedStatement ps = null;
    try{
      ps = conn.prepareStatement(SQLQueries.REMOVE_APPLICATION_DETAILS);
      ps.setInt(i++, appId);
      ps.executeUpdate();
    }finally{
      DBUtil.closeStatementIgnoreException(ps);
    }
  }
  
  public static void removeUserAppDetail(Connection conn, String userName, int appId) throws SQLException{
    int i = 1;
    PreparedStatement ps = null;
    try{
      ps = conn.prepareStatement(SQLQueries.REMOVE_USER_APP_DETAIL);
      ps.setString(i++, userName);
      ps.setInt(i++, appId);
      ps.executeUpdate();
    }finally{
      DBUtil.closeStatementIgnoreException(ps);
    }    
  }
  
  public static void updateApplicationDetails(Connection conn, int appId, String appName, String hostName, int portNumber, String appContext, String publicResource) throws SQLException{
    int i = 1;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(SQLQueries.UPDATE_APPLICATION_DETAILS);
      ps.setString(i++, appName);
      ps.setString(i++, hostName);
      ps.setInt(i++, portNumber);
      ps.setString(i++, appContext);
      ps.setString(i++, publicResource);
      ps.setInt(i++, appId);
      ps.executeUpdate();
    }finally{
      DBUtil.closeResultSetIgnoreException(rs);
      DBUtil.closeStatementIgnoreException(ps);
    }
  }
}
