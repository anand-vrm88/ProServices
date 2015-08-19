/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.remover.api;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import com.anverm.webutil.handler.api.EventHandler;
import com.anverm.webutil.queue.api.QueueReader;
import com.anverm.webutil.queue.exception.QueueException;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 29-Nov-2014
 */
abstract public class QueueRemover extends TimerTask{
  private static final Logger logger = Logger.getLogger(QueueRemover.class);
  private QueueReader queueReader;
  private EventHandler eventHandler;
  
  /**
   * @param queueName TODO
   * 
   */
  public QueueRemover(QueueReader queueReader, EventHandler eventHandler, String queueName, long period) {
    this.queueReader = queueReader;
    this.eventHandler = eventHandler;
    new Timer("QueueRemover").scheduleAtFixedRate(this, 0, period);
  }
  
  /**
   * 
   */
  @Override
  public void run() {
    try {
      Set<String> queueNames = queueReader.getQueueNames("temporary");
      for (String queueName : queueNames) {
        LoggingEvent loggingEvent = null;
        while ((loggingEvent = (LoggingEvent) queueReader.dequeue(queueName)) != null) {
          eventHandler.handleEvent(loggingEvent);
        }
      }
    } catch (QueueException e) {
      logger.error("Exception occerred", e);
    } catch (Throwable e) {
      logger.error("Exception occerred", e);
    }
  }

}
