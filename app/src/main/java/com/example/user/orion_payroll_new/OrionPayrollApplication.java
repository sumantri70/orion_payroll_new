package com.example.user.orion_payroll_new;

import android.app.Application;

import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.database.master.TunjanganTable;
import com.example.user.orion_payroll_new.models.PegawaiModel;
import com.example.user.orion_payroll_new.utility.EngineGeneral;

import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;

public class OrionPayrollApplication extends Application {
    public PegawaiTable TPegawai;
    public TunjanganTable TTunjangan;

    @Override
    public void onCreate() {
        super.onCreate();
        this.TPegawai = new PegawaiTable(getApplicationContext(), TRUE_STRING, "NIK");
        this.TTunjangan = new TunjanganTable(getApplicationContext());
    }
}
