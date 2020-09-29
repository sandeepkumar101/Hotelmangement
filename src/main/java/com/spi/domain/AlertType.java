/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "alerttype")
public class AlertType extends BaseEntity {

	/**
	 * 
	 */

	@OneToOne
	@JoinColumn(name = "alerttype_id", insertable=false, updatable=false)
	AlertConfig alertConfig;

	@OneToOne
	@JoinColumn(name = "alerttype_id", insertable=false, updatable=false)
	Alert alert;

	private static final long serialVersionUID = -53026811590533833L;
	String alertType;

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public String getAlertDescription() {
		return alertDescription;
	}

	public void setAlertDescription(String alertDescription) {
		this.alertDescription = alertDescription;
	}

	String alertDescription;

}
