/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.service;

import com.spi.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Set;

/**
 * @version 1.0
 * @author:
 * 
 */
public abstract class BaseService {

	private Validator validator;

	public BaseService(Validator validator) {
		this.validator = validator;
	}

	protected void validate(Object request) {
		Set<? extends ConstraintViolation<?>> constraintViolations = validator
				.validate(request);
		if (constraintViolations.size() > 0) {
			throw new ValidationException(constraintViolations);
		}
	}

}
