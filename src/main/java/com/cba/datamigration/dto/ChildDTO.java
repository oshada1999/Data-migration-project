package com.cba.datamigration.dto;


import java.sql.Date;

public class ChildDTO {
    private String cusCode;
    private String clnCode;
    private String address;
    private String location;
    private String province;
    private String contactPer;
    private String saAttn;
    private String phone;
    private String fax;
    private String email;
    private String cusType;
    private String addedBy;
    private String addedOn;
    private String id;
    private String district;
    private Date joinDate;

    // Default constructor
    public ChildDTO() {
    }

    // Parameterized constructor
    public ChildDTO(String cusCode, String clnCode, String address, String location,
                    String province, String contactPer, String saAttn, String phone,
                    String fax, String email, String cusType, String addedBy,
                    String addedOn, String id, String district, Date joinDate) {
        this.cusCode = cusCode;
        this.clnCode = clnCode;
        this.address = address;
        this.location = location;
        this.province = province;
        this.contactPer = contactPer;
        this.saAttn = saAttn;
        this.phone = phone;
        this.fax = fax;
        this.email = email;
        this.cusType = cusType;
        this.addedBy = addedBy;
        this.addedOn = addedOn;
        this.id = id;
        this.district = district;
        this.joinDate = joinDate;
    }

    // Getters and Setters
    public String getCusCode() {
        return cusCode;
    }

    public void setCusCode(String cusCode) {
        this.cusCode = cusCode;
    }

    public String getClnCode() {
        return clnCode;
    }

    public void setClnCode(String clnCode) {
        this.clnCode = clnCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getContactPer() {
        return contactPer;
    }

    public void setContactPer(String contactPer) {
        this.contactPer = contactPer;
    }

    public String getSaAttn() {
        return saAttn;
    }

    public void setSaAttn(String saAttn) {
        this.saAttn = saAttn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public String toString() {
        return "ChildDTO{" +
                "cusCode='" + cusCode + '\'' +
                ", clnCode='" + clnCode + '\'' +
                ", address='" + address + '\'' +
                ", location='" + location + '\'' +
                ", province='" + province + '\'' +
                ", contactPer='" + contactPer + '\'' +
                ", saAttn='" + saAttn + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", email='" + email + '\'' +
                ", cusType='" + cusType + '\'' +
                ", addedBy='" + addedBy + '\'' +
                ", addedOn='" + addedOn + '\'' +
                ", id='" + id + '\'' +
                ", district='" + district + '\'' +
                ", joinDate='" + joinDate + '\'' +
                '}';
    }
}