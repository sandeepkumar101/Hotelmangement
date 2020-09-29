/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.spi.config.ApplicationConfig;
import com.spi.dao.UserDAO;
import com.spi.domain.Address;
import com.spi.domain.AlertConfig;
import com.spi.domain.AuthorizationToken;
import com.spi.domain.Role;
import com.spi.domain.User;
import com.spi.exception.AuthenticationException;
import com.spi.exception.AuthorizationException;
import com.spi.exception.DuplicateUserException;
import com.spi.exception.UserNotFoundException;
import com.spi.service.BaseService;
import com.spi.service.UserService;
import com.spi.user.api.AlertNotificationsConfig;
import com.spi.user.api.AuthenticatedUserToken;
import com.spi.user.api.CreateUserRequest;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.LoginRequest;
import com.spi.user.api.UpdateUserRequest;
import com.spi.user.social.JpaUsersConnectionRepository;
import com.spi.util.StringUtil;

/**
 * Service for managing User accounts
 *
 * @author:
 */
@Service("userService")
public class UserServiceImpl extends BaseService implements UserService {

	/**
	 * For Social API handling
	 */
	private UsersConnectionRepository jpaUsersConnectionRepository;

	private UserDAO userRepository;

	private ApplicationConfig applicationConfig;

	public UserServiceImpl(Validator validator) {
		super(validator);
	}

