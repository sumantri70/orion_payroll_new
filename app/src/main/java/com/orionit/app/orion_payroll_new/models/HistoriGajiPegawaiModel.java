package com.orionit.app.orion_payroll_new.models;

public class HistoriGajiPegawaiModel {
    private int id, id_pegawai;
    private long tanggal;
    private double gaji_pokok, uang_ikatan, uang_kehadiran, premi_harian, premi_perjam;

    public HistoriGajiPegawaiModel(int id, int id_pegawai, long tanggal, double gaji_pokok, double uang_ikatan, double uang_kehadiran, double premi_harian, double premi_perjam) {
        this.id = id;
        this.id_pegawai = id_pegawai;
        this.tanggal = tanggal;
        this.gaji_pokok = gaji_pokok;
        this.uang_ikatan = uang_ikatan;
        this.uang_kehadiran = uang_kehadiran;
        this.premi_harian = premi_harian;
        this.premi_perjam = premi_perjam;
    }

    public HistoriGajiPegawaiModel() {
        this.id = 0;
        this.id_pegawai = 0;
        this.tanggal = 0;
        this.gaji_pokok = 0.0;
        this.uang_ikatan = 0.0;
        this.uang_kehadiran = 0.0;
        this.premi_harian = 0.0;
        this.premi_perjam = 0.0;
    }

    public HistoriGajiPegawaiModel(HistoriGajiPegawaiModel Data) {
        this.id = Data.getId();
        this.id_pegawai = Data.getId_pegawai();
        this.tanggal = Data.getTanggal();
        this.gaji_pokok = Data.getGaji_pokok();
        this.uang_ikatan = Data.getUang_ikatan();
        this.uang_kehadiran = Data.getUang_kehadiran();
        this.premi_harian = Data.getPremi_harian();
        this.premi_perjam = Data.getPremi_perjam();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_pegawai(int id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public void setTanggal(long tanggal) {
        this.tanggal = tanggal;
    }

    public void setGaji_pokok(double gaji_pokok) {
        this.gaji_pokok = gaji_pokok;
    }

    public void setUang_ikatan(double uang_ikatan) {
        this.uang_ikatan = uang_ikatan;
    }

    public void setUang_kehadiran(double uang_kehadiran) {
        this.uang_kehadiran = uang_kehadiran;
    }

    public void setPremi_harian(double premi_harian) {
        this.premi_harian = premi_harian;
    }

    public void setPremi_perjam(double premi_perjam) {
        this.premi_perjam = premi_perjam;
    }

    public int getId() {
        return id;
    }

    public int getId_pegawai() {
        return id_pegawai;
    }

    public long getTanggal() {
        return tanggal;
    }

    public double getGaji_pokok() {
        return gaji_pokok;
    }

    public double getUang_ikatan() {
        return uang_ikatan;
    }

    public double getUang_kehadiran() {
        return uang_kehadiran;
    }

    public double getPremi_harian() {
        return premi_harian;
    }

    public double getPremi_perjam() {
        return premi_perjam;
    }
}
