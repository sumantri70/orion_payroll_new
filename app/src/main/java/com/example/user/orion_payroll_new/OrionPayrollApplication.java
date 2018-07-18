package com.example.user.orion_payroll_new;

import android.app.Application;

import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.database.master.TunjanganTable;
import com.example.user.orion_payroll_new.models.PegawaiModel;

public class OrionPayrollApplication extends Application {
    public PegawaiTable TPegawai;
    public TunjanganTable TTunjangan;

    @Override
    public void onCreate() {
        super.onCreate();
        this.TPegawai = new PegawaiTable(getApplicationContext());
        this.TTunjangan = new TunjanganTable(getApplicationContext());
    }
}
