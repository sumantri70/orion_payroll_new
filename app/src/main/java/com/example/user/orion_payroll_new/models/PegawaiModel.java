package com.example.user.orion_payroll_new.models;

import java.util.Date;

public class PegawaiModel {
    private int id;
    private String nik, nama, alamat, telpon1, telpon2, email, status;
    private Double gaji_pokok;
    private long tgl_lahir;

    public PegawaiModel(int id, String NIK, String nama, String alamat, String telpon1, String telpon2, String email, Double gaji_pokok, String status, long tgl_lahir) {
        this.id = id;
        this.nik = NIK;
        this.nama = nama;
        this.alamat = alamat;
        this.telpon1 = telpon1;
        this.telpon2 = telpon2;
        this.email = email;
        this.gaji_pokok = gaji_pokok;
        this.status = status;
        this.tgl_lahir = tgl_lahir;
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

    public void setgaji_pokok(Double gaji_pokok) {
        this.gaji_pokok = gaji_pokok;
    }

    public void setStatus(String status) {this.status = status;}

    public void setTgl_lahir(Integer tgl_lahir) {this.tgl_lahir = tgl_lahir; }

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

    public Double getgaji_pokok() {
        return gaji_pokok;
    }

    public String getStatus() { return status; }

    public long getTgl_lahir() {
        return tgl_lahir;
    }

    @Override
    public String toString() {
        return this.nik +" "+ this.nama;
    }
}
