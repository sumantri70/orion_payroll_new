package com.example.user.orion_payroll_new.models;

public class PenggajianDetailModel {
    int id, id_master, id_tjg_pot_kas, lama_cicilan;
    String tipe, nomor;
    Double jumlah, sisa, total;
    boolean check;
    long tanggal;


    public PenggajianDetailModel(int id, int id_master, int id_tjg_pot_kas, String tipe, Double jumlah) {
        this.id = id;
        this.id_master = id_master;
        this.id_tjg_pot_kas = id_tjg_pot_kas;
        this.tipe = tipe;
        this.jumlah = jumlah;
        this.check = false;
        this.nomor = "";
        this.tanggal = 0;
        this.lama_cicilan = 0;
        this.total = 0.0;
    }

    public PenggajianDetailModel() {
        this.id = 0;
        this.id_master = 0;
        this.id_tjg_pot_kas = 0;
        this.tipe = "";
        this.jumlah = 0.0;
        this.check = false;
        this.sisa = 0.0;
        this.nomor = "";
        this.tanggal = 0;
        this.lama_cicilan = 0;
        this.total = 0.0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_master(int id_master) {
        this.id_master = id_master;
    }

    public void setId_tjg_pot_kas(int id_tjg_pot_kas) {
        this.id_tjg_pot_kas = id_tjg_pot_kas;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public void setJumlah(Double jumlah) {
        this.jumlah = jumlah;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public void setSisa(Double sisa) {
        this.sisa = sisa;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public void setTanggal(long tanggal) {
        this.tanggal = tanggal;
    }

    public void setLama_cicilan(int lama_cicilan) {
        this.lama_cicilan = lama_cicilan;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public int getId_master() {
        return id_master;
    }

    public int getId_tjg_pot_kas() {
        return id_tjg_pot_kas;
    }

    public String getTipe() {
        return tipe;
    }

    public Double getJumlah() {
        return jumlah;
    }

    public boolean isCheck() {
        return check;
    }

    public Double getSisa() {
        return sisa;
    }

    public String getNomor() {
        return nomor;
    }

    public long getTanggal() {
        return tanggal;
    }

    public int getLama_cicilan() {
        return lama_cicilan;
    }

    public Double getTotal() {
        return total;
    }
}
