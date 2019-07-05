package com.orionit.app.orion_payroll_new.models;

public class SaldoKasbonModel {
    private int id_pegawai;
    private Double saldo;

    public SaldoKasbonModel(int id_pegawai, Double saldo) {
        this.id_pegawai = id_pegawai;
        this.saldo = saldo;
    }

    public SaldoKasbonModel() {
        this.id_pegawai = 0;
        this.saldo = 0.0;
    }

    public int getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(int id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
}
