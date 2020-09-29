/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.domain;

import com.spi.model.BaseEntity;

import org.joda.time.DateTime;

import javax.persistence.*;

import java.util.Date;
import java.util.UUID;

/**
 * A token that gives the user permission to carry out a specific task once
 * within a determined time period. An example would be a Lost Password token.
 * The user receives the token embedded in a link. They send the token back to
 * the server by clicking the link and the action is processed
 *
 * @version 1.0
 * @author:
 * 
 */
@Entity
@Table(name = "verification_token")
public class VerificationToken extends BaseEntity {

	private static final int DEFAULT_EXPIRY_TIME_IN_MINS = 60 * 24; // 24 hours

	@Column(length = 36)
	private final String token;

	private Date expiryDate;

	@Enumerated(EnumType.STRING)
	private VerificationTokenType tokenType;

	private boolean verified;

	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;

	public VerificationToken() {
		super();
		this.token = UUID.randomUUID().toString();
		this.expiryDate = calculateExpiryDate(DEFAULT_EXPIRY_TIME_IN_MINS);
	}

	public VerificationToken(User user, VerificationTokenType tokenType,
			int expirationTimeInMinutes) {
		this();
		this.user = user;
		this.tokenType = tokenType;
		this.expiryDate = calculateExpiryDate(expirationTimeInMinutes);
	}

	public VerificationTokenType getTokenType() {
		return tokenType;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public String getToken() {
		return token;
	}

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		DateTime now = new DateTime();
		return now.plusMinutes(expiryTimeInMinutes).toDate();
	}

	public enum VerificationTokenType {

		lostPassword, emailVerification, emailRegistration
	}

	public boolean hasExpired() {
		DateTime tokenDate = new DateTime(getExpiryDate());
		return tokenDate.isBeforeNow();
	}
}
