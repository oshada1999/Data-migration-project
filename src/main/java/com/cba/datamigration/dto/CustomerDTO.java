package com.cba.datamigration.dto;

import java.time.LocalDateTime;

public class CustomerDTO {
    private String clnCode;
    private String clnName;
    private String clnSegment;
    private String clnSector;
    private String clnIndustry;
    private String notSure;
    private String addedBy;
    private LocalDateTime addedOn;
    private String clnPriority;

    // Constructor
    public CustomerDTO(String clnCode, String clnName, String clnSegment, String clnSector,
                       String clnIndustry, String notSure, String addedBy,
                       LocalDateTime addedOn, String clnPriority) {
        this.clnCode = clnCode;
        this.clnName = clnName;
        this.clnSegment = clnSegment;
        this.clnSector = clnSector;
        this.clnIndustry = clnIndustry;
        this.notSure = notSure;
        this.addedBy = addedBy;
        this.addedOn = addedOn;
        this.clnPriority = clnPriority;
    }

    // Default constructor (optional)
    public CustomerDTO() {}

    // Getters and Setters
    public String getClnCode() {
        return clnCode;
    }

    public void setClnCode(String clnCode) {
        this.clnCode = clnCode;
    }

    public String getClnName() {
        return clnName;
    }

    public void setClnName(String clnName) {
        this.clnName = clnName;
    }

    public String getClnSegment() {
        return clnSegment;
    }

    public void setClnSegment(String clnSegment) {
        this.clnSegment = clnSegment;
    }

    public String getClnSector() {
        return clnSector;
    }

    public void setClnSector(String clnSector) {
        this.clnSector = clnSector;
    }

    public String getClnIndustry() {
        return clnIndustry;
    }

    public void setClnIndustry(String clnIndustry) {
        this.clnIndustry = clnIndustry;
    }

    public String getNotSure() {
        return notSure;
    }

    public void setNotSure(String notSure) {
        this.notSure = notSure;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    public String getClnPriority() {
        return clnPriority;
    }

    public void setClnPriority(String clnPriority) {
        this.clnPriority = clnPriority;
    }
}
