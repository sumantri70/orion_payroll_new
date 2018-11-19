package com.example.user.orion_payroll_new.models;

public class LaporanKasbonModel {
    private String nomor;
    private long tanggal;
    private int id_pegawai;
    private double jumlah;
    private int cicilan;
    private double terbayar;
    private double sisa;

    public LaporanKasbonModel(String nomor, long tanggal, int id_pegawai, double jumlah, int cicilan, double terbayar, double sisa) {
        this.nomor = nomor;
        this.tanggal = tanggal;
        this.id_pegawai = id_pegawai;
        this.jumlah = jumlah;
        this.cicilan = cicilan;
        this.terbayar = terbayar;
        this.sisa = sisa;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public void setTanggal(long tanggal) {
        this.tanggal = tanggal;
    }

    public void setId_pegawai(int id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public void setCicilan(int cicilan) {
        this.cicilan = cicilan;
    }

    public void setTerbayar(double terbayar) {
        this.terbayar = terbayar;
    }

    public void setSisa(double sisa) {
        this.sisa = sisa;
    }

    public long getTanggal() {
        return tanggal;
    }

    public int getId_pegawai() {
        return id_pegawai;
    }

    public double getJumlah() {
        return jumlah;
    }

    public int getCicilan() {
        return cicilan;
    }

    public double getTerbayar() {
        return terbayar;
    }

    public double getSisa() {
        return sisa;
    }

    public String getNomor() {
        return nomor;
    }
}
