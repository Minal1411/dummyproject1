package com.exploretalent.hireme;

import android.net.Uri;

public class Information_Class{

    String name, worktype,address,contact,location,email;
    //email;

    public Information_Class()
    {

    }
    //constructor for service provider
    public Information_Class(String email,String name, String worktype, String address, String contact, String location) {
        this.name = name;
        this.worktype = worktype;
        this.address = address;
        this.contact = contact;
        this.location = location;
        this.email=email;
        //this.imageUri = imageUri;
    }
    //for customer constructor
    public Information_Class(String email,String name, String address, String contact, String location) {

        this.name = name;
        this.address = address;
        this.contact = contact;
        this.location = location;
        this.email=email;
        //this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorktype() {
        return worktype;
    }

    public void setWorktype(String worktype) {
        this.worktype = worktype;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

   /* public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
*/

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
