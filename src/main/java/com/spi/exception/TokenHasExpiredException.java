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
public class TokenHasExpiredException extends BaseWebApplicationException {

	public TokenHasExpiredException() {
		super(403, "40304", "Token has expired",
				"An attempt was made to load a token that has expired");
	}
}
