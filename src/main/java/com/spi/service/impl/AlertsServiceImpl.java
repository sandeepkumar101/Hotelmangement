package com.spi.service.impl;

import java.util.List;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spi.service.BaseService;
import com.spi.user.social.JpaUsersConnectionRepository;
import com.spi.config.ApplicationConfig;
import com.spi.dao.AlertsDAO;
import com.spi.dao.UserDAO;
import com.spi.domain.Alert;
import com.spi.service.AlertsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service("alertsService")
public class AlertsServiceImpl extends BaseService implements AlertsService {

	private static final Logger LOG = LoggerFactory.getLogger(AlertsServiceImpl.class);
	
	private UsersConnectionRepository jpaUsersConnectionRepository;

	private AlertsDAO alertsRepository;

	private ApplicationConfig applicationConfig;

	public AlertsServiceImpl(Validator validator) {
		super(validator);
	}

	@Autowired
	public AlertsServiceImpl(Validator validator, ApplicationConfig applicationConfig) {
		this(validator);
		this.applicationConfig = applicationConfig;
	}

	
	
	@Transactional
	public Page<Alert> getLatestAlerts(Long userId, Pageable topTen) {
		
		return alertsRepository.findByUserId(userId, topTen);
	}

	@Transactional
	public int getTotalAlerts(String userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Transactional
	public List<Alert> getAllAlerts(Long userId) {
		// TODO Auto-generated method stub
		return alertsRepository.findAllByUserId(userId);
	}

	
	@Autowired
	public void setAlertsRepository(AlertsDAO alertsRepository) {
		this.alertsRepository = alertsRepository;
	}
	
	

}
