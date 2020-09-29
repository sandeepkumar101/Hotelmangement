/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.exception;

/**
 */
public class AuthenticationException extends BaseWebApplicationException {

	public AuthenticationException() {
		super(401, "40102", "Authentication Error",
				"Authentication Error. The username or password were incorrect");
	}

}
