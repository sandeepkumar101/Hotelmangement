/* Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */
package com.spi.user.api;

import org.codehaus.jackson.annotate.JsonIgnore;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.*;

import com.spi.domain.Role;
/**
 *
 * @author:
 */
@XmlRootElement
public class DashboardLinks {

	private List iconsList;
	
	private List linksList;
 
	private List linksNameList;
	
	private List linksClassList;

	public DashboardLinks() {
	}

	
	public DashboardLinks(Role role) {
		
		if(role.equals(Role.authenticated))
		{
			buildUserAccessList();
		}
	}
	
	public DashboardLinks(String role) {
		
                System.out.println("DashboardLinks role:: "+role);
                System.out.println("DashboardLinks Teacher Condition role:: "+(role.equals("teacher")));
		if(role.equals("authenticated"))
		{
			buildUserAccessList();
		}
	}
	
	private void buildUserAccessList()
	{
		iconsList = new ArrayList();
		linksList = new ArrayList();
		linksNameList = new ArrayList();
		linksClassList = new ArrayList();
				
		iconsList.add("img/avatar5.png");
		iconsList.add("icons/Setting_Icon_128.png");
				
		linksList.add("");
		linksList.add("#settings.html");
		
		linksNameList.add("Room Details");
		linksNameList.add("Settings");
				
		linksClassList.add("ion-android-contacts");
		linksClassList.add("ion-gear-a");
		
	}

	public List getIconsList() {
		return iconsList;
	}


	public void setIconsList(List iconsList) {
		this.iconsList = iconsList;
	}


	public List getLinksList() {
		return linksList;
	}


	public void setLinksList(List linksList) {
		this.linksList = linksList;
	}


	public List getLinksNameList() {
		return linksNameList;
	}


	public void setLinksNameList(List linksNameList) {
		this.linksNameList = linksNameList;
	}


	public List getLinksClassList() {
		return linksClassList;
	}


	public void setLinksClassList(List linksClassList) {
		this.linksClassList = linksClassList;
	}
	
	
	
}
