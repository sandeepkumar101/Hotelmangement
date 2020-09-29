/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.user.api;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author:
 */
@XmlRootElement
public class AuthenticatedUserToken {

	private String userId;
	private String token;

	public AuthenticatedUserToken() {
	}

	public AuthenticatedUserToken(String userId, String sessionToken) {
		this.userId = userId;
		this.token = sessionToken;
	}

	public String getUserId() {
		return userId;
	}

	public String getToken() {
		return token;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