	@Autowired
	public UserServiceImpl(UsersConnectionRepository usersConnectionRepository,
			Validator validator, ApplicationConfig applicationConfig) {
		this(validator);
		this.jpaUsersConnectionRepository = usersConnectionRepository;
		((JpaUsersConnectionRepository) this.jpaUsersConnectionRepository)
				.setUserService(this);
		this.applicationConfig = applicationConfig;
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(UserServiceImpl.class);

	/**
	 * {@inheritDoc}
	 *
	 * This method creates a User with the given Role. A check is made to see if
	 * the username already exists and a duplication check is made on the email
	 * address if it is present in the request.
	 * <P>
	 * </P>
	 * The password is hashed and a AuthorizationToken generated for subsequent
	 * authorization of role-protected requests.
	 *
	 */

	@Transactional
	public AuthenticatedUserToken createUser(CreateUserRequest request,
			Role role) {
		validate(request);
		User searchedForUser = userRepository.findByEmailAddress(request
				.getUser().getEmailAddress());
		if (searchedForUser != null) {
			throw new DuplicateUserException();
		}

		User newUser = createNewUser(request, role);
		AuthenticatedUserToken token = new AuthenticatedUserToken(newUser
				.getUuid().toString(), createAuthorizationToken(newUser)
				.getToken());
		userRepository.save(newUser);
		return token;
	}

	@Transactional
	public AuthenticatedUserToken createUser(Role role) {
		User user = new User();
		user.setRole(role);
		AuthenticatedUserToken token = new AuthenticatedUserToken(user
				.getUuid().toString(), createAuthorizationToken(user)
				.getToken());
		userRepository.save(user);
		return token;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Login supports authentication against an email attribute. If a User is
	 * retrieved that matches, the password in the request is hashed and
	 * compared to the persisted password for the User account.
	 */
	@Transactional
	public AuthenticatedUserToken login(LoginRequest request) {
		validate(request);
		User user = null;
		user = userRepository.findByEmailAddress(request.getUsername());
		if (user == null) {
			throw new AuthenticationException();
		}
		String hashedPassword = null;
		try {
			hashedPassword = user.hashPassword(request.getPassword());
		} catch (Exception e) {
			throw new AuthenticationException();
		}
		if (hashedPassword.equals(user.getHashedPassword())) {
			return new AuthenticatedUserToken(user.getUuid().toString(),
					createAuthorizationToken(user).getToken());
		} else {
			throw new AuthenticationException();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Associate a Connection with a User account. If one does not exist a new
	 * User is created and linked to the {@link com.spi.domain.SocialUser}
	 * represented in the Connection details.
	 *
	 * <P>
	 * </P>
	 *
	 * A AuthorizationToken is generated and any Profile data that can be
	 * collected from the Social account is propagated to the User object.
	 *
	 */
	@Transactional
	public AuthenticatedUserToken socialLogin(Connection<?> connection) {

		List<String> userUuids = jpaUsersConnectionRepository
				.findUserIdsWithConnection(connection);
		if (userUuids.size() == 0) {
			throw new AuthenticationException();
		}
		User user = userRepository.findByUuid(userUuids.get(0)); // take the
																	// first one
																	// if there
																	// are
																	// multiple
																	// userIds
																	// for this
																	// provider
																	// Connection
		if (user == null) {
			throw new AuthenticationException();
		}
		updateUserFromProfile(connection, user);
		return new AuthenticatedUserToken(user.getUuid().toString(),
				createAuthorizationToken(user).getToken());
	}

	/**
	 * Allow user to get their own profile or a user with administrator role to
	 * get any profile
	 *
	 * @param requestingUser
	 * @param userIdentifier
	 * @return user
	 */
	@Transactional
	public ExternalUser getUser(ExternalUser requestingUser,
			String userIdentifier) {
		Assert.notNull(requestingUser);
		Assert.notNull(userIdentifier);
		User user = ensureUserIsLoaded(userIdentifier);
		if (!requestingUser.getId().equals(user.getUuid().toString())
				&& !requestingUser.getRole().equalsIgnoreCase(
						Role.administrator.toString())) {
			throw new AuthorizationException(
					"User not authorized to load profile");
		}
		return new ExternalUser(user);
	}

	@Transactional
	public void deleteUser(ExternalUser userMakingRequest, String userId) {
		Assert.notNull(userMakingRequest);
		Assert.notNull(userId);
		User userToDelete = ensureUserIsLoaded(userId);
		if (userMakingRequest.getRole().equalsIgnoreCase(
				Role.administrator.toString())
				&& (userToDelete.hasRole(Role.anonymous) || userToDelete
						.hasRole(Role.authenticated))) {
			userRepository.delete(userToDelete);
		} else {
			throw new AuthorizationException(
					"User cannot be deleted. Only users with anonymous or authenticated role can be deleted.");
		}
	}

	@Transactional
	public ExternalUser saveUser(String userId, UpdateUserRequest request) {
		validate(request);
		User user = ensureUserIsLoaded(userId);
		if (request.getFirstName() != null) {
			user.setFirstName(request.getFirstName());
		}
		if (request.getLastName() != null) {
			user.setLastName(request.getLastName());
		}
		if (request.getEmailAddress() != null) {
			if (!request.getEmailAddress().equals(user.getEmailAddress())) {
				user.setEmailAddress(request.getEmailAddress());
				user.setVerified(false);
			}
		}
		userRepository.save(user);
		return new ExternalUser(user);
	}
	
	@Transactional
	public ExternalUser updateUser(String userId, UpdateUserRequest request) {
		validate(request);
		//User user = new User(request);
		User existingUser = ensureUserIsLoaded(userId);
		
		if (request.getEmailAddress() != null) {
			if (!request.getEmailAddress().equals(existingUser.getEmailAddress())) {
				existingUser.setEmailAddress(request.getEmailAddress());
				existingUser.setVerified(false);
			}
		}
		existingUser.setUserDetails(request);
		userRepository.save(existingUser);
		return new ExternalUser(existingUser);
	}

	@Override
	public AuthorizationToken createAuthorizationToken(User user) {
		if (user.getAuthorizationToken() == null
				|| user.getAuthorizationToken().hasExpired()) {
			user.setAuthorizationToken(new AuthorizationToken(user,
					applicationConfig.getAuthorizationExpiryTimeInSeconds()));
			userRepository.save(user);
		}
		return user.getAuthorizationToken();
	}

	private User createNewUser(CreateUserRequest request, Role role) {
		User userToSave = new User(request.getUser());
		Address addressToSave = new Address(request.getAddress(), userToSave);
		try {
			ArrayList<Address> addresses = new ArrayList<Address>();
			addresses.add(addressToSave);
			userToSave.setAddresses(addresses);
			userToSave.setHashedPassword(userToSave.hashPassword(request
					.getPassword().getPassword()));
		} catch (Exception e) {
			throw new AuthenticationException();
		}
		userToSave.setRole(role);
		return userToSave;
	}

	private void updateUserFromProfile(Connection<?> connection, User user) {
		UserProfile profile = connection.fetchUserProfile();
		user.setEmailAddress(profile.getEmail());
		user.setFirstName(profile.getFirstName());
		user.setLastName(profile.getLastName());
		// users logging in from social network are already verified
		user.setVerified(true);
		if (user.hasRole(Role.anonymous)) {
			user.setRole(Role.authenticated);
		}
		userRepository.save(user);
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

	@Autowired
	public void setUserRepository(UserDAO userRepository) {
		this.userRepository = userRepository;
	}
        
        public ExternalUser updateUserNotificationSettings(String userId, String configType, AlertNotificationsConfig config)
        {
            User user = ensureUserIsLoaded(userId);
            
            AlertConfig alertConfig = user.getAlertConfig();
             System.out.println("Before Save alertConfig:: "+alertConfig);
            if(alertConfig ==null)
            {
                alertConfig = new AlertConfig();
                user.setAlertConfig(alertConfig);
            }
            alertConfig.setUser(user);
            if("pickTime".equals(configType))
            {
                alertConfig.setPickStartTime(config.getPickStartTime());
                alertConfig.setPickEndTime(config.getPickEndTime());
            }
            else if("dropTime".equals(configType))
            {
                alertConfig.setDropStartTime(config.getDropStartTime());
                alertConfig.setDropEndTime(config.getDropEndTime());
            }
            else if("alertDays".equals(configType))
            {
                alertConfig.setActiveDays(config.getActiveDays());
            }
            else if("alertTypesSel".equals(configType))
            {
                alertConfig.setAlertTypes(config.getAlertTypes());
            }
            else if("enableNotification".equals(configType))
            {
                alertConfig.setActiveTime(config.getActiveTime());
            }
            
            System.out.println("Save alertConfig:: "+alertConfig);
            userRepository.save(user);
            
            return new ExternalUser(user);
        }
        
        public AlertNotificationsConfig getUserNotificationSettings(String userId)
        {
            User user = ensureUserIsLoaded(userId);
            
            AlertConfig alertConfig = user.getAlertConfig();
            
            AlertNotificationsConfig alertNotificationsConfig = new AlertNotificationsConfig(alertConfig);
            
            return alertNotificationsConfig;
        }

}
