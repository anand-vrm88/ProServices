/*******************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * There is no copyright registered for following awesome work.
 * Whatever contributed here is not private but do no copy. Instead
 * collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.session;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 03-Oct-2014
 */
public class User {
  private String username;
  private String firstName;
  private String lastName;
  private String emailId;
  private int appId;
  private int groupId;
  private int profileId;
  
  public User(String username, String firstName, String lastName, String emailId, int appId, int groupId, int profileId) {
    super();
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailId = emailId;
    this.appId = appId;
    this.groupId = groupId;
    this.profileId = profileId;
  }
  
  public String getUsername() {
    return username;
  }

  public String getFirstName() {
    return firstName;
  }
  
  public String getLastName() {
    return lastName;
  }
  
  public String getEmailId() {
    return emailId;
  }

  public int getAppId() {
    return appId;
  }

  public int getGroupId() {
    return groupId;
  }

  public int getProfileId() {
    return profileId;
  }
  
}
