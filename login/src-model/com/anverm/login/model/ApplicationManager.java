/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.anverm.login.session.ApplicationData;
import com.anverm.webutil.db.DBUtil;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 11-Nov-2014
 */
public class ApplicationManager {
  private static final Logger logger = Logger.getLogger(ApplicationManager.class);
  private DataSource loginDS;
  private static ApplicationManager applicationManager;

  private ApplicationManager(DataSource loginDS) throws ServletException {
    this.loginDS = loginDS;
  }

  public static ApplicationManager init(DataSource loginDS) throws ServletException {
    applicationManager = new ApplicationManager(loginDS);
    return applicationManager;
  }

  public static ApplicationManager getInstance() {
    if (applicationManager == null) {
      throw new IllegalStateException("ApplicationManager was not initialized.");
    }
    return applicationManager;
  }
  
  public void registerApplication(String userName, String appName, String hostName, int portNumber, String appContext, String publicResource)
      throws SQLException {
    Connection conn = null;
    boolean defaultAutoCommit = true;
    try {
      conn = loginDS.getConnection();
      defaultAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);
      int appId = SQLHandler.insertApplicationDetails(conn, appName, hostName, portNumber, appContext, publicResource);
      SQLHandler.insertUserAppDetail(conn, userName, appId);
      conn.commit();
    } catch (SQLException e) {
      conn.rollback();
    } finally {
      conn.setAutoCommit(defaultAutoCommit);
      DBUtil.closeConnectionIgnoreException(conn);
    }
  }

  public void unRegisterApplication(String userName, int appId)
      throws SQLException {
    Connection conn = null;
    boolean defaultAutoCommit = true;
    try {
      conn = loginDS.getConnection();
      defaultAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);
      SQLHandler.removeApplicationDetails(conn, appId);
      SQLHandler.removeUserAppDetail(conn, userName, appId);
      conn.commit();
    } catch (SQLException e) {
      conn.rollback();
      throw e;
    } finally {
      conn.setAutoCommit(defaultAutoCommit);
      DBUtil.closeConnectionIgnoreException(conn);
    }
  }
  
  public List<ApplicationData> getUserApplicationsList(String userName) throws SQLException{
    Connection conn = null;
    try {
      conn = loginDS.getConnection();
      return SQLHandler.getUserApplicationsList(conn, userName);
    } catch (SQLException e) {
      logger.error("Exception occurred while fetching user details", e);
      throw e;
    } finally {
      DBUtil.closeConnectionIgnoreException(conn);
    }
  }

  public ApplicationData getApplicationDetails(int appId) throws SQLException{
    Connection conn = null;
    try {
      conn = loginDS.getConnection();
      return SQLHandler.getApplicationDetails(conn, appId);
    } finally {
      DBUtil.closeConnectionIgnoreException(conn);
    }
  }

  public void updateApplicationDetails(int appId, String appName, String hostName, int portNumber, String appContext, String publicResource) throws SQLException{
    Connection conn = null;
    try {
      conn = loginDS.getConnection();
      SQLHandler.updateApplicationDetails(conn, appId, appName, hostName, portNumber, appContext, publicResource);
    } finally {
      DBUtil.closeConnectionIgnoreException(conn);
    }
  }
  
  public void destroy(){
    
  }
}
