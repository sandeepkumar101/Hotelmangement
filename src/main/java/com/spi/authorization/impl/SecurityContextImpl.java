/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */

package com.spi.authorization.impl;

import com.spi.authorization.exception.InvalidAuthorizationHeaderException;
import com.spi.domain.Role;
import com.spi.user.api.ExternalUser;

import javax.ws.rs.core.SecurityContext;

import java.security.Principal;

/**
 * Implementation of {@link javax.ws.rs.core.SecurityContext}
 *
 * 
 */
public class SecurityContextImpl implements SecurityContext {

	private final ExternalUser user;

	public SecurityContextImpl(ExternalUser user) {
		this.user = user;
	}

	public Principal getUserPrincipal() {
		return user;
	}

	public boolean isUserInRole(String role) {
		if (role.equalsIgnoreCase(Role.anonymous.name())) {
			return true;
		}
		if (user == null) {
			throw new InvalidAuthorizationHeaderException();
		}
		return user.getRole().equalsIgnoreCase(role);
	}

	public boolean isSecure() {
		return false;
	}

	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}
}
