package com.orionit.app.orion_payroll_new.ModelsHelper;

public class PenggajianDetailModel {
    private int id;
    private String tipe_detail;
    private String nama, kode;
    private double jumlah;
    private int lama_cicilan;

    public PenggajianDetailModel(int id, String tipe_detail, String nama, String kode, double jumlah, int lama_cicilan) {
        this.id = id;
        this.tipe_detail = tipe_detail;
        this.nama = nama;
        this.kode = kode;
        this.jumlah = jumlah;
        this.lama_cicilan = lama_cicilan;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipe_detail(String tipe_detail) {
        this.tipe_detail = tipe_detail;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public void setLama_cicilan(int lama_cicilan) {
        this.lama_cicilan = lama_cicilan;
    }

    public int getId() {
        return id;
    }

    public String getTipe_detail() {
        return tipe_detail;
    }

    public String getNama() {
        return nama;
    }

    public String getKode() {
        return kode;
    }

    public double getJumlah() {
        return jumlah;
    }

    public int getLama_cicilan() {
        return lama_cicilan;
    }
}
