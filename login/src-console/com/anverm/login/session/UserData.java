/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.session;

import java.util.List;


/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 03-Nov-2014
 */
public class UserData {
  private String userName;
  private List<ApplicationData> applicationDataHolder;
  
  public UserData(String userName, List<ApplicationData> applicationDataHolder) {
      super();
      this.userName = userName;
      this.applicationDataHolder = applicationDataHolder;
  }

  public String getUserName() {
      return userName;
  }

  public List<ApplicationData> getApplicationDataHolder() {
      return applicationDataHolder;
  }
}
