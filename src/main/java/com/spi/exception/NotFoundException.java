/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.exception;

import javax.ws.rs.WebApplicationException;

/**
 * 
 */
public class NotFoundException extends WebApplicationException {

	public NotFoundException() {
		super(404);
	}
}
