/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.session;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 03-Nov-2014
 */
public class ApplicationData {
  private int appId;
  private String appName;
  private String domainName;
  private int portNumber;
  private String appContext;
  private String publicResource;
  
  public ApplicationData(int appId, String appName, String domainName, int portNumber, String appContext, String publicResource) {
      super();
      this.appId = appId;
      this.appName = appName;
      this.domainName = domainName;
      this.portNumber = portNumber;
      this.appContext = appContext;
      this.publicResource = publicResource;
  }

  public int getAppId() {
      return appId;
  }

  public String getAppName() {
      return appName;
  }

  public String getDomainName() {
      return domainName;
  }

  public int getPortNumber() {
      return portNumber;
  }
  
  public String getAppContext(){
    return appContext;
  }

  public String getPublicResource() {
      return publicResource;
  }
}
