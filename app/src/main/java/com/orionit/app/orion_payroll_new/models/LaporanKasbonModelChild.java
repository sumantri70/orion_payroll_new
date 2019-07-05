package com.orionit.app.orion_payroll_new.models;

public class LaporanKasbonModelChild {
    private int id_pegawai;
    private String nomor;
    private Double jumlah;
    private  String tipe;
    private long tanggal;

    public LaporanKasbonModelChild(int id_pegawai, String nomor, Double jumlah, String tipe, long tanggal) {
        this.id_pegawai = id_pegawai;
        this.nomor = nomor;
        this.jumlah = jumlah;
        this.tipe = tipe;
        this.tanggal = tanggal;
    }

    public int getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(int id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public Double getJumlah() {
        return jumlah;
    }

    public void setJumlah(Double jumlah) {
        this.jumlah = jumlah;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public long getTanggal() {
        return tanggal;
    }

    public void setTanggal(long tanggal) {
        this.tanggal = tanggal;
    }
}
