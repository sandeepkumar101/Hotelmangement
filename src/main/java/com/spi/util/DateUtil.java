/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;

/**
 * @author:
 */
public class DateUtil {

	private static final DateTimeFormatter ISO8061_FORMATTER = ISODateTimeFormat
			.dateTimeNoMillis();

	public static Date getDateFromIso8061DateString(String dateString) {
		return ISO8061_FORMATTER.parseDateTime(dateString).toDate();
	}

	public static String getCurrentDateAsIso8061String() {
		DateTime today = new DateTime();
		return ISO8061_FORMATTER.print(today);
	}

	public static String getDateDateAsIso8061String(DateTime date) {
		return ISO8061_FORMATTER.print(date);
	}
}
