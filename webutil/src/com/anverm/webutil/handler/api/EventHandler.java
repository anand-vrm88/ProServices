/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.handler.api;

import org.apache.log4j.spi.LoggingEvent;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 29-Nov-2014
 */
public interface EventHandler {
  public void handleEvent(LoggingEvent logingEvent) throws Throwable;
  public void destroy() throws Throwable;
}
