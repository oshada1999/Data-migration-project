package com.cba.datamigration.dto;

import java.time.LocalDateTime;

public class MachineDTO {
    private String mcnCode;
    private String mcnName;
    private String model;
    private String make;
    private String warranty;
    private Integer services;
    private String depCode;
    private Integer mcnType;
    private String estimateServiceTime;
    private String mcnCategory;
    private String addedBy;
    private LocalDateTime addedOn;
    private String mcnPriority;

    // Constructor, getters, setters
    public MachineDTO(String mcnCode, String mcnName, String model, String make, String warranty,
                      Integer services, String depCode, Integer mcnType, String estimateServiceTime,
                      String mcnCategory, String addedBy, LocalDateTime addedOn, String mcnPriority) {
        this.mcnCode = mcnCode;
        this.mcnName = mcnName;
        this.model = model;
        this.make = make;
        this.warranty = warranty;
        this.services = services;
        this.depCode = depCode;
        this.mcnType = mcnType;
        this.estimateServiceTime = estimateServiceTime;
        this.mcnCategory = mcnCategory;
        this.addedBy = addedBy;
        this.addedOn = addedOn;
        this.mcnPriority = mcnPriority;
    }

    // Getters...
    public String getMcnCode() {
        return mcnCode;
    }

    public String getMcnName() {
        return mcnName;
    }

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }

    public String getWarranty() {
        return warranty;
    }

    public Integer getServices() {
        return services;
    }

    public String getDepCode() {
        return depCode;
    }

    public Integer getMcnType() {
        return mcnType;
    }

    public String getEstimateServiceTime() {
        return estimateServiceTime;
    }

    public String getMcnCategory() {
        return mcnCategory;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public String getMcnPriority() {
        return mcnPriority;
    }

}
