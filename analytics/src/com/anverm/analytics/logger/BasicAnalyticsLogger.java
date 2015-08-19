/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.analytics.logger;

import org.apache.log4j.AppenderSkeleton;

import com.anverm.analytics.appender.BasicAnalyticsAppender;
import com.anverm.analytics.event.LogEvent;
import com.anverm.analytics.event.PageVisitEvent;
import com.anverm.webutil.client.RedisClient.RedisDB;
import com.anverm.webutil.logger.QueueLogger;
import com.anverm.webutil.queue.RedisQueueWriter;
import com.anverm.webutil.queue.api.QueueWriter;
import com.anverm.webutil.queue.exception.QueueException;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 29-Nov-2014
 */
public class BasicAnalyticsLogger extends QueueLogger{
  private static QueueWriter queueWriter;
  private static AppenderSkeleton appender;
  private static BasicAnalyticsLogger basicAnalyticsLogger;
  private static final String BASIC_ANALYTICS_LOGGER = "BasicAnalyticsLogger";
  
  /**
   * 
   */
  private BasicAnalyticsLogger() {
    super(BASIC_ANALYTICS_LOGGER, appender = new BasicAnalyticsAppender(queueWriter = new RedisQueueWriter("localhost", 6379, 5000, "@nv$rMg@t$w@y", RedisDB.ANALYTICS), 1, 5, 5000, 1024));
    
  }
  
  public static BasicAnalyticsLogger init(){
    if(basicAnalyticsLogger != null){
      throw new IllegalStateException("BasicAnalyticsLogger is already initialized.");
    }
    basicAnalyticsLogger = new BasicAnalyticsLogger();
    return basicAnalyticsLogger;
  }
  
  public static BasicAnalyticsLogger getInstance(){
    if(basicAnalyticsLogger == null){
      throw new IllegalStateException("BasicAnalyticsLogger was not initialized.");
    }
    return basicAnalyticsLogger;
  }

  public void logPageVisitAnalytics(int visitCorrelId, long endUserId, String userAgent, String referer, long logTime){
    LogEvent logEvent = new PageVisitEvent(visitCorrelId, endUserId, userAgent, referer, logTime);
    log(logEvent);
  }
  
  @Override
  public void destroy() throws QueueException{
    super.destroy();
    if(queueWriter != null){
      queueWriter.close();
    }
    if(appender != null){
      appender.close();
    }
  }
}
