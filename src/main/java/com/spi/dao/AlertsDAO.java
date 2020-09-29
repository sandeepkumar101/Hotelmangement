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

import com.spi.domain.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 *
 * @version 1.0
 * @author:
 * 
 */
public interface AlertsDAO extends JpaRepository<Alert, Long> {

	@Query("select a from Alert a where user_id = ? order by a.alertDateTime desc")
	Page<Alert> findByUserId(Long user_id, Pageable topTen);
        
        @Query("select a from Alert a where user_id = ? order by a.alertDateTime desc")
	List<Alert> findAllByUserId(Long user_id);

	@Query("select COUNT(a) from Alert a where user_id = ?")
	Long countByUserId(String user_id);

}
