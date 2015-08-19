/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.model;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 03-Oct-2014
 */
public class SQLQueries {
  public static final String GET_LOGGED_IN_SESSION_DETAILS =
      "select up.FIRST_NAME as FIRST_NAME, up.LAST_NAME as LAST_NAME, up.EMAIL_ID as EMAIL_ID, aa.APP_URL as APP_URL, lis.CREATION_TIME as CREATION_TIME, lis.LAST_ACTIVE_TIME as LAST_ACTIVE_TIME from LOGGED_IN_SESSION lis, USER_PROFILE up, APPLICATION_ACCOUNTS aa where lis.PROFILE_ID = up.PROFILE_ID and lis.APP_ID = aa.APP_ID and lis.SESSION_ID = ? ";
  
  public static final String GET_APP_URL = "select APP_URL from APPLICATION_ACCOUNTS where APP_ID = ? ";
  
  public static final String GET_PASSWORD = "select PASSWORD from USER_ACCOUNTS where USERNAME = ? and APP_ID = ? ";
  
  public static final String GET_USER_DETAILS = "select up.FIRST_NAME as FIRST_NAME, up.LAST_NAME as LAST_NAME, up.EMAIL_ID as EMAIL_ID, ua.APP_ID as APP_ID, ua.GROUP_ID as GROUP_ID, up.PROFILE_ID as PROFILE_ID from USER_ACCOUNTS ua, USER_PROFILE up where ua.USERNAME = up.USERNAME and ua.USERNAME = ? ";
  
  public static final String INSERT_SESSION_DETAILS = "insert into USER_SESSION(SESSION_ID, TOKEN, USERNAME, CREATION_TIME, LAST_ACTIVE_TIME) values(?, ?, ?, ?, ?) ";
  
  public static final String GET_TOKEN = "select TOKEN from USER_SESSION where SESSION_ID = ? ";
  
  public static final String REMOVE_SESSION_DETAILS = "delete from USER_SESSION where SESSION_ID = ? ";
  
  public static final String REMOVE_ALL_SESSION_DETAILS = "delete from USER_SESSION ";
  
  public static final String IS_TOKEN_VALID = "select if(count(SESSION_TOKEN) == 1, true, false) as IS_TOKEN_VALID from LOGGED_IN_SESSION where SESSION_TOKEN = ? ";
  
  public static final String GET_APPLICATION_DETAILS = "select * from APPLICATION_ACCOUNTS where APP_ID = ? ";
  
  public static final String GET_USER_APPLICATIONS_DATA = "select aa.* from APPLICATION_ACCOUNTS aa, USER_APPLICATIONS ua where aa.APP_ID=ua.APP_ID and USERNAME = ? ;";
  
  public static final String GET_USERNAME_AND_TOKEN_FROM_SESSION = "select TOKEN, USERNAME from USER_SESSION where SESSION_ID = ? ;";
  
  public static final String INSERT_APPLICATION_DETAILS = "insert into APPLICATION_ACCOUNTS (APP_NAME, DOMAIN_NAME, PORT_NUMBER, APP_CONTEXT, PUBLIC_RESOURCE) values (?, ?, ?, ?, ?) ";
  
  public static final String INSERT_USER_APP_DETAIL = "insert into USER_APPLICATIONS values (?, ?) ";
  
  public static final String REMOVE_APPLICATION_DETAILS = "delete from APPLICATION_ACCOUNTS where APP_ID = ? ";
  
  public static final String REMOVE_USER_APP_DETAIL = "delete from USER_APPLICATIONS where USERNAME = ? and APP_ID = ? ";
  
  public static final String UPDATE_APPLICATION_DETAILS = "update APPLICATION_ACCOUNTS set APP_NAME = ?, DOMAIN_NAME = ?, PORT_NUMBER = ?, APP_CONTEXT = ?, PUBLIC_RESOURCE = ? where APP_ID = ? ";
  
}
