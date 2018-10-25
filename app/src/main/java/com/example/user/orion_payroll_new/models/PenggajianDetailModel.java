package com.example.user.orion_payroll_new.models;

public class PenggajianDetailModel {
    int id, id_master, id_tjg_pot_kas;
    String tipe;
    Double jumlah;
    boolean check;

    public PenggajianDetailModel(int id, int id_master, int id_tjg_pot_kas, String tipe, Double jumlah) {
        this.id = id;
        this.id_master = id_master;
        this.id_tjg_pot_kas = id_tjg_pot_kas;
        this.tipe = tipe;
        this.jumlah = jumlah;
        this.check = false;
    }

    public PenggajianDetailModel() {
        this.id = 0;
        this.id_master = 0;
        this.id_tjg_pot_kas = 0;
        this.tipe = "";
        this.jumlah = 0.0;
        this.check = false;
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
}
