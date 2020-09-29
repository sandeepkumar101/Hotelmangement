/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.email;

import com.spi.domain.User;
import com.spi.domain.VerificationToken;

import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;

/**
 *
 * @version 1.0
 * @author:
 * 
 */
public class EmailServiceTokenModel implements Serializable {

	private final String emailAddress;
	private final String token;
	private final VerificationToken.VerificationTokenType tokenType;
	private final String hostNameUrl;

	public EmailServiceTokenModel(User user, VerificationToken token,
			String hostNameUrl) {
		this.emailAddress = user.getEmailAddress();
		this.token = token.getToken();
		this.tokenType = token.getTokenType();
		this.hostNameUrl = hostNameUrl;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getEncodedToken() {
		return new String(Base64.encodeBase64(token.getBytes()));
	}

	public String getToken() {
		return token;
	}

	public VerificationToken.VerificationTokenType getTokenType() {
		return tokenType;
	}

	public String getHostNameUrl() {
		return hostNameUrl;
	}
}
