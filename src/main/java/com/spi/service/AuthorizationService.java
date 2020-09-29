/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.service;

import com.spi.authorization.AuthorizationRequestContext;
import com.spi.user.api.ExternalUser;

/**
 *
 * @author:
 */
public interface AuthorizationService {

	/**
	 * Given an AuthorizationRequestContext validate and authorize a User
	 *
	 * @param authorizationRequestContext
	 *            the context required to authorize a user for a particular
	 *            request
	 * @return ExternalUser
	 */
	public ExternalUser authorize(
			AuthorizationRequestContext authorizationRequestContext);
}
