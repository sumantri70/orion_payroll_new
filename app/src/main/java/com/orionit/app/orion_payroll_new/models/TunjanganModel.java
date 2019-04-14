package com.orionit.app.orion_payroll_new.models;

public class TunjanganModel {
    private int id;
    private String kode, nama, keterangan, status, can_delete;
    private double jumlah;

    public TunjanganModel(int id, String kode, String nama, String keterangan, String status, String can_delete) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.keterangan = keterangan;
        this.status = status;
        this.can_delete = can_delete;
    }

    public TunjanganModel() {
        this.id = 0;
        this.kode = "";
        this.nama = "";
        this.keterangan = "";
        this.status = "";
        this.jumlah = 0;
        this.can_delete = "";
    }

    public TunjanganModel(TunjanganModel Data) {
        this.id = Data.getId();
        this.kode = Data.getKode();
        this.nama = Data.getNama();
        this.keterangan = Data.getKeterangan();
        this.status = Data.getStatus();
        this.jumlah = Data.getJumlah();
        this.can_delete = Data.getCan_delete();
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

    public String getCan_delete() {
        return can_delete;
    }

    public void setCan_delete(String can_delete) {
        this.can_delete = can_delete;
    }
}
