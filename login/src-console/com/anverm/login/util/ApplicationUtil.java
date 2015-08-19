/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anverm.webutil.CommonUtil;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 13-Nov-2014
 */
public class ApplicationUtil {

  /**
   * @throws Exception 
   * 
   */
  public static int getApplicationId(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String applicationId = request.getParameter("appId");
    if(CommonUtil.isEmpty(applicationId)){
      throw new Exception("appId was not found.");
    }
  
    int appId = -1;
    try{
      appId = Integer.parseInt(applicationId);
    }catch(NumberFormatException e){
      throw new Exception("AppId is invalid = "+applicationId);
    }
    return appId;
  }

}
