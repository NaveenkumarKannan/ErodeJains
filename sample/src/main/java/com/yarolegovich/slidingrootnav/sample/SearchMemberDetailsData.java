package com.yarolegovich.slidingrootnav.sample;

import android.graphics.Bitmap;

public class SearchMemberDetailsData {
    String headName,headMobile,officeAddress,officePhNo,residenceAddress,residencePhNo;

    public SearchMemberDetailsData(String headName, String headMobile, String officeAddress, String officePhNo, String residenceAddress, String residencePhNo) {
        this.headName = headName;
        this.headMobile = headMobile;
        this.officeAddress = officeAddress;
        this.officePhNo = officePhNo;
        this.residenceAddress = residenceAddress;
        this.residencePhNo = residencePhNo;
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

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getOfficePhNo() {
        return officePhNo;
    }

    public void setOfficePhNo(String officePhNo) {
        this.officePhNo = officePhNo;
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(String residenceAddress) {
        this.residenceAddress = residenceAddress;
    }

    public String getResidencePhNo() {
        return residencePhNo;
    }

    public void setResidencePhNo(String residencePhNo) {
        this.residencePhNo = residencePhNo;
    }
}
