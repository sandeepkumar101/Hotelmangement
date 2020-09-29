/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.user.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import com.spi.config.ApplicationConfig;
import com.spi.dao.SocialUserDAO;
import com.spi.dao.UserDAO;

/**
 */
@Configuration
public class SocialConfig {

	@Autowired
	ApplicationConfig config;

	@Autowired
	SocialUserDAO socialUserRepository;

	@Autowired
	UserDAO userRepository;

	@Autowired
	TextEncryptor textEncryptor;

	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new FacebookConnectionFactory(config
				.getFacebookClientId(), config.getFacebookClientSecret()));
		return registry;
	}

	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		JpaUsersConnectionRepository usersConnectionRepository = new JpaUsersConnectionRepository(
				socialUserRepository, userRepository,
				connectionFactoryLocator(), textEncryptor);

		return usersConnectionRepository;
	}
}