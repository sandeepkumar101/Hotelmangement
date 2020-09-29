/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */

package com.spi.authorization.impl;

import com.spi.authorization.AuthorizationRequestContext;
import com.spi.dao.UserDAO;
import com.spi.domain.AuthorizationToken;
import com.spi.domain.User;
import com.spi.exception.AuthorizationException;
import com.spi.service.AuthorizationService;
import com.spi.user.api.ExternalUser;

import java.util.Date;

/**
 *
 * Simple authorization service that requires a session token in the
 * Authorization header This is then matched to a user
 *
 * @version 1.0
 * @author:
 * 
 */
public class SessionTokenAuthorizationService implements AuthorizationService {

	/**
	 * directly access user objects
	 */
	private final UserDAO userRepository;

	public SessionTokenAuthorizationService(UserDAO repository) {
		this.userRepository = repository;
	}

	public ExternalUser authorize(AuthorizationRequestContext securityContext) {
		String token = securityContext.getAuthorizationToken();
		ExternalUser externalUser = null;
		if (token == null) {
			return externalUser;
		}
		User user = userRepository.findBySession(token);
		if (user == null) {
			throw new AuthorizationException("Session token not valid");
		}
		AuthorizationToken authorizationToken = user.getAuthorizationToken();
		if (authorizationToken.getToken().equals(token)) {
			externalUser = new ExternalUser(user);
		}
		return externalUser;
	}
}
