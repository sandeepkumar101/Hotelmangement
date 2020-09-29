/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */

package com.spi.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.spi.domain.User;

/**
 *
 * @version 1.0
 * @author:
 * 
 */
public interface UserDAO extends JpaRepository<User, Long> {

	User findByEmailAddress(String emailAddress);

	@Query("select u from User u where uuid = ?")
	User findByUuid(String uuid);

	@Query("select u from User u where u in (select user from AuthorizationToken where lastUpdated < ?)")
	List<User> findByExpiredSession(Date lastUpdated);

	@Query("select u from User u where u = (select user from AuthorizationToken where token = ?)")
	User findBySession(String token);

}
