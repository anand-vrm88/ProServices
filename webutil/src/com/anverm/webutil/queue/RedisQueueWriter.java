/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.queue;

import com.anverm.webutil.client.RedisClient;
import com.anverm.webutil.client.exception.RedisException;
import com.anverm.webutil.queue.api.QueueWriter;
import com.anverm.webutil.queue.exception.QueueException;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 27-Nov-2014
 */
public class RedisQueueWriter extends RedisClient implements QueueWriter{
  /**
   * 
   */
  public RedisQueueWriter(String host, int port, int timeout, String password, RedisDB redisDB) {
    super(host, port, timeout, password, redisDB);
  }

  /**
   * 
   */
  @Override
  public void enqueue(String name, Object obj) throws QueueException {
    try {
      rpush(name, obj);
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

}
