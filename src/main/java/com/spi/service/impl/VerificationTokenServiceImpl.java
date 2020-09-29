/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.service.impl;

import com.spi.config.ApplicationConfig;
import com.spi.dao.UserDAO;
import com.spi.dao.VerificationTokenDAO;
import com.spi.domain.Role;
import com.spi.domain.User;
import com.spi.domain.VerificationToken;
import com.spi.email.EmailServiceTokenModel;
import com.spi.exception.*;
import com.spi.gateway.EmailServicesGateway;
import com.spi.service.BaseService;
import com.spi.service.VerificationTokenService;
import com.spi.user.api.LostPasswordRequest;
import com.spi.user.api.PasswordRequest;
import com.spi.util.StringUtil;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.Validator;

/**
 * @version 1.0
 * @author:
 * 
 */
@Service("verificationTokenService")
public class VerificationTokenServiceImpl extends BaseService implements
		VerificationTokenService {

	private VerificationTokenDAO tokenRepository;

	private EmailServicesGateway emailServicesGateway;

	private UserDAO userRepository;

	ApplicationConfig config;

	public VerificationTokenServiceImpl(Validator validator) {
		super(validator);
	}

	@Autowired
	public VerificationTokenServiceImpl(UserDAO userRepository,
			VerificationTokenDAO tokenRepository,
			EmailServicesGateway emailServicesGateway, Validator validator) {
		this(validator);
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
		this.emailServicesGateway = emailServicesGateway;
	}

	@Transactional
	public VerificationToken sendEmailVerificationToken(String userId) {
		User user = ensureUserIsLoaded(userId);
		return sendEmailVerificationToken(user);
	}

	private VerificationToken sendEmailVerificationToken(User user) {
		VerificationToken token = new VerificationToken(user,
				VerificationToken.VerificationTokenType.emailVerification,
				config.getEmailVerificationTokenExpiryTimeInMinutes());
		user.addVerificationToken(token);
		userRepository.save(user);
		emailServicesGateway.sendVerificationToken(new EmailServiceTokenModel(
				user, token, getConfig().getHostNameUrl()));
		return token;
	}

	@Transactional
	public VerificationToken sendEmailRegistrationToken(String userId) {
		User user = ensureUserIsLoaded(userId);
		VerificationToken token = new VerificationToken(user,
				VerificationToken.VerificationTokenType.emailRegistration,
				config.getEmailRegistrationTokenExpiryTimeInMinutes());
		user.addVerificationToken(token);
		userRepository.save(user);
		emailServicesGateway.sendVerificationToken(new EmailServiceTokenModel(
				user, token, getConfig().getHostNameUrl()));
		return token;
	}

	/**
	 * generate token if user found otherwise do nothing
	 *
	 * @param lostPasswordRequest
	 * @return a token or null if user not found
	 */
	@Transactional
	public VerificationToken sendLostPasswordToken(
			LostPasswordRequest lostPasswordRequest) {
		validate(lostPasswordRequest);
		VerificationToken token = null;
		User user = userRepository.findByEmailAddress(lostPasswordRequest
				.getEmailAddress());
		if (user != null) {
			token = user.getActiveLostPasswordToken();
			if (token == null) {
				token = new VerificationToken(user,
						VerificationToken.VerificationTokenType.lostPassword,
						config.getLostPasswordTokenExpiryTimeInMinutes());
				user.addVerificationToken(token);
				userRepository.save(user);
			}
			emailServicesGateway
					.sendVerificationToken(new EmailServiceTokenModel(user,
							token, getConfig().getHostNameUrl()));
		}

		return token;
	}

	@Transactional
	public VerificationToken verify(String base64EncodedToken) {
		VerificationToken token = loadToken(base64EncodedToken);
		if (token.isVerified() || token.getUser().isVerified()) {
			throw new AlreadyVerifiedException();
		}
		token.setVerified(true);
		token.getUser().setVerified(true);
		userRepository.save(token.getUser());
		return token;
	}

	@Transactional
	public VerificationToken generateEmailVerificationToken(String emailAddress) {
		Assert.notNull(emailAddress);
		User user = userRepository.findByEmailAddress(emailAddress);
		if (user == null) {
			throw new UserNotFoundException();
		}
		if (user.isVerified()) {
			throw new AlreadyVerifiedException();
		}
		// if token still active resend that
		VerificationToken token = user.getActiveEmailVerificationToken();
		if (token == null) {
			token = sendEmailVerificationToken(user);
		} else {
			emailServicesGateway
					.sendVerificationToken(new EmailServiceTokenModel(user,
							token, getConfig().getHostNameUrl()));
		}
		return token;
	}

	@Transactional
	public VerificationToken resetPassword(String base64EncodedToken,
			PasswordRequest passwordRequest) {
		Assert.notNull(base64EncodedToken);
		validate(passwordRequest);
		VerificationToken token = loadToken(base64EncodedToken);
		if (token.isVerified()) {
			throw new AlreadyVerifiedException();
		}
		token.setVerified(true);
		User user = token.getUser();
		try {
			user.setHashedPassword(user.hashPassword(passwordRequest
					.getPassword()));
		} catch (Exception e) {
			throw new AuthenticationException();
		}
		// set user to verified if not already and authenticated role
		user.setVerified(true);
		if (user.hasRole(Role.anonymous)) {
			user.setRole(Role.authenticated);
		}
		userRepository.save(user);
		return token;
	}

	private VerificationToken loadToken(String base64EncodedToken) {
		Assert.notNull(base64EncodedToken);
		String rawToken = new String(Base64.decodeBase64(base64EncodedToken));
		VerificationToken token = tokenRepository.findByToken(rawToken);
		if (token == null) {
			throw new TokenNotFoundException();
		}
		if (token.hasExpired()) {
			throw new TokenHasExpiredException();
		}
		return token;
	}

	@Autowired
	public void setConfig(ApplicationConfig config) {
		this.config = config;
	}

	@Autowired
	public void setUserRepository(UserDAO userRepository) {
		this.userRepository = userRepository;
	}

	public ApplicationConfig getConfig() {
		return this.config;
	}

	private User ensureUserIsLoaded(String userIdentifier) {
		User user = null;
		if (StringUtil.isValidUuid(userIdentifier)) {
			user = userRepository.findByUuid(userIdentifier);
		} else {
			user = userRepository.findByEmailAddress(userIdentifier);
		}
		if (user == null) {
			throw new UserNotFoundException();
		}
		return user;
	}
}
