/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.analytics.event;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 01-Dec-2014
 */
public class PageVisitEvent extends LogEvent{
  private static final long serialVersionUID = 1L;
  
  private int visitId;
  private int visitCorrelId;
  private long endUserId;
  private String userAgent;
  private String referer;
  private long logTime;
  
  /**
   * 
   */
  public PageVisitEvent(int visitCorrelId, long endUserId,
      String userAgent, String referer, long logTime) {
    this.visitCorrelId = visitCorrelId;
    this.endUserId = endUserId;
    this.userAgent = userAgent;
    this.referer = referer;
    this.logTime = logTime;
  }

  public void setVisitId(int visitId){
    this.visitId = visitId;
  }
  
  public int getVisitId() {
    return visitId;
  }

  public int getVisitCorrelId() {
    return visitCorrelId;
  }

  public long getEndUserId() {
    return endUserId;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public String getReferer() {
    return referer;
  }

  public long getLogTime() {
    return logTime;
  }
}