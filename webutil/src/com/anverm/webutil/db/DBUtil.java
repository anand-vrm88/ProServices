/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 04-Oct-2014
 */
public class DBUtil {
  private static final Logger logger = Logger.getLogger(DBUtil.class);

  /**
   * Closes connection silently and logs details if any unwanted thing occurs.
   * 
   * @param conn an SQL Connection to close.
   */
  public static void closeConnectionIgnoreException(Connection conn){
    try {
      if (conn != null) {
        conn.close();
      } else {
        logger.warn("Not closing the Connection because it was not initialized.");
      }
    } catch (Throwable t) {
      logger.error("Exception occurred while closing the "+conn.getClass().getName(), t);
    }
  }

  /**
   * Closes statement silently and logs details if any unwanted thing occurs.
   * @param statement an SQL Statement to close.
   */
  public static void closeStatementIgnoreException(Statement statement){
    try {
      if (statement != null) {
        statement.close();
      } else {
        logger.warn("Not closing the statement because it was not initialized.");
      }
    } catch (Throwable t) {
      logger.error("Exception occurred while closing the "+statement.getClass().getName(), t);
    }    
  }
  
  
  /**
   * Closes resultSet silently and logs details if any unwanted thing occurs.
   * @param resultSet an SQL ResultSet to close.
   */
  public static void closeResultSetIgnoreException(ResultSet resultSet){
    try {
      if (resultSet != null) {
        resultSet.close();
      } else {
        logger.warn("Not closing the ResultSet because it was not initialized.");
      }
    } catch (Throwable t) {
      logger.error("Exception occurred while closing the "+resultSet.getClass().getName(), t);
    }        
  }
  
}
