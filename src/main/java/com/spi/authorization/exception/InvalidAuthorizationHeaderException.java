/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */

package com.spi.authorization.exception;

import com.spi.exception.BaseWebApplicationException;

/**
 *
 * @version 1.0
 * @author:
 * 
 */
public class InvalidAuthorizationHeaderException extends
		BaseWebApplicationException {

	public static final String DEVELOPER_MESSAGE = "Authorization failed. This could be due to missing properties in the header or"
			+ " the Authorization header may have been incorrectly hashed";

	public InvalidAuthorizationHeaderException() {
		super(401, "40101", "Authorization failed", DEVELOPER_MESSAGE);
	}

}
