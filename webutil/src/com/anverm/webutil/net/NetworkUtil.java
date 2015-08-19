/*******************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER. There is no copyright registered for
 * following awesome work. Whatever contributed here is not private but do no copy. Instead
 * collaborate and spread knowledge.
 *******************************************************************************/
package com.anverm.webutil.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

/**
 * @author anand <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$ Created on: 03-Oct-2014
 */
public class NetworkUtil {
  private static final Logger logger = Logger.getLogger(NetworkUtil.class);


  /**
   * @param url a String type url.
   * 
   * @return boolean type which is true if url is not corrupted otherwise it returns false.
   */
  public static boolean isURLValid(String url) {
    try {
      new URL(url);
    } catch (MalformedURLException e) {
      logger.error("Exception occurred while validating url: " + url, e);
      return false;
    }
    return true;
  }

  public static String getData(String serviceURL, Map<String, String[]> requestParameters, Map<String, String> requestHeaders)
      throws Exception {
    HttpMethod method = new GetMethod(serviceURL);
    HttpClient client = new HttpClient();

    // set request headers
    if (requestHeaders != null && requestHeaders.size() > 0) {
      for (Map.Entry<String, String> entry : requestHeaders.entrySet())
        method.setRequestHeader(entry.getKey(), entry.getValue());
    }

    // set request parameters
    if (requestParameters != null && requestParameters.size() > 0) {
      method.setQueryString(getNameValuePairs(requestParameters));
    }

    try {
      int statusCode = client.executeMethod(method);

      if (statusCode != HttpStatus.SC_OK) {
        throw new IOException("Non OK Status code retured : " + statusCode);
      } else {
        return new String(method.getResponseBody(), "UTF-8");
      }
    } finally {
      method.releaseConnection();
    }
  }

  public static String postData(String serviceURL, Map<String, String[]> postParameters, Map<String, String> requestHeaders)
      throws Exception {
    HttpMethod method = new PostMethod(serviceURL);
    HttpClient client = new HttpClient();

    // set request headers
    if (requestHeaders != null && requestHeaders.size() > 0) {
      for (Map.Entry<String, String> entry : requestHeaders.entrySet())
        method.setRequestHeader(entry.getKey(), entry.getValue());
    }

    // set request parameters
    if (postParameters != null && postParameters.size() > 0) {
      method.setQueryString(getNameValuePairs(postParameters));
    }

    try {
      int statusCode = client.executeMethod(method);

      if (statusCode != HttpStatus.SC_OK) {
        throw new IOException("Non OK Status code retured : " + statusCode);
      } else {
        return new String(method.getResponseBody(), "UTF-8");
      }
    } finally {
      method.releaseConnection();
    }
  }

  private static NameValuePair[] getNameValuePairs(Map<String, String[]> requestParameters) {
    NameValuePair[] nameValuePairs = new NameValuePair[requestParameters.size()];
    int i = 0;
    for (Map.Entry<String, String[]> entry : requestParameters.entrySet()) {
      nameValuePairs[i++] = new NameValuePair(entry.getKey(), entry.getValue()[0]);
    }
    return nameValuePairs;

  }

  public static void setInternalServerErrorResponse(HttpServletResponse res) {
    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

  public static void setForbiddenResponse(HttpServletResponse res) {
    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
  }

  public static void setNotFoundResponse(HttpServletResponse res) {
    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }

  public static Map<String, String[]> getQueryStringAsParametersMap(String parameters) {
    Map<String, String[]> parametersMap = new HashMap<String, String[]>();
    String[] nameValuePairs = parameters.split("[" + "&" + "]");
    for (String nameValuePair : nameValuePairs) {
      String[] nameAndValue = nameValuePair.split(Character.toString('='));
      if (nameAndValue.length == 2) {
        parametersMap.put(nameAndValue[0], new String[] {nameAndValue[1]});
      }
    }
    return parametersMap;
  }

  public static void addBasicAuthHeader(Map<String, String> headers, String username, String password) {
    headers.put("Authorization", "Basic" + " " + Base64.encodeBase64((username + ":" + password).getBytes()));
  }

  public static String getAuthorizationHeader(HttpServletRequest req) {
    return req.getHeader("Authorization");
  }

  public static void setAuthenticationRequiredResponse(HttpServletResponse res) {
    res.addHeader("WWW-Authenticate", "Basic" + " " + "realm" + "=" + "some realm");
    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
