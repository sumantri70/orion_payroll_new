package com.example.user.orion_payroll_new.models;

public class AbsenPegawaiModel {
    private int id, id_pegawai;
    private long tanggal;
    private String jenis_absen, keterangan;
    private int telat_satu, telat_dua, dokter, izin_stgh_hari, izin_cuti, izin_non_cuti, masuk;

    public AbsenPegawaiModel(int id, int id_pegawai, long tanggal, String jenis_absen, String keterangan, int telat_satu, int telat_dua, int dokter, int izin_stgh_hari, int izin_cuti, int izin_non_cuti, int masuk) {
        this.id = id;
        this.id_pegawai = id_pegawai;
        this.tanggal = tanggal;
        this.jenis_absen = jenis_absen;
        this.keterangan = keterangan;
        this.telat_satu = telat_satu;
        this.telat_dua = telat_dua;
        this.dokter = dokter;
        this.izin_stgh_hari = izin_stgh_hari;
        this.izin_cuti = izin_cuti;
        this.izin_non_cuti = izin_non_cuti;
        this.masuk = masuk;
    }

    public AbsenPegawaiModel() {
        this.id = 0;
        this.id_pegawai = 0;
        this.tanggal = 0;
        this.jenis_absen = "";
        this.keterangan = "";
        this.telat_satu = 0;
        this.telat_dua = 0;
        this.dokter = 0;
        this.izin_stgh_hari = 0;
        this.izin_cuti = 0;
        this.izin_non_cuti = 0;
        this.masuk = 0;
    }

    public AbsenPegawaiModel(AbsenPegawaiModel Data) {
        this.id = Data.getId();
        this.id_pegawai = Data.getId_pegawai();
        this.tanggal = Data.getTanggal();
        this.jenis_absen = Data.getJenis_absen();
        this.keterangan = Data.getKeterangan();
        this.telat_satu = Data.getTelat_satu();
        this.telat_dua = Data.getTelat_dua();
        this.dokter = getDokter();
        this.izin_stgh_hari = getIzin_stgh_hari();
        this.izin_cuti = getIzin_cuti();
        this.izin_non_cuti = getIzin_non_cuti();
        this.masuk = getMasuk();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_pegawai(int id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public void setJenis_absen(String jenis_absen) {
        this.jenis_absen = jenis_absen;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setTelat_satu(int telat_satu) {
        this.telat_satu = telat_satu;
    }

    public void setTelat_dua(int telat_dua) {
        this.telat_dua = telat_dua;
    }

    public void setDokter(int dokter) {
        this.dokter = dokter;
    }

    public void setIzin_stgh_hari(int izin_stgh_hari) {
        this.izin_stgh_hari = izin_stgh_hari;
    }

    public void setIzin_cuti(int izin_cuti) {
        this.izin_cuti = izin_cuti;
    }

    public void setIzin_non_cuti(int izin_non_cuti) {
        this.izin_non_cuti = izin_non_cuti;
    }

    public int getId() {
        return id;
    }

    public int getId_pegawai() {
        return id_pegawai;
    }

    public String getJenis_absen() {
        return jenis_absen;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public int getTelat_satu() {
        return telat_satu;
    }

    public int getTelat_dua() {
        return telat_dua;
    }

    public int getDokter() {
        return dokter;
    }

    public int getIzin_stgh_hari() {
        return izin_stgh_hari;
    }

    public int getIzin_cuti() {
        return izin_cuti;
    }

    public int getIzin_non_cuti() {
        return izin_non_cuti;
    }

    public long getTanggal() {
        return tanggal;
    }

    public void setTanggal(long tanggal) {
        this.tanggal = tanggal;
    }

    public int getMasuk() {
        return masuk;
    }

    public void setMasuk(int masuk) {
        this.masuk = masuk;
    }
}
