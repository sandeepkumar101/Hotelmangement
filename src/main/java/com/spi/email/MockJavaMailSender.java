/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.email;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author:
 */
public class MockJavaMailSender implements JavaMailSender {

	List<MimeMessage> messages = new ArrayList<MimeMessage>();

	public MimeMessage createMimeMessage() {
		MimeMessage message = new MimeMessage(
				Session.getInstance(new Properties()));
		return message;
	}

	public MimeMessage createMimeMessage(InputStream contentStream)
			throws MailException {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	public void send(MimeMessage mimeMessage) throws MailException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	public void send(MimeMessage[] mimeMessages) throws MailException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	public void send(MimeMessagePreparator mimeMessagePreparator)
			throws MailException {
		try {
			MimeMessage mimeMessage = createMimeMessage();
			mimeMessagePreparator.prepare(mimeMessage);
			messages.add(mimeMessage);
		} catch (Exception e) {
			System.out.println("Exception while preparing Mail Message" + e);
			throw new RuntimeException(e);
		}

	}

	public void send(MimeMessagePreparator[] mimeMessagePreparators)
			throws MailException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	public void send(SimpleMailMessage simpleMessage) throws MailException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	public void send(SimpleMailMessage[] simpleMessages) throws MailException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	public List<MimeMessage> getMessages() {
		return messages;
	}
}
