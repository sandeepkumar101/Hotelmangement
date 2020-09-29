/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.exception;

public class ApplicationRuntimeException extends BaseWebApplicationException {

	public ApplicationRuntimeException(String applicationMessage) {
		super(500, "50002", "Internal System error", applicationMessage);
	}
}
