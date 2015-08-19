/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.property;

import java.util.Map;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 02-Oct-2014
 * Properties.java
 */
public class Properties {
  private Map<String, String> propertyMap;
  
  public Properties(Map<String, String> propertyMap) {
    super();
    this.propertyMap = propertyMap;
  }
  
  public String getProperty(String propertyName){
    return propertyMap.get(propertyName);
  }
  
}
