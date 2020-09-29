/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.exception;

/**
 */
public class DuplicateUserException extends BaseWebApplicationException {

	public DuplicateUserException() {
		super(409, "40901", "User already exists",
				"An attempt was made to create a user that already exists");
	}
}
