package com.example.user.orion_payroll_new.models;

import java.util.Date;

public class PegawaiModel {
    private int id;
    private String nik, nama, alamat, telpon1, telpon2, email, status, keterangan;
    private Double gaji_pokok;
    private long tgl_lahir, tgl_mulai_kerja;

    public PegawaiModel(int id, String NIK, String nama, String alamat, String telpon1, String telpon2, String email,
                        Double gaji_pokok, String status, long tgl_lahir, long tgl_mulai_kerja, String keterangan) {
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
        this.keterangan = keterangan;
    }

    public PegawaiModel(){
        this.id = 0;
        this.nik = "";
        this.nama = "";
        this.alamat = "";
        this.telpon1 = "";
        this.telpon2 = "";
        this.email = "";
        this.gaji_pokok = 0.0;
        this.status = "";
        this.tgl_lahir = 0;
        this.keterangan = "";
    }


    public PegawaiModel(PegawaiModel data){
        this.id = data.getId();
        this.nik = data.getNik();
        this.nama = data.getNama();
        this.alamat = data.getAlamat();
        this.telpon1 = data.getTelpon1();
        this.telpon2 = data.getTelpon2();
        this.email = data.getEmail();
        this.gaji_pokok = data.getGaji_pokok();
        this.status = data.getStatus();
        this.tgl_lahir = data.getTgl_lahir();
        this.keterangan = data.getKeterangan();
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setGaji_pokok(Double gaji_pokok) {
        this.gaji_pokok = gaji_pokok;
    }

    public void setTgl_lahir(long tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    public void setTgl_mulai_kerja(long tgl_mulai_kerja) {
        this.tgl_mulai_kerja = tgl_mulai_kerja;
    }

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

    public String getStatus() {
        return status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public Double getGaji_pokok() {
        return gaji_pokok;
    }

    public long getTgl_lahir() {
        return tgl_lahir;
    }

    public long getTgl_mulai_kerja() {
        return tgl_mulai_kerja;
    }

    @Override
    public String toString() {
        return this.nik +" "+ this.nama;
    }
}
