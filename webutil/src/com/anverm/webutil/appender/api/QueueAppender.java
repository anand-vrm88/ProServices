/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.appender.api;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import com.anverm.webutil.queue.api.QueueWriter;
import com.anverm.webutil.queue.exception.QueueException;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 29-Nov-2014
 */
abstract public class QueueAppender extends AppenderSkeleton {
  private static final Logger logger = Logger.getLogger(QueueAppender.class);
  private QueueWriter queueWriter;
  private ThreadPoolExecutor threadPoolExecuter;
  
  /**
   * 
   */
  public QueueAppender(QueueWriter queueWriter, int corePoolSize, int maximumPoolSize, long keepAliveTime, int eventQueueSize) {
    super();
    this.queueWriter = queueWriter;
    this.threadPoolExecuter = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(eventQueueSize));
  }

  /**
   * @exception RejectedExecutionException a runtime exception
   * is thrown when Runnable is full.
   */
  @Override
  protected void append(LoggingEvent logingEvent){
    threadPoolExecuter.submit(new EventAppender(logingEvent));
  }
  
  class EventAppender extends Thread {
    private LoggingEvent logingEvent;
    
    /**
     * 
     */
    public EventAppender(LoggingEvent logingEvent) {
      this.logingEvent = logingEvent;
    }

    /**
     * 
     */
    @Override
    public void run() {
      String queueName = getQueueName();
      try {
        queueWriter.enqueue(queueName, logingEvent);
      } catch (QueueException e) {
        logger.error("Exception occurred while enqueue event: "+logingEvent, e);
      }
    }
  }
  
  protected String getQueueName(){
    //TODO: to be changed in future.
    return "temporary";
  }
  
  /**
   * 
   */
  @Override
  public boolean requiresLayout() {
    return false;
  }
  
  @Override
  public void close(){
    if(threadPoolExecuter != null){
      threadPoolExecuter.shutdown();
    }
  }
}
