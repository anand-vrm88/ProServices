/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.client.exception;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 27-Nov-2014
 */
public class RedisException extends Exception{
  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  public RedisException() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * 
   */
  public RedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // TODO Auto-generated constructor stub
  }
  
  /**
   * 
   */
  public RedisException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }
  
  /**
   * 
   */
  public RedisException(String message) {
    super(message);
    // TODO Auto-generated constructor stub
  }
  
  /**
   * 
   */
  public RedisException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }
}
