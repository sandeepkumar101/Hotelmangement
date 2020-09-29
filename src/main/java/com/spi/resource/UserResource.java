/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.resource;

import java.io.*;
import java.net.URI;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spi.config.ApplicationConfig;
import com.spi.domain.Role;
import com.spi.exception.AuthorizationException;
import com.spi.gateway.EmailServicesGateway;
import com.spi.service.UserService;
import com.spi.service.VerificationTokenService;
import com.spi.user.api.AlertNotificationsConfig;
import com.spi.user.api.AuthenticatedUserToken;
import com.spi.user.api.CreateUserRequest;
import com.spi.user.api.DashboardLinks;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.LoginRequest;
import com.spi.user.api.OAuth2Request;
import com.spi.user.api.UpdateUserRequest;
import com.spi.util.StringUtil;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import java.util.ArrayList;
import java.util.Date;


/**
 */
@Path("/user")
@Component
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })

public class UserResource {

	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	protected UserService userService;

	@Autowired
	protected VerificationTokenService verificationTokenService;

	@Autowired
	protected EmailServicesGateway emailServicesGateway;
        
        @Autowired
        ApplicationConfig applicationConfig;

	@Context
	protected UriInfo uriInfo;

	@Autowired
	ApplicationConfig config;

	@Autowired
	public UserResource(ConnectionFactoryLocator connectionFactoryLocator) {
		this.connectionFactoryLocator = connectionFactoryLocator;
	}

	@PermitAll
	@Path("signup")
	@POST
	public Response signupUser(CreateUserRequest request) {
		AuthenticatedUserToken token = userService.createUser(request, Role.authenticated);
		verificationTokenService.sendEmailRegistrationToken(token.getUserId());
		URI location = uriInfo.getAbsolutePathBuilder().path(token.getUserId())
				.build();
		return Response.created(location).entity(token).build();
	}

	@RolesAllowed({"authenticated", "administrator", "teacher"})
	@Path("{userId}")
	@DELETE
	public Response deleteUser(@Context SecurityContext sc,
			@PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		userService.deleteUser(userMakingRequest, userId);
		return Response.ok().build();
	}

	@PermitAll
	@Path("login")
	@POST
	public Response login(LoginRequest request) {
		AuthenticatedUserToken token = userService.login(request);
		return getLoginResponse(token);
	}

