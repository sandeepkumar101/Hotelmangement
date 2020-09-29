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
public class UserNotFoundException extends BaseWebApplicationException {

	public UserNotFoundException() {
		super(404, "40402", "User Not Found",
				"No User could be found for that Id");
	}
}
