/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.resource;

import com.spi.service.VerificationTokenService;
import com.spi.user.api.EmailVerificationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @version 1.0
 * @author:
 * 
 */
@Path("verify")
@Component
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class VerificationResource {

	@Autowired
	protected VerificationTokenService verificationTokenService;

	@PermitAll
	@Path("tokens/{token}")
	@POST
	public Response verifyToken(@PathParam("token") String token) {
		verificationTokenService.verify(token);
		return Response.ok().build();
	}

	@PermitAll
	@Path("tokens")
	@POST
	public Response sendEmailToken(EmailVerificationRequest request) {
		verificationTokenService.generateEmailVerificationToken(request
				.getEmailAddress());
		return Response.ok().build();
	}
}
