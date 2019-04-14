package com.orionit.app.orion_payroll_new.models;

public class DetailTunjanganPegawaiModel {
    private int id, id_pegawai, id_tunjangan;
    private double jumlah;

    public DetailTunjanganPegawaiModel(int id, int id_pegawai, int id_tunjangan, double jumlah) {
        this.id = id;
        this.id_pegawai = id_pegawai;
        this.id_tunjangan = id_tunjangan;
        this.jumlah = jumlah;
    }

    public DetailTunjanganPegawaiModel() {
        this.id = 0;
        this.id_pegawai = 0;
        this.id_tunjangan = 0;
        this.jumlah = 0.0;
    }

    public DetailTunjanganPegawaiModel(DetailTunjanganPegawaiModel Data) {
        this.id = Data.getId();
        this.id_pegawai = Data.getId_pegawai();
        this.id_tunjangan = Data.getId_tunjangan();
        this.jumlah = Data.getJumlah();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_pegawai(int id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public void setId_tunjangan(int id_tunjangan) {
        this.id_tunjangan = id_tunjangan;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public int getId() {
        return id;
    }

    public int getId_pegawai() {
        return id_pegawai;
    }

    public int getId_tunjangan() {
        return id_tunjangan;
    }

    public double getJumlah() {
        return jumlah;
    }
}
