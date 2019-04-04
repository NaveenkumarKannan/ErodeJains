package com.yarolegovich.slidingrootnav.sample;

/**
 * Created by SANKAR on 3/30/2018.
 */

public class Blood {
    private String Name, Phone_No, BloodGroup;

    public Blood(String name, String phone_No, String bloodGroup) {
        Name = name;
        Phone_No = phone_No;
        BloodGroup = bloodGroup;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone_No() {
        return Phone_No;
    }

    public void setPhone_No(String phone_No) {
        Phone_No = phone_No;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }
}
