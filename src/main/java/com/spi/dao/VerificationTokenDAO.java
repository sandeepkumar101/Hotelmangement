/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */

package com.spi.dao;

import com.spi.domain.VerificationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @version 1.0
 * @author:
 * 
 */
public interface VerificationTokenDAO extends
		JpaRepository<VerificationToken, Long> {

	@Query("select t from VerificationToken t where uuid = ?")
	VerificationToken findByUuid(String uuid);

	@Query("select t from VerificationToken t where token = ?")
	VerificationToken findByToken(String token);
}
