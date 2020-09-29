/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.service;


import org.springframework.social.connect.Connection;

import com.spi.domain.AuthorizationToken;
import com.spi.domain.Role;
import com.spi.domain.User;
import com.spi.user.api.AlertNotificationsConfig;
import com.spi.user.api.AuthenticatedUserToken;
import com.spi.user.api.CreateUserRequest;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.LoginRequest;
import com.spi.user.api.UpdateUserRequest;

/**
 * @author:
 *
 *          Service to manage users
 */
public interface UserService {

	/**
	 * Create a new User with the given role
	 *
	 * @param request
	 * @param role
	 * @return AuthenticatedUserToken
	 */
	public AuthenticatedUserToken createUser(CreateUserRequest request,
			Role role);

	/**
	 * Create a Default User with a given role
	 *
	 * @param role
	 * @return AuthenticatedUserToken
	 */
	public AuthenticatedUserToken createUser(Role role);

	/**
	 * Login a User
	 *
	 * @param request
	 * @return AuthenticatedUserToken
	 */
	public AuthenticatedUserToken login(LoginRequest request);

	/**
	 * Log in a User using Connection details from an authorized request from
	 * the User's supported Social provider encapsulated in the
	 * {@link org.springframework.social.connect.Connection} parameter
	 *
	 * @param connection
	 *            containing the details of the authorized user account form the
	 *            Social provider
	 * @return the User account linked to the {@link com.spi.domain.SocialUser}
	 *         account
	 */
	public AuthenticatedUserToken socialLogin(Connection<?> connection);

	/**
	 * Get a User based on a unique identifier
	 *
	 * Identifiers supported are uuid, emailAddress
	 *
	 * @param userIdentifier
	 * @return User
	 */
	public ExternalUser getUser(ExternalUser requestingUser,
			String userIdentifier);

	/**
	 * Delete user, only authenticated user accounts can be deleted
	 *
	 * @param userMakingRequest
	 *            the user authorized to delete the user
	 * @param userId
	 *            the id of the user to delete
	 */
	public void deleteUser(ExternalUser userMakingRequest, String userId);

	/**
	 * Save User
	 *
	 * @param userId
	 * @param request
	 */
	public ExternalUser saveUser(String userId, UpdateUserRequest request);

	/**
	 * Create an AuthorizationToken for the User
	 *
	 * @return
	 */
	public AuthorizationToken createAuthorizationToken(User user);
	
	/**
	 * Update User
	 *
	 * @param userId
	 * @param request
	 */
	public ExternalUser updateUser(String userId, UpdateUserRequest request);
        
        public ExternalUser updateUserNotificationSettings(String userId, String configType, AlertNotificationsConfig config);
        
        public AlertNotificationsConfig getUserNotificationSettings(String userId);
	

}
