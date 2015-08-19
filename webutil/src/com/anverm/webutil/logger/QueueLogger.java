/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.logger;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.AsyncAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import com.anverm.webutil.queue.exception.QueueException;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 30-Nov-2014
 */
public abstract class QueueLogger {
  private Logger queueLogger;
  private AsyncAppender asyncAppender;
  
  /**
   * 
   */
  public QueueLogger(String loggerName, AppenderSkeleton appender) {
    super();
    asyncAppender = new AsyncAppender();
    init(loggerName, appender);
  }

  private void init(String loggerName, AppenderSkeleton appender){
    queueLogger = Logger.getLogger(loggerName);
    asyncAppender.addAppender(appender);
    queueLogger.addAppender(asyncAppender);
    queueLogger.setAdditivity(false);
  }
  

  protected void log(LoggingEvent loggingEvent){
    queueLogger.info(loggingEvent);
  }
  
  public void destroy() throws QueueException{
    if(queueLogger != null){
      queueLogger.removeAllAppenders();
    }
    if(asyncAppender != null){
      asyncAppender.close();
    }
  }
}
