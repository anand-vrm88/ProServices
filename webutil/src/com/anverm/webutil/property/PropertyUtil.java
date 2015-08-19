/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.property;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.anverm.webutil.io.IOUtil;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 02-Oct-2014
 */
public class PropertyUtil {
  private static final Logger logger = Logger.getLogger(PropertyUtil.class);
  
  /**
   * @param fileName Name of the property file which contains
   * properties.
   * 
   * @return Returns Properties object containing properties defined
   * in fileName. If file does not exists, it returns null.
   */
  public static Properties getProperties(String fileName){
    Properties properties = null;
    ClassLoader classLoader = PropertyUtil.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(fileName);
    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
    Map<String, String> map = new HashMap<String, String>();
    String line = null;

    try {
      while ((line = bufferReader.readLine()) != null) {
        line = line.trim();
        if (!"".equals(line)) {
          String[] nameValue = line.split("=");
          if (nameValue.length == 2) {
            map.put(nameValue[0], nameValue[1]);
          } else {
            logger.error("Illegal property format specefied [ " + line + " ] is property file: " + fileName);
          }
        }
      }
      properties = new Properties(map);
    } catch (IOException t) {
      logger.error("Exception occurred while reading property file: " + fileName + ". Returning null", t);
      return null;
    } finally {
      IOUtil.closeInputStreamIgnoreException(inputStream);
    }

    return properties;
  }
}
