/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.exception;

/**
 */
public class AuthorizationException extends BaseWebApplicationException {

	public AuthorizationException(String applicationMessage) {
		super(403, "40301", "Not authorized", applicationMessage);
	}

}
