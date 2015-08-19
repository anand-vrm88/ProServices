/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.analytics.appender;

import com.anverm.webutil.appender.api.QueueAppender;
import com.anverm.webutil.queue.api.QueueWriter;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 29-Nov-2014
 */
public class BasicAnalyticsAppender extends QueueAppender{

  /**
   * 
   */
  public BasicAnalyticsAppender(QueueWriter queueWriter, int corePoolSize, int maximumPoolSize, long keepAliveTime, int eventQueueSize) {
    super(queueWriter, corePoolSize, maximumPoolSize, keepAliveTime, eventQueueSize);
  }

}
