package com.orionit.app.orion_payroll_new.models;

public class PegawaiModel {
    private int id;
    private String nik, nama, alamat, telpon1, telpon2, email, status, keterangan, no_telpon_darurat;
    private Double gaji_pokok;
    private long tgl_lahir, tgl_mulai_kerja;
    private Double uang_ikatan, uang_kehadiran, premi_harian, premi_perjam;

    public PegawaiModel(int id, String NIK, String nama, String alamat, String telpon1, String telpon2, String email,
                        Double gaji_pokok, String status, long tgl_lahir, long tgl_mulai_kerja, String keterangan,
                        Double uang_ikatan, Double uang_kehadiran, Double premi_harian, Double premi_perjam, String no_telpon_darurat) {
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
        this.tgl_mulai_kerja = tgl_mulai_kerja;
        this.keterangan = keterangan;
        this.uang_ikatan = uang_ikatan;
        this.uang_kehadiran = uang_kehadiran;
        this.premi_harian = premi_harian;
        this.premi_perjam = premi_perjam;
        this.no_telpon_darurat = no_telpon_darurat;
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
        this.uang_ikatan = 0.0;
        this.uang_kehadiran = 0.0;
        this.premi_harian = 0.0;
        this.premi_perjam = 0.0;
        this.no_telpon_darurat = "";
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
        this.uang_ikatan = data.getUang_ikatan();
        this.uang_kehadiran = data.getUang_kehadiran();
        this.premi_harian = data.getPremi_harian();
        this.premi_perjam = data.getPremi_perjam();
        this.premi_perjam = data.getPremi_perjam();
        this.no_telpon_darurat = data.getNo_telpon_darurat();
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

    public void setUang_ikatan(Double uang_ikatan) {
        this.uang_ikatan = uang_ikatan;
    }

    public void setUang_kehadiran(Double uang_kehadiran) {
        this.uang_kehadiran = uang_kehadiran;
    }

    public void setPremi_harian(Double premi_harian) {
        this.premi_harian = premi_harian;
    }

    public void setPremi_perjam(Double premi_perjam) {
        this.premi_perjam = premi_perjam;
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

    public Double getUang_ikatan() {
        return uang_ikatan;
    }

    public Double getUang_kehadiran() {
        return uang_kehadiran;
    }

    public Double getPremi_harian() {
        return premi_harian;
    }

    public Double getPremi_perjam() {
        return premi_perjam;
    }

    public String getNo_telpon_darurat() {
        return no_telpon_darurat;
    }

    public void setNo_telpon_darurat(String no_telpon_darurat) {
        this.no_telpon_darurat = no_telpon_darurat;
    }

    @Override
    public String toString() {
        return this.nik +" "+ this.nama;
    }
}
