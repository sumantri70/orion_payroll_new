package com.example.user.orion_payroll_new.models;

public class KasbonPegawaiModel {
    private int id;
    private long tanggal;
    private String nomor;
    private int id_pegawai;
    private double jumlah;
    private double sisa;
    private int cicilan;
    private String keterangan;
    private String user_id;
    private long tgl_input;
    private String user_edit;
    private long tgl_edit;

    public KasbonPegawaiModel(int id, long tanggal, String nomor, int id_pegawai, double jumlah, double sisa, int cicilan, String keterangan, String user_id, long tgl_input, String user_edit, long tgl_edit) {
        this.id = id;
        this.tanggal = tanggal;
        this.nomor = nomor;
        this.id_pegawai = id_pegawai;
        this.jumlah = jumlah;
        this.sisa = sisa;
        this.cicilan = cicilan;
        this.keterangan = keterangan;
        this.user_id = user_id;
        this.tgl_input = tgl_input;
        this.user_edit = user_edit;
        this.tgl_edit = tgl_edit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTanggal(long tanggal) {
        this.tanggal = tanggal;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public void setId_pegawai(int id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public void SetSisa(double sisa) {
        this.sisa = sisa;
    }

    public void setCicilan(int cicilan) {
        this.cicilan = cicilan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setSisa(double sisa) {
        this.sisa = sisa;
    }

    public void setUser_edit(String user_edit) {
        this.user_edit = user_edit;
    }

    public void setTgl_input(long tgl_input) {
        this.tgl_input = tgl_input;
    }

    public void setTgl_edit(long tgl_edit) {
        this.tgl_edit = tgl_edit;
    }

    public int getId() {
        return id;
    }

    public long getTanggal() {
        return tanggal;
    }

    public String getNomor() {
        return nomor;
    }

    public int getId_pegawai() {
        return id_pegawai;
    }

    public double getJumlah() {
        return jumlah;
    }

    public double getSisa() {
        return sisa;
    }

    public int getCicilan() {
        return cicilan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_edit() {
        return user_edit;
    }

    public long getTgl_input() {
        return tgl_input;
    }

    public long getTgl_edit() {
        return tgl_edit;
    }
}
