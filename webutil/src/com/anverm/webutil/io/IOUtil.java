/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.io;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 02-Oct-2014
 * IOUtil.java
 */
public class IOUtil {
  private static final Logger logger = Logger.getLogger(IOUtil.class);
  
  /**
   * Closes input stream silently and logs details if any unwanted thing occurs.
   * 
   * @param inputStream an IO input stream to close.
   */
  public static void closeInputStreamIgnoreException(InputStream inputStream) {
    try {
      if (inputStream != null) {
        inputStream.close();
      } else {
        logger.warn("Not closing the input stream because it was not initialized.");
      }
    } catch (Throwable t) {
      logger.error("Exception occurred while closing the "+inputStream.getClass().getName(), t);
    }
  }
  
  /**
   * Closes output stream silently and logs details if any unwanted thing occurs.
   * 
   * @param outputStream an IO output stream to close.
   */
  public static void closeOutputStreamIgnoreException(OutputStream outputStream) {
    try {
      if (outputStream != null) {
        outputStream.close();
      } else {
        logger.warn("Not closing the output stream because it was not initialized.");
      }
    } catch (Throwable t) {
      logger.error("Exception occurred while closing the "+outputStream.getClass().getName(), t);
    }
  }
}
