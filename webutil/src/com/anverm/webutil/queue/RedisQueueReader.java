/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.queue;

import java.util.Set;

import com.anverm.webutil.client.RedisClient;
import com.anverm.webutil.client.exception.RedisException;
import com.anverm.webutil.queue.api.QueueReader;
import com.anverm.webutil.queue.exception.QueueException;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 27-Nov-2014
 */
public class RedisQueueReader extends RedisClient implements QueueReader {
  /**
   * 
   */
  public RedisQueueReader(String host, int port, int timeout, String password, RedisDB redisDB) {
    super(host, port, timeout, password, redisDB);
  }


  /**
   * 
   */
  @Override
  public Object dequeue(String name) throws QueueException {
    try {
      return lpop(name);
    } catch (RedisException e) {
      throw new QueueException(e);
    }
  }

  /**
   * 
   */
  @Override
  public Object blockingDequeue(String name) throws QueueException {
    try {
      return blpop(name);
    } catch (RedisException e) {
      throw new QueueException(e);
    }
  }
  
  /**
   * 
   */
  @Override
  public void close() throws QueueException {
    super.destroy();
  }
  
  @Override
  public Set<String> getQueueNames(String pattern) throws QueueException{
    try {
      return getKeys(pattern);
    } catch (RedisException e) {
      throw new QueueException(e);
    }
  }


  
  /**
   * 
   */
  @Override
  public Object blockingDequeue(String name, int timeout) throws QueueException {
    try {
      return blpop(name, timeout);
    } catch (RedisException e) {
      throw new QueueException(e);
    }
  }

}
