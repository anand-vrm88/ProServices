/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.login.service.api;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.anverm.login.model.LoginManager;
import com.sun.jersey.json.impl.provider.entity.JSONObjectProvider;

/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 02-Oct-2014
 * LoginService.java
 */
@Path("/")
public class LoginService {
  private static final Logger logger = Logger.getLogger(LoginService.class);
  
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/sign-in")
  public Response signIn(@FormParam("username") String username, @FormParam("password") String password){
    return Response.ok().entity("").build();
  }
  
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/sign-out")
  public Response singOut(@FormParam("username") String username, @FormParam("password") String password){
    return Response.ok().entity("").build();
  }
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/authenticate")
  public Response authenticate(@Context HttpServletRequest request, @Context HttpServletResponse response){
    int tokenId = Integer.parseInt(request.getParameter("tokenId"));
    String token = request.getParameter("token");
    JSONObject json = new JSONObject();
    try {
      boolean authenticated = LoginManager.getInstance().authenticate(tokenId, token);
      json.put("authenticated", authenticated);
    } catch (SQLException e) {
      logger.error("Exception occurred while authenticating token", e);
      json.put("authenticated", false);
    }
    return Response.status(200).entity(json).build();
  }
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/validateAuthParams")
  public Response validateAuthParams(@Context HttpServletRequest request, @Context HttpServletResponse response){
    int tokenId = Integer.parseInt(request.getParameter("tokenId"));
    String token = request.getParameter("token");
    String userName = request.getParameter("userName");
    JSONObject json = new JSONObject();
    try {
      boolean areAuthParamsValid = LoginManager.getInstance().validateAuthParams(tokenId, token, userName);
      json.put("areAuthParamsValid", areAuthParamsValid);
    } catch (Exception e) {
      logger.error("Exception occurred while authenticating token", e);
      json.put("areAuthParamsValid", false);
    }
    return Response.status(200).entity(json).build();
  }
  
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/register")
  public Response register(@FormParam("username") String username, @FormParam("password") String password){
    return Response.ok().entity("").build();
  }
  
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/change-password")
  public Response changePassword(@FormParam("username") String username, @FormParam("oldpassword") String oldPassword, @FormParam("newpassword") String newPassword){
    return Response.ok().entity("").build();
  }
  
}
