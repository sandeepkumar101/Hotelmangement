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

import com.spi.model.BaseEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "alertconfig")
public class AlertConfig extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 927936135058330166L;

	int studentId;
	int alertId;
        
	//String activeDays;
        
        @Column(name = "activeDays", nullable = true)
        private ArrayList<String> activeDays = new ArrayList<String>();
        
        @Column(name = "alertTypes", nullable = true)
        private ArrayList<String> alertTypes = new ArrayList<String>();
        
        @Temporal(TemporalType.TIME)
        private Date pickStartTime;

        @Temporal(TemporalType.TIME)
        private Date pickEndTime;
        
        @Temporal(TemporalType.TIME)
        private Date dropStartTime;

        @Temporal(TemporalType.TIME)
        private Date dropEndTime;
        
        @Column(columnDefinition="int DEFAULT 1")
	int activeTime;
	//int userId;
        
	@OneToOne(mappedBy = "alertConfig", targetEntity = AlertType.class, cascade = CascadeType.ALL)
	private AlertType alertType;
        
        
        @OneToOne
	@JoinColumn(name = "user_id")
	User user;

        /*
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
        */

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getAlertId() {
		return alertId;
	}

	public void setAlertId(int alertId) {
		this.alertId = alertId;
	}
        /*
	public String getActiveDays() {
		return activeDays;
	}

	public void setActiveDays(String activeDays) {
		this.activeDays = activeDays;
	}
        */
       
	public int getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(int activeTime) {
		this.activeTime = activeTime;
	}
        

    public ArrayList<String> getActiveDays() {
        return activeDays;
    }

    public void setActiveDays(ArrayList<String> activeDays) {
        this.activeDays = activeDays;
    }

    public Date getPickStartTime() {
        return pickStartTime;
    }

    public void setPickStartTime(Date pickStartTime) {
        this.pickStartTime = pickStartTime;
    }

    public Date getPickEndTime() {
        return pickEndTime;
    }

    public void setPickEndTime(Date pickEndTime) {
        this.pickEndTime = pickEndTime;
    }

    public Date getDropStartTime() {
        return dropStartTime;
    }

    public void setDropStartTime(Date dropStartTime) {
        this.dropStartTime = dropStartTime;
    }

    public Date getDropEndTime() {
        return dropEndTime;
    }

    public void setDropEndTime(Date dropEndTime) {
        this.dropEndTime = dropEndTime;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<String> getAlertTypes() {
        return alertTypes;
    }

    public void setAlertTypes(ArrayList<String> alertTypes) {
        this.alertTypes = alertTypes;
    }

}
