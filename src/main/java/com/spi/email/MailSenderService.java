/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.email;

/**
 *
 * @version 1.0
 * @author:
 * 
 */
public interface MailSenderService {

	public EmailServiceTokenModel sendVerificationEmail(
			EmailServiceTokenModel emailServiceTokenModel);

	public EmailServiceTokenModel sendRegistrationEmail(
			EmailServiceTokenModel emailServiceTokenModel);

	public EmailServiceTokenModel sendLostPasswordEmail(
			EmailServiceTokenModel emailServiceTokenModel);

}
