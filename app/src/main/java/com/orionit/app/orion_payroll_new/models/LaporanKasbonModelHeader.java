package com.orionit.app.orion_payroll_new.models;

public class LaporanKasbonModelHeader {
    private int id_pegawai;
    private Double saldo_awal;
    private Double saldo_akhir;

    public LaporanKasbonModelHeader(int id_pegawai, Double saldo_awal, Double saldo_akhir) {
        this.id_pegawai = id_pegawai;
        this.saldo_awal = saldo_awal;
        this.saldo_akhir = saldo_akhir;
    }

    public int getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(int id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public Double getSaldo_awal() {
        return saldo_awal;
    }

    public void setSaldo_awal(Double saldo_awal) {
        this.saldo_awal = saldo_awal;
    }

    public Double getSaldo_akhir() {
        return saldo_akhir;
    }

    public void setSaldo_akhir(Double saldo_akhir) {
        this.saldo_akhir = saldo_akhir;
    }
}