	@PermitAll
	@Path("login/{providerId}")
	@POST
	public Response socialLogin(@PathParam("providerId") String providerId,
			OAuth2Request request) {
		OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator
				.getConnectionFactory(providerId);
		Connection<?> connection = connectionFactory
				.createConnection(new AccessGrant(request.getAccessToken()));
		AuthenticatedUserToken token = userService.socialLogin(connection);
		return getLoginResponse(token);
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("{userId}")
	@GET
	public Response getUser(@Context SecurityContext sc,
			@PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();		
		ExternalUser user = userService.getUser(userMakingRequest, userId);
		return Response.ok().entity(user).build();
	}

	
	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("saveUserDetails/{userId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@POST
	public Response saveUserDetails(@Context SecurityContext sc, @PathParam("userId") String userId, @FormDataParam("userPhoto") InputStream fileInputStream, @FormDataParam("userPhoto") FormDataContentDisposition contentDispositionHeader,  @FormDataParam("userDetails") String userDetails) {
		System.out.println("142::request:: ");
		String fileName = contentDispositionHeader.getFileName();
		String filePath = applicationConfig.getUserImageDirectory()+ fileName;
		Map paramValues = contentDispositionHeader.getParameters();
		System.out.println("paramValues:: "+paramValues.toString());
		System.out.println("userDetails:: "+userDetails);
		System.out.println("userId:: "+userId);
		
		
		ObjectMapper mapper = new ObjectMapper();
		//mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		UpdateUserRequest request = null;
		try {
			request = mapper.readValue(userDetails, UpdateUserRequest.class);
			request.setUserImage(fileName);
			System.out.println("request.getFirstName:: "+request.getFirstName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(StringUtil.isValid(fileName))
		{
			saveFile(fileInputStream, filePath);
		}
		String output = "File saved to server location : " + filePath;

		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		System.out.println("::request:: "+request.getFirstName());
		if (!userMakingRequest.getId().equals(userId)) {
			throw new AuthorizationException(
                        "User not authorized to modify this profile");
		}

		boolean sendVerificationToken = StringUtils.hasLength(request.getEmailAddress()) 
				&& !request.getEmailAddress().equals(userMakingRequest.getEmailAddress());
		ExternalUser savedUser = userService.updateUser(userId, request);
		if (sendVerificationToken) {
			verificationTokenService.sendEmailVerificationToken(savedUser.getId());
		}
		return Response.status(200).entity(savedUser).build();
		//return Response.ok().build();
	}
	
	// save uploaded file to a defined location on the server

    private void saveFile(InputStream uploadedInputStream, String serverLocation) {
        try {
            OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
            int read = 0;
            byte[] bytes = new byte[1024];
            outpuStream = new FileOutputStream(new File(serverLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }
            outpuStream.flush();
            outpuStream.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
	
	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("{userId}")
	@PUT
	public Response updateUser(@Context SecurityContext sc,
			@PathParam("userId") String userId, UpdateUserRequest request) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		System.out.println("::request:: "+request.getFirstName());
		if (!userMakingRequest.getId().equals(userId)) {
			throw new AuthorizationException(
                        "User not authorized to modify this profile");
		}

		boolean sendVerificationToken = StringUtils.hasLength(request
                        .getEmailAddress())
                        && !request.getEmailAddress().equals(userMakingRequest.getEmailAddress());
		ExternalUser savedUser = userService.saveUser(userId, request);
		if (sendVerificationToken) {
                    verificationTokenService.sendEmailVerificationToken(savedUser.getId());
		}
		return Response.ok().build();
	}

	private Response getLoginResponse(AuthenticatedUserToken token) {
		URI location = UriBuilder.fromPath(
                    uriInfo.getBaseUri() + "user/" + token.getUserId()).build();
		return Response.ok().entity(token).contentLocation(location).build();
	}
		
	
	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("getUserDashboard/{userId}")
	@GET
	public Response getUserDashboard(@Context SecurityContext sc,
			@PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();		
		//ExternalUser user = userService.getUser(userMakingRequest, userId);
                System.out.println("userMakingRequest Role:: "+userMakingRequest.getRole());
		DashboardLinks dashboardLinks = new DashboardLinks(userMakingRequest.getRole());
		
		return Response.ok().entity(dashboardLinks).build();
	}
	
	  @GET
	  @Path("userHome/{userId}")
	  @RolesAllowed({ "authenticated", "teacher", "administrator" })
	  @Produces(MediaType.TEXT_HTML)
	  public Response getUserHome(@Context SecurityContext sc, @PathParam("userId") String userId) {
                ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();								
                DashboardLinks dashboardLinks = new DashboardLinks(userMakingRequest.getRole());
                return Response.ok(new Viewable("/welcome", userMakingRequest.getRole())).build();
	}
          
          
        @Path("userPicture/{userId}")
        @Produces({"image/png","image/jpg", "image/gif"})
        public Response getFullImage(@Context SecurityContext sc, @PathParam("userId") String userId) {
            ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();	
            System.out.println("User Picture File:: "+userMakingRequest.getUserImage());            
            File userImage = new File(applicationConfig.getUserImageDirectory()+userMakingRequest.getUserImage());

            // uncomment line below to send non-streamed
            return Response.ok(userImage).build();

            // uncomment line below to send streamed
            //return Response.ok(new ByteArrayInputStream(imageData)).build();
        }
        
        @RolesAllowed({ "authenticated" })
	@Path("updateNotificationSettings/{userId}")
	@PUT
	public Response updateNotificationSettings(@Context SecurityContext sc, @PathParam("userId") String userId, AlertNotificationsConfig config) {
            
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
                System.out.println("alertType to save:: "+config.getAlertUpdateType());
                System.out.println("alertType to save:: "+config.getPickStartTime());
                System.out.println("alertType to save:: "+config.getActiveDays());
                
                String alertUpdateType = config.getAlertUpdateType();
                
                Date pickStartTime = config.getPickStartTime();    
                Date pickEndTime = config.getPickEndTime();

                Date dropStartTime = config.getDropStartTime();
                Date dropEndTime = config.getDropEndTime();

                ArrayList<String> activeDays = config.getActiveDays();

                ArrayList<String> alertTypes = config.getAlertTypes();
                
                
                userService.updateUserNotificationSettings(userId, alertUpdateType, config);
                
                
		return Response.ok().build();
	}
        
        
        @RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("getUserNotificationSettings/{userId}")
	@GET
	public Response getUserNotificationSettings(@Context SecurityContext sc,
			@PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();		
		//ExternalUser user = userService.getUser(userMakingRequest, userId);
                AlertNotificationsConfig alertNotificationsConfig = userService.getUserNotificationSettings(userId);
		
		return Response.ok().entity(alertNotificationsConfig).build();
	}


	
}
