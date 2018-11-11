package com.example.user.orion_payroll_new.models;

public class LaporanPenggajianModel {
    private long periode;
    private double gaji_pokok;
    private double tunjangan;
    private double potongan;
    private double kasbon;
    private double lembur;
    private double total;


    public LaporanPenggajianModel(long periode, double gaji_pokok, double tunjangan, double potongan, double kasbon, double lembur, double total) {
        this.periode = periode;
        this.gaji_pokok = gaji_pokok;
        this.tunjangan = tunjangan;
        this.potongan = potongan;
        this.kasbon = kasbon;
        this.lembur = lembur;
        this.total = total;
    }

    public LaporanPenggajianModel() {
        this.periode = 0;
        this.gaji_pokok = 0.0;
        this.tunjangan = 0.0;
        this.potongan = 0.0;
        this.kasbon = 0.0;
        this.lembur = 0.0;
        this.total = 0.0;
    }

    public LaporanPenggajianModel(LaporanPenggajianModel Data) {
        this.periode = Data.getPeriode();
        this.gaji_pokok = Data.getGaji_pokok();
        this.tunjangan = Data.getTunjangan();
        this.potongan = getPotongan();
        this.kasbon = getKasbon();
        this.lembur = getLembur();
        this.total = getTotal();
    }


    public void setPeriode(long periode) {
        this.periode = periode;
    }

    public void setGaji_pokok(double gaji_pokok) {
        this.gaji_pokok = gaji_pokok;
    }

    public void setTunjangan(double tunjangan) {
        this.tunjangan = tunjangan;
    }

    public void setPotongan(double potongan) {
        this.potongan = potongan;
    }

    public void setKasbon(double kasbon) {
        this.kasbon = kasbon;
    }

    public void setLembur(double lembur) {
        this.lembur = lembur;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public long getPeriode() {
        return periode;
    }

    public double getGaji_pokok() {
        return gaji_pokok;
    }

    public double getTunjangan() {
        return tunjangan;
    }

    public double getPotongan() {
        return potongan;
    }

    public double getKasbon() {
        return kasbon;
    }

    public double getLembur() {
        return lembur;
    }

    public double getTotal() {
        return total;
    }
}
