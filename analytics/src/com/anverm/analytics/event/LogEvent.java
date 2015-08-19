/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.analytics.event;

import org.apache.log4j.spi.LoggingEvent;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 01-Dec-2014
 */
public class LogEvent extends LoggingEvent{
  private static final long serialVersionUID = 1L;
  
  public LogEvent(){
    super(null, null, 0, null, null, null, null, null, null, null);
  }

}
