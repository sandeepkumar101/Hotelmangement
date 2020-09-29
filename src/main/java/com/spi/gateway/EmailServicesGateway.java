/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.gateway;

import com.spi.email.EmailServiceTokenModel;

/**
 *
 * @version 1.0
 * @author:
 * 
 */
public interface EmailServicesGateway {

	public void sendVerificationToken(EmailServiceTokenModel model);
}
