package com.orionit.app.orion_payroll_new.models;

public class EmailModel {
    private String alamat_email, password;
    private int id;

    public EmailModel(int id, String alamat_email, String password) {
        this.id = id;
        this.alamat_email = alamat_email;
        this.password = password;
    }

    public EmailModel() {
        this.id = 0;
        this.alamat_email = "";
        this.password = "";
    }

    public String getAlamat_email() {
        return alamat_email;
    }

    public void setAlamat_email(String alamat_email) {
        this.alamat_email = alamat_email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
