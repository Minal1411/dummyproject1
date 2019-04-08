package com.exploretalent.hireme;

public class ListViewInfo_Class {
    private String name;
    private String email;
    private String worktype;
    private String Address;
    private String location;
    private String contactNo;
    private Integer distance;
    private String key;

    public ListViewInfo_Class(String name, String email, String worktype, String address,
                              String location, String contactNo, Integer distance, String key) {
        this.name = name;
        this.email = email;
        this.worktype = worktype;
        Address = address;
        this.location = location;
        this.contactNo = contactNo;
        this.distance = distance;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorktype() {
        return worktype;
    }

    public void setWorktype(String worktype) {
        this.worktype = worktype;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
