package com.yarolegovich.slidingrootnav.sample;

import android.graphics.Bitmap;

public class ViewDetailsData {
    Bitmap headPhoto;
    String headName,headMobile,occupation,nativePlace,headEmail,id;

    public ViewDetailsData(Bitmap headPhoto, String headName, String headMobile, String occupation, String nativePlace, String headEmail, String id) {
        this.headPhoto = headPhoto;
        this.headName = headName;
        this.headMobile = headMobile;
        this.occupation = occupation;
        this.nativePlace = nativePlace;
        this.headEmail = headEmail;
        this.id = id;
    }

    public Bitmap getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(Bitmap headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getHeadMobile() {
        return headMobile;
    }

    public void setHeadMobile(String headMobile) {
        this.headMobile = headMobile;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getHeadEmail() {
        return headEmail;
    }

    public void setHeadEmail(String headEmail) {
        this.headEmail = headEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
