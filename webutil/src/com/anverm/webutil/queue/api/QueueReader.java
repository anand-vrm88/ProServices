/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.queue.api;

import java.util.Set;

import com.anverm.webutil.queue.exception.QueueException;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 27-Nov-2014
 */
public interface QueueReader {
  public Object dequeue(String name) throws QueueException;
  public Object blockingDequeue(String name) throws QueueException;
  public Object blockingDequeue(String name, int timeout) throws QueueException;
  public Set<String> getQueueNames(String pattern) throws QueueException;
  public void close() throws QueueException;
}
