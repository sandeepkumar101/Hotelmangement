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
public class AlreadyVerifiedException extends BaseWebApplicationException {

	public AlreadyVerifiedException() {
		super(409, "40905", "Already verified",
				"The token has already been verified");
	}
}
