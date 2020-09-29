/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import com.spi.domain.AlertConfig;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author Gungun&Goldy
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlertNotificationsConfig {
    
    
    private Date pickStartTime;    
    private Date pickEndTime;
    
    private Date dropStartTime;
    private Date dropEndTime;
    
    private ArrayList<String> activeDays;
    
    private ArrayList<String> alertTypes;
    
    private String alertUpdateType;
    
    private int activeTime;

    public AlertNotificationsConfig() {}
    
    public AlertNotificationsConfig(AlertConfig alertConfig)
    {
        this.pickStartTime = alertConfig.getPickStartTime();
        this.pickEndTime = alertConfig.getPickEndTime();
        this.dropStartTime = alertConfig.getDropStartTime();
        this.dropEndTime = alertConfig.getDropEndTime();
        
        this.activeDays = alertConfig.getActiveDays();
        this.alertTypes = alertConfig.getAlertTypes();
        
        this.activeTime = alertConfig.getActiveTime();
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

    public ArrayList<String> getActiveDays() {
        return activeDays;
    }

    public void setActiveDays(ArrayList<String> activeDays) {
        this.activeDays = activeDays;
    }

    public ArrayList<String> getAlertTypes() {
        return alertTypes;
    }

    public void setAlertTypes(ArrayList<String> alertTypes) {
        this.alertTypes = alertTypes;
    }

    public String getAlertUpdateType() {
        return alertUpdateType;
    }

    public void setAlertUpdateType(String alertUpdateType) {
        this.alertUpdateType = alertUpdateType;
    }

    public int getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(int activeTime) {
        this.activeTime = activeTime;
    }
    
    
    
}
