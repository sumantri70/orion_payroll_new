package com.orionit.app.orion_payroll_new.models;

import java.util.ArrayList;

public class SettingDBModel {
    private String AccessKey;
    private String AccessSecret;
    private String Password;

    public SettingDBModel(String accessKey, String accessSecret, String password) {
        AccessKey = accessKey;
        AccessSecret = accessSecret;
        Password = password;
    }

    public SettingDBModel() {
        this.AccessKey = "";
        this.AccessSecret = "";
        this.Password = "";
    }


    public String getAccessKey() {
        return AccessKey;
    }

    public void setAccessKey(String accessKey) {
        AccessKey = accessKey;
    }

    public String getAccessSecret() {
        return AccessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        AccessSecret = accessSecret;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
