/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.filter;

import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

/**
 * Add the SecurityContextFilter to the list of Filters to apply to requests
 *
 * This factory is registered with the Web Context:
 *
 * <code>
 *     <init-param>
            <param-name>com.sun.jersey.spi.container.ResourceFilters</param-name>
            <param-value>com.spi.com.spi.filter.ResourceFilterFactory</param-value>
        </init-param>
 * </code>
 *
 *
 * @author:
 */
@Component
@Provider
public class ResourceFilterFactory extends RolesAllowedResourceFilterFactory {

	@Autowired
	private SecurityContextFilter securityContextFilter;

	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		List<ResourceFilter> filters = super.create(am);
		if (filters == null) {
			filters = new ArrayList<ResourceFilter>();
		}
		List<ResourceFilter> securityFilters = new ArrayList<ResourceFilter>(
				filters);
		// put the Security Filter first in line
		securityFilters.add(0, securityContextFilter);
		return securityFilters;
	}
}
