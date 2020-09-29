/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.exception;

/**
 *
 * @version 1.0
 * @author:
 * 
 */
public class TokenNotFoundException extends BaseWebApplicationException {

	public TokenNotFoundException() {
		super(404, "40407", "Token Not Found",
				"No token could be found for that Id");
	}
}
