package com.example.user.orion_payroll_new.form.master;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;

public class TunjanganInput extends AppCompatActivity {
    private TextInputEditText txtKode, txtNama, txtKeterangan;
    private Button btnSimpan;

    private PegawaiTable TTunjangan;
    public Boolean EditMode;
    private String Mode;
    private int SelectedData;
    private int IdMst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tunjangan_input);
    }
}
