package com.yarolegovich.slidingrootnav.sample;

import android.graphics.Bitmap;

public class FamilyDetailsData {
    Bitmap childPhoto;
    String child_name,child_phone_no,child_hus_or_wife_name,
            grand_son_or_daug_name1,grand_son_or_daug_name2,grand_son_or_daug_name3,grand_son_or_daug_name4;

    public FamilyDetailsData(Bitmap childPhoto, String child_name, String child_phone_no, String child_hus_or_wife_name, String grand_son_or_daug_name1, String grand_son_or_daug_name2, String grand_son_or_daug_name3, String grand_son_or_daug_name4) {
        this.childPhoto = childPhoto;
        this.child_name = child_name;
        this.child_phone_no = child_phone_no;
        this.child_hus_or_wife_name = child_hus_or_wife_name;
        this.grand_son_or_daug_name1 = grand_son_or_daug_name1;
        this.grand_son_or_daug_name2 = grand_son_or_daug_name2;
        this.grand_son_or_daug_name3 = grand_son_or_daug_name3;
        this.grand_son_or_daug_name4 = grand_son_or_daug_name4;
    }

    public Bitmap getChildPhoto() {
        return childPhoto;
    }

    public void setChildPhoto(Bitmap childPhoto) {
        this.childPhoto = childPhoto;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public String getChild_phone_no() {
        return child_phone_no;
    }

    public void setChild_phone_no(String child_phone_no) {
        this.child_phone_no = child_phone_no;
    }

    public String getChild_hus_or_wife_name() {
        return child_hus_or_wife_name;
    }

    public void setChild_hus_or_wife_name(String child_hus_or_wife_name) {
        this.child_hus_or_wife_name = child_hus_or_wife_name;
    }

    public String getGrand_son_or_daug_name1() {
        return grand_son_or_daug_name1;
    }

    public void setGrand_son_or_daug_name1(String grand_son_or_daug_name1) {
        this.grand_son_or_daug_name1 = grand_son_or_daug_name1;
    }

    public String getGrand_son_or_daug_name2() {
        return grand_son_or_daug_name2;
    }

    public void setGrand_son_or_daug_name2(String grand_son_or_daug_name2) {
        this.grand_son_or_daug_name2 = grand_son_or_daug_name2;
    }

    public String getGrand_son_or_daug_name3() {
        return grand_son_or_daug_name3;
    }

    public void setGrand_son_or_daug_name3(String grand_son_or_daug_name3) {
        this.grand_son_or_daug_name3 = grand_son_or_daug_name3;
    }

    public String getGrand_son_or_daug_name4() {
        return grand_son_or_daug_name4;
    }

    public void setGrand_son_or_daug_name4(String grand_son_or_daug_name4) {
        this.grand_son_or_daug_name4 = grand_son_or_daug_name4;
    }
}
