/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.anverm.webutil.net.NetworkUtil;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 23-Oct-2014
 */
public class CommonUtil {
  private static final Logger logger = Logger.getLogger(CommonUtil.class);
  
  public static boolean isEmpty(Object data) {
    if (data == null) {
      if(data instanceof String){
        return ((String)data).length() == 0;
      }
      return true;
    } else {
      return false;
    }
  }
  
  public static int generateRandomNumber(int min, int max) {
    int randomNumber = 0;
    try {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "text/plain");
        String response = NetworkUtil.getData("http://www.random.org/integers/?num=1&min=" + min + "&max=" + max
                + "&col=1&base=10&format=plain&rnd=new", null, headers);
        randomNumber = Integer.parseInt(response);
    } catch (Exception e) {
        logger.warn("Failed to get random number from remote server. Falling back to local random number generator", e);
        int range = max - min + 1;
        Random random = new Random();
        randomNumber = min + random.nextInt(range) + 1;
    }
    return randomNumber;
}
}
