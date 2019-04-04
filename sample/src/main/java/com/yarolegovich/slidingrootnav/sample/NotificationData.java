package com.yarolegovich.slidingrootnav.sample;

import android.graphics.Bitmap;

public class NotificationData {
    String title,message,user_name,date;
    Bitmap bitmap;

    public NotificationData(String title, String message, String user_name, String date, Bitmap bitmap) {
        this.title = title;
        this.message = message;
        this.user_name = user_name;
        this.date = date;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
