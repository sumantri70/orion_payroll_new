package com.orionit.app.orion_payroll_new.form.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.form.adapter.SettingApplikasiAdapater;

import java.util.ArrayList;
import java.util.List;

public class SettingAplikasi extends AppCompatActivity {
    private RecyclerView RcvRekap;
    private List<String> ListSetting;
    private SettingApplikasiAdapater mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_aplikasi);
        CreateVew();
        InitClass();
        IsiSetting();
    }

    private void CreateVew(){
        this.RcvRekap = (RecyclerView) findViewById(R.id.RcvRekap);
    }

    private void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Setting");
        ListSetting = new ArrayList<String>();
        //RcvRekap.addItemDecoration(new DividerItemDecoration(RcvRekap.getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new SettingApplikasiAdapater(SettingAplikasi.this, ListSetting);
        RcvRekap.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RcvRekap.setAdapter(mAdapter);
    }

    private void IsiSetting (){
        ListSetting.add("Database");
        ListSetting.add("Email Pengirim");
        ListSetting.add("Ganti Password");
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

