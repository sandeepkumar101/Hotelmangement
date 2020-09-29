/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */

package com.spi.dao;

import com.spi.domain.SocialUser;
import com.spi.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Set;

/**
 */
public interface SocialUserDAO extends JpaRepository<SocialUser, Long> {

	List<SocialUser> findAllByUser(User user);

	List<SocialUser> findByUserAndProviderId(User user, String providerId);

	List<SocialUser> findByProviderIdAndProviderUserId(String providerId,
			String providerUserId);

	// TODO will need a JPA Query here
	List<SocialUser> findByUserAndProviderUserId(User user,
			MultiValueMap<String, String> providerUserIds);

	@Query("Select userId from SocialUser where providerId = ? AND providerUserId in (?)")
	Set<String> findByProviderIdAndProviderUserId(String providerId,
			Set<String> providerUserIds);

	SocialUser findByUserAndProviderIdAndProviderUserId(User user,
			String providerId, String providerUserId);

}
