package com.example.user.orion_payroll_new.models;

import java.util.Date;

public class PegawaiModel {
    private int id;
    private String nik, nama, alamat, telpon1, telpon2, email, status;
    private Double gaji_pokokl;

    public PegawaiModel(int id, String NIK, String nama, String alamat, String telpon1, String telpon2, String email, Double gaji_pokokl, String status) {
        this.id = id;
        this.nik = NIK;
        this.nama = nama;
        this.alamat = alamat;
        this.telpon1 = telpon1;
        this.telpon2 = telpon2;
        this.email = email;
        this.gaji_pokokl = gaji_pokokl;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setTelpon1(String telpon1) {
        this.telpon1 = telpon1;
    }

    public void setTelpon2(String telpon2) {
        this.telpon2 = telpon2;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGaji_pokokl(Double gaji_pokokl) {
        this.gaji_pokokl = gaji_pokokl;
    }

    public void setStatus(String status) {this.status = status;}

    public int getId() {
        return id;
    }

    public String getNik() {
        return nik;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTelpon1() {
        return telpon1;
    }

    public String getTelpon2() {
        return telpon2;
    }

    public String getEmail() {
        return email;
    }

    public Double getGaji_pokokl() {
        return gaji_pokokl;
    }

    public String getStatus() { return status; }

    @Override
    public String toString() {
        return this.nik +" "+ this.nama;
    }
}
