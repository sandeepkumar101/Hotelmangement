/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.resource;

import com.spi.service.VerificationTokenService;
import com.spi.user.api.LostPasswordRequest;
import com.spi.user.api.PasswordRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @version 1.0
 * @author:
 * 
 */
@Path("password")
@Component
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class PasswordResource {

	@Autowired
	protected VerificationTokenService verificationTokenService;

	@PermitAll
	@Path("tokens")
	@POST
	public Response sendEmailToken(LostPasswordRequest request) {
		verificationTokenService.sendLostPasswordToken(request);
		return Response.ok().build();
	}

	@PermitAll
	@Path("tokens/{token}")
	@POST
	public Response resetPassword(
			@PathParam("token") String base64EncodedToken,
			PasswordRequest request) {
		verificationTokenService.resetPassword(base64EncodedToken, request);
		return Response.ok().build();
	}
}
