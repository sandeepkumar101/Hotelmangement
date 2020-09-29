/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.Date;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "alert")
public class Alert extends BaseEntity {

	/**
	 * 
	 */
	int userId;
	int studentId;
	Date alertDateTime;
	String addText;

	@OneToOne(mappedBy = "alert", targetEntity = AlertType.class, cascade = CascadeType.ALL)
	private AlertType alertType;

	private static final long serialVersionUID = -3464704269743212311L;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public Date getAlertDateTime() {
		return alertDateTime;
	}

	public void setAlertDateTime(Date alertDateTime) {
		this.alertDateTime = alertDateTime;
	}

	public String getAddText() {
		return addText;
	}

	public void setAddText(String addText) {
		this.addText = addText;
	}

}
