package com.cba.datamigration.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public class ServiceAgreementDTO {
    private String saCode;
    private String period;
    private Double cumAmount;
    private Double discount;
    private String vatType;
    private Double vat;
    private String nbtType;
    private Double nbt;
    private Double total;
    private String saEndDate;
    private String serviceCount;
    private String saId;
    private String saCategoryRemark;
    private String followUpCategory;
    private String phone;
    private String contactPer;
    private String saStartDate;
    private String clnCode;
    private String deliveryMethod;
    private String status;
    private String saStatus;
    private String saType;
    private String saleCode;
    private String saCategory;
    private String warrantyPeriod;

    // Default constructor
    public ServiceAgreementDTO() {
    }

    // All-args constructor
    public ServiceAgreementDTO(String saCode, String period, Double cumAmount, Double discount,
                               String vatType, Double vat, String nbtType, Double nbt,
                               Double total, String saEndDate, String serviceCount, String saId,
                               String saCategoryRemark, String followUpCategory, String phone, String contactPer,
                               String saStartDate, String clnCode, String deliveryMethod, String status,
                               String saStatus, String saType, String saleCode, String saCategory, String warrantyPeriod) {
        this.saCode = saCode;
        this.period = period;
        this.cumAmount = cumAmount;
        this.discount = discount;
        this.vatType = vatType;
        this.vat = vat;
        this.nbtType = nbtType;
        this.nbt = nbt;
        this.total = total;
        this.saEndDate = saEndDate;
        this.serviceCount = serviceCount;
        this.saId = saId;
        this.saCategoryRemark = saCategoryRemark;
        this.followUpCategory = followUpCategory;
        this.phone = phone;
        this.contactPer = contactPer;
        this.saStartDate = saStartDate;
        this.clnCode = clnCode;
        this.deliveryMethod = deliveryMethod;
        this.status = status;
        this.saStatus = saStatus;
        this.saType = saType;
        this.saleCode = saleCode;
        this.saCategory = saCategory;
        this.warrantyPeriod = warrantyPeriod;
    }

    // Getters and Setters
    public String getSaCode() {
        return saCode;
    }

    public void setSaCode(String saCode) {
        this.saCode = saCode;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Double getCumAmount() {
        return cumAmount;
    }

    public void setCumAmount(Double cumAmount) {
        this.cumAmount = cumAmount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getVatType() {
        return vatType;
    }

    public void setVatType(String vatType) {
        this.vatType = vatType;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public String getNbtType() {
        return nbtType;
    }

    public void setNbtType(String nbtType) {
        this.nbtType = nbtType;
    }

    public Double getNbt() {
        return nbt;
    }

    public void setNbt(Double nbt) {
        this.nbt = nbt;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getSaEndDate() {
        return saEndDate;
    }

    public void setSaEndDate(String saEndDate) {
        this.saEndDate = saEndDate;
    }

    public String getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(String serviceCount) {
        this.serviceCount = serviceCount;
    }

    public String getSaId() {
        return saId;
    }

    public void setSaId(String saId) {
        this.saId = saId;
    }

    public String getSaCategoryRemark() {
        return saCategoryRemark;
    }

    public void setSaCategoryRemark(String saCategoryRemark) {
        this.saCategoryRemark = saCategoryRemark;
    }

    public String getFollowUpCategory() {
        return followUpCategory;
    }

    public void setFollowUpCategory(String followUpCategory) {
        this.followUpCategory = followUpCategory;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactPer() {
        return contactPer;
    }

    public void setContactPer(String contactPer) {
        this.contactPer = contactPer;
    }

    public String getSaStartDate() {
        return saStartDate;
    }

    public void setSaStartDate(String saStartDate) {
        this.saStartDate = saStartDate;
    }

    public String getClnCode() {
        return clnCode;
    }

    public void setClnCode(String clnCode) {
        this.clnCode = clnCode;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSaStatus() {
        return saStatus;
    }

    public void setSaStatus(String saStatus) {
        this.saStatus = saStatus;
    }

    public String getSaType() {
        return saType;
    }

    public void setSaType(String saType) {
        this.saType = saType;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public void setSaleCode(String saleCode) {
        this.saleCode = saleCode;
    }

    public String getSaCategory() {
        return saCategory;
    }

    public void setSaCategory(String saCategory) {
        this.saCategory = saCategory;
    }

    public String getWarrantyPeriod() {return warrantyPeriod;}
    public void setWarrantyPeriod(String warrantyPeriod) {this.warrantyPeriod = warrantyPeriod;}

    @Override
    public String toString() {
        return "ServiceAgreementDTO{" +
                "saCode='" + saCode + '\'' +
                ", period='" + period + '\'' +
                ", cumAmount=" + cumAmount +
                ", discount=" + discount +
                ", vatType='" + vatType + '\'' +
                ", vat=" + vat +
                ", nbtType='" + nbtType + '\'' +
                ", nbt=" + nbt +
                ", total=" + total +
                ", saEndDate=" + saEndDate +
                ", serviceCount=" + serviceCount +
                ", saId='" + saId + '\'' +
                ", saCategoryRemark='" + saCategoryRemark + '\'' +
                ", followUpCategory='" + followUpCategory + '\'' +
                ", phone='" + phone + '\'' +
                ", contactPer='" + contactPer + '\'' +
                ", saStartDate=" + saStartDate +
                ", clnCode='" + clnCode + '\'' +
                ", deliveryMethod='" + deliveryMethod + '\'' +
                ", status='" + status + '\'' +
                ", saStatus='" + saStatus + '\'' +
                ", saType='" + saType + '\'' +
                ", saleCode='" + saleCode + '\'' +
                ", saCategory='" + saCategory + '\'' +
                '}';
    }
}