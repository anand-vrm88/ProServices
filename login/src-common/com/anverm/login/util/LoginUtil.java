/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.util;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.anverm.login.util.LoginConstants;
import com.anverm.webutil.CommonUtil;
import com.anverm.webutil.property.Properties;
import com.anverm.webutil.property.PropertyUtil;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 02-Oct-2014
 * AuthUtil.java
 */
public class LoginUtil {
  private static final Logger logger = Logger.getLogger(LoginUtil.class);
  private static Properties properties = PropertyUtil.getProperties(LoginConstants.Properties.PROPERTY_FILE_NAME);
  
  public static String getLoginServiceURL(){
    return getProperty(LoginConstants.Properties.LOGIN_SERVICE_URL);
  }
  
  public static String getDomainName(){
    return getProperty(LoginConstants.Properties.DOMAIN_NAME);
  }
  
  private static String getProperty(String propertyName){
    if(properties == null){
      logger.error("Properties object was not initialized.");
      throw new IllegalStateException("Properties object was not initialized.");
    }
    return properties.getProperty(propertyName);    
  }
  
  public static String getSignInURL(String callbackURL) throws UnsupportedEncodingException{
    String loginServiceURL = getLoginServiceURL();
    int appId = getAppId();
    if(CommonUtil.isEmpty(loginServiceURL) || appId == -1){
      throw new AssertionError("Inconsistent jar exception");
    }
    return loginServiceURL + "/service/sign-in?callback=" + URLEncoder.encode(callbackURL, "utf-8") + "&appId=" + appId; 
  }
  
  public static String getSignOutURL(HttpSession session, String callbackURL) throws UnsupportedEncodingException{
    Object id = session.getAttribute("tokenId");
    String loginServiceURL = getLoginServiceURL();
    if(CommonUtil.isEmpty(loginServiceURL) || id == null){
      throw new AssertionError("Inconsistent jar exception");
    }
    int tokenId = (int)id;
    return loginServiceURL + "/service/SignOut?callback=" + URLEncoder.encode(callbackURL, "utf-8") + "&tokenId=" + tokenId; 
  }
  
  public static int getAppId() {
    int appId = -1;
    if (properties == null) {
      logger.error("Properties object was not initialized.");
      throw new IllegalStateException("Properties object was not initialized.");
    }

    try {
      appId = Integer.parseInt(properties.getProperty(LoginConstants.Properties.APP_ID));
    } catch (NumberFormatException e) {
      logger.warn("App Id is corrupted", e);
    }
    return appId;
  }

  public static boolean authenticate(int tokenId, String token){
    boolean authenticated = false;
    HttpClient httpClient = new HttpClient();
    GetMethod method = new GetMethod(getLoginServiceURL()+"/service/api/authenticate?tokenId="+tokenId+"&token="+token);
    method.setRequestHeader("Accept", "application/json");
    try {
      httpClient.executeMethod(method);
      JSONParser parser = new JSONParser();
      String output = method.getResponseBodyAsString();
      JSONObject result = (JSONObject)parser.parse(output);
      authenticated = (boolean)result.get("authenticated");
    } catch (Exception e) {
      logger.error("Exception occurred while authenticating session", e);
    }
    return authenticated;
  }

  public static boolean validateAuthParams(String token, String tokenId, String userName){
    boolean areAuthParamsValid = false;
    HttpClient httpClient = new HttpClient();
    GetMethod method = new GetMethod(getLoginServiceURL()+"/service/api/validateAuthParams?token="+token+"&tokenId="+tokenId+"&userName="+userName);
    method.setRequestHeader("Accept", "application/json");
    try {
      httpClient.executeMethod(method);
      JSONParser parser = new JSONParser();
      String output = method.getResponseBodyAsString();
      JSONObject result = (JSONObject)parser.parse(output);
      areAuthParamsValid = (boolean)result.get("areAuthParamsValid");
    } catch (Exception e) {
      logger.error("Exception occurred while authenticating session", e);
    }
    return areAuthParamsValid;
  }
  
  public static String getAnonymousResource(){
    if(properties == null){
      logger.error("Properties object was not initialized.");
      throw new IllegalStateException("Properties object was not initialized.");
    }
    return properties.getProperty(LoginConstants.Properties.ANONYMOUS_RESOURCE);
  }

  public static final String Auth_COOKIE_NAME = "auth_token";
  
  public boolean authenticate(String authToken){
    HttpClient httpClient = new HttpClient();
    GetMethod method = new GetMethod(getLoginServiceURL()+"/service?authenticate");
    try {
      httpClient.executeMethod(method);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

  public static String getCallbackURL(HttpServletRequest request){
    Set<String> paramNameSet = new HashSet<String>(Arrays.asList("token","tokenId","userName"));
    String callbackURL = request.getRequestURL().toString();
    String queryString = request.getQueryString();
    if(CommonUtil.isEmpty(queryString)){
      return callbackURL;
    }
    String[] queryParams = queryString.split("&");
    StringBuilder sb = new StringBuilder();
    for(String queryParam : queryParams){
      if(!paramNameSet.contains(queryParam.split("=")[0])){
        sb.append(queryParam+"&");
      }
    }
    
    int lastIndex = sb.lastIndexOf("&");
    if(lastIndex != - 1){
      sb.deleteCharAt(lastIndex);
    }
    
    return sb.toString().length() == 0 ? callbackURL : callbackURL+"?"+sb.toString();
  }

  /**
   * @param authToken colon separated appId and unique Id generated by login
   * server.
   * 
   * @return a non-negative integer if authToken is not corrupted otherwise returns -1.
   */
  public static int getAppId(String authToken) {
    int appId = -1;
    if (authToken == null || "".equals(authToken)) {
      return -1;
    }
  
    String[] tokenIds = authToken.split(":");
    if (tokenIds.length != 2) {
      return -1;
    }
  
    try {
      appId = Integer.parseInt(tokenIds[0]);
    } catch (NumberFormatException e) {
      LoginUtil.logger.error("Could not parse appId: " + tokenIds[0] + " . Falling back to -1 as appId", e);
    }
    return appId;
  }

  public static int getTokenId(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if(session == null){
      return -1;
    }
/*    String tokenIdStr = request.getParameter("tokenId");
    if(CommonUtil.isEmpty(tokenIdStr)){*/
      Object tokenIdObj = session.getAttribute("tokenId");
      if(CommonUtil.isEmpty(tokenIdObj)){
        return -1;
      } 
      return (int)tokenIdObj;
    //} else{
      //return Integer.parseInt(tokenIdStr);
    }
  
  public static String filterOutParams(String url, Set<String> filterParams){
    int index = url.indexOf("?");
    StringBuilder uri = new StringBuilder();
    uri.append(index == -1 ? url : url.substring(0, index-1));
    String queryString = (index == -1 ? "" : url.substring(index+1,url.length()-1));
    if(CommonUtil.isEmpty(queryString)){
      return uri.toString();
    }
    String[] queryParams = queryString.split("&");
    StringBuilder sb = new StringBuilder();
    for(String queryParam : queryParams){
      if(!filterParams.contains(queryParam.split("=")[0])){
        sb.append(queryParam+"&");
      }
    }
    sb.deleteCharAt(sb.lastIndexOf("&"));
    return uri.append("?"+sb.toString()).toString();
  }
  
  public static String getUserName(HttpServletRequest request){
    HttpSession session = request.getSession(false);
    if(session == null){
      return null;
    }
    
    Object userName = session.getAttribute("userName");
    if(CommonUtil.isEmpty(userName)){
      return null;
    }
    
    return (String)userName;
  }
}
