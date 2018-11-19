package com.example.user.orion_payroll_new.models;

public class PenggajianModel {
    private int id, id_pegawai, telat_satu, telat_dua, dokter, izin_stgh_hari, izin_cuti, izin_non_cuti;
    private String nomor, keterangan, user_id, user_edit, nama_pegawai;
    private Double gaji_pokok, uang_ikatan, uang_kehadiran, premi_harian, premi_perjam, jam_lembur, total_tunjangan, total_potongan, total_lembur, total_kasbon, total;
    private long tanggal, tgl_input, tgl_edit, periode;
    private boolean pilih;

    public PenggajianModel(int id, int id_pegawai, int telat_satu, int telat_dua, int dokter, int izin_stgh_hari, int izin_cuti, int izin_non_cuti, String nomor, String keterangan, String user_id, String user_edit, Double gaji_pokok, Double uang_ikatan, Double uang_kehadiran, Double premi_harian, Double premi_perjam, Double jam_lembur, Double total_tunjangan, Double total_potongan, Double total_lembur, Double total_kasbon, Double total, long tanggal, long tgl_input, long tgl_edit, long periode) {
        this.id = id;
        this.id_pegawai = id_pegawai;
        this.telat_satu = telat_satu;
        this.telat_dua = telat_dua;
        this.dokter = dokter;
        this.izin_stgh_hari = izin_stgh_hari;
        this.izin_cuti = izin_cuti;
        this.izin_non_cuti = izin_non_cuti;
        this.nomor = nomor;
        this.keterangan = keterangan;
        this.user_id = user_id;
        this.user_edit = user_edit;
        this.gaji_pokok = gaji_pokok;
        this.uang_ikatan = uang_ikatan;
        this.uang_kehadiran = uang_kehadiran;
        this.premi_harian = premi_harian;
        this.premi_perjam = premi_perjam;
        this.jam_lembur = jam_lembur;
        this.total_tunjangan = total_tunjangan;
        this.total_potongan = total_potongan;
        this.total_lembur = total_lembur;
        this.total_kasbon = total_kasbon;
        this.total = total;
        this.tanggal = tanggal;
        this.tgl_input = tgl_input;
        this.tgl_edit = tgl_edit;
        this.nama_pegawai = "";
        this.periode = periode;
        this.pilih = false;
    }

    public PenggajianModel() {
        this.id = 0;
        this.id_pegawai = 0;
        this.telat_satu = 0;
        this.telat_dua = 0;
        this.dokter = 0;
        this.izin_stgh_hari = 0;
        this.izin_cuti = 0;
        this.izin_non_cuti = 0;
        this.nomor = "";
        this.keterangan = "";
        this.user_id = "";
        this.user_edit = "";
        this.gaji_pokok = 0.0;
        this.uang_ikatan = 0.0;
        this.uang_kehadiran = 0.0;
        this.premi_harian = 0.0;
        this.premi_perjam = 0.0;
        this.jam_lembur = 0.0;
        this.total_tunjangan = 0.0;
        this.total_potongan = 0.0;
        this.total_lembur = 0.0;
        this.total_kasbon = 0.0;
        this.total = 0.0;
        this.tanggal = 0;
        this.tgl_input = 0;
        this.tgl_edit = 0;
        this.periode = 0;
        this.pilih = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_pegawai(int id_pegawai) {
        this.id_pegawai = id_pegawai;
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

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_edit(String user_edit) {
        this.user_edit = user_edit;
    }

    public void setGaji_pokok(Double gaji_pokok) {
        this.gaji_pokok = gaji_pokok;
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

    public void setJam_lembur(Double jam_lembur) {
        this.jam_lembur = jam_lembur;
    }

    public void setTotal_kasbon(Double total_kasbon) {
        this.total_kasbon = total_kasbon;
    }

    public void setTotal_tunjangan(Double total_tunjangan) {
        this.total_tunjangan = total_tunjangan;
    }

    public void setTotal_potongan(Double total_potongan) {
        this.total_potongan = total_potongan;
    }

    public void setTotal_lembur(Double total_lembur) {
        this.total_lembur = total_lembur;
    }

    public void setTanggal(long tanggal) {
        this.tanggal = tanggal;
    }

    public void setTgl_input(long tgl_input) {
        this.tgl_input = tgl_input;
    }

    public void setTgl_edit(long tgl_edit) {
        this.tgl_edit = tgl_edit;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setNama_pegawai(String nama_pegawai) {
        this.nama_pegawai = nama_pegawai;
    }

    public void setPeriode(long periode) {
        this.periode = periode;
    }

    public int getId() {
        return id;
    }

    public int getId_pegawai() {
        return id_pegawai;
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

    public String getNomor() {
        return nomor;
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

    public Double getGaji_pokok() {
        return gaji_pokok;
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

    public Double getJam_lembur() {
        return jam_lembur;
    }

    public Double getTotal_tunjangan() {
        return total_tunjangan;
    }

    public Double getTotal_potongan() {
        return total_potongan;
    }

    public Double getTotal_lembur() {
        return total_lembur;
    }

    public long getTanggal() {
        return tanggal;
    }

    public long getTgl_input() {
        return tgl_input;
    }

    public long getTgl_edit() {
        return tgl_edit;
    }

    public Double getTotal() {
        return total;
    }

    public Double getTotal_kasbon() {
        return total_kasbon;
    }

    public String getNama_pegawai() {
        return nama_pegawai;
    }

    public long getPeriode() {
        return periode;
    }

    public void setPilih(boolean pilih) {
        this.pilih = pilih;
    }

    public boolean isPilih() {
        return pilih;
    }
}
