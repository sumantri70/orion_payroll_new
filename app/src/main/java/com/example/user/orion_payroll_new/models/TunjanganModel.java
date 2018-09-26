package com.example.user.orion_payroll_new.models;

public class TunjanganModel {
    private int id;
    private String kode, nama, keterangan, status;
    private double jumlah;

    public TunjanganModel(int id, String kode, String nama, String keterangan, String status) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.keterangan = keterangan;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getStatus() {
        return status;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    @Override
    public String toString() {
        return this.kode +" "+ this.nama;
    }
}
