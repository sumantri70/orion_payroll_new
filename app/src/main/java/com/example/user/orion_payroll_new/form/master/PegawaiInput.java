package com.example.user.orion_payroll_new.form.master;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.models.PegawaiModel;

import static android.service.autofill.Validators.or;
import static com.example.user.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.example.user.orion_payroll_new.models.JCons.EDIT_MODE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_SAVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_UPDATE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_UPDATE;

public class PegawaiInput extends AppCompatActivity {
    private TextInputEditText txtNik, txtNama, txtAlamat, txtTelpon1, txtTelpon2, txtEmail, txtGajiPokok;
    private Button btnSimpan;

    private PegawaiTable TPegawai;
    public Boolean EditMode;
    private String Mode;
    private int SelectedData;
    private int IdMst;

    protected void CreateView(){
        txtNik       = (TextInputEditText) findViewById(R.id.txtNik);
        txtNama      = (TextInputEditText) findViewById(R.id.txtNama);
        txtAlamat    = (TextInputEditText) findViewById(R.id.txtAlamat);
        txtTelpon1   = (TextInputEditText) findViewById(R.id.txtTelpon1);
        txtTelpon2   = (TextInputEditText) findViewById(R.id.txtTelpon2);
        txtEmail     = (TextInputEditText) findViewById(R.id.txtEmail);
        txtGajiPokok = (TextInputEditText) findViewById(R.id.txtGajiPokok);
        btnSimpan    = (Button) findViewById(R.id.btnSimpan);
    }

    protected void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extra = this.getIntent().getExtras();
        this.Mode = extra.getString("MODE");
        this.SelectedData = extra.getInt("POSITION");

        if (Mode.equals(EDIT_MODE)){
            this.setTitle("Edit Pegawai");
        }else if (Mode.equals(DETAIL_MODE)){
            this.setTitle("Pegawai");
            this.btnSimpan.setVisibility(View.INVISIBLE);
        }else{
            this.setTitle("Input Pegawai");
        };

        boolean Enabled = !Mode.equals(DETAIL_MODE);
        this.txtNik.setEnabled(Enabled);
        this.txtNama.setEnabled(Enabled);
        this.txtTelpon1.setEnabled(Enabled);
        this.txtTelpon2.setEnabled(Enabled);
        this.txtEmail.setEnabled(Enabled);
        this.txtGajiPokok.setEnabled(Enabled);
        this.btnSimpan.setEnabled(Enabled);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pegawai_input);
        CreateView();
        InitClass();
        this.TPegawai = ((OrionPayrollApplication)getApplicationContext()).TPegawai;

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(Mode.equals(EDIT_MODE)){
                IsSavedEdit();
                Toast.makeText(PegawaiInput.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
            }else{
                IsSaved();
                Toast.makeText(PegawaiInput.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ((Mode.equals(EDIT_MODE)) || (Mode.equals(DETAIL_MODE))){
            LoadData();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    protected void LoadData(){
        PegawaiModel Data = this.TPegawai.GetDataKaryawanByIndex(this.SelectedData);
        this.IdMst = Data.getId();
        this.txtNik.setText(Data.getNik());
        this.txtNama.setText(Data.getNama());
        this.txtTelpon1.setText(Data.getTelpon1());
        this.txtTelpon2.setText(Data.getTelpon2());
        this.txtEmail.setText(Data.getEmail());
        this.txtGajiPokok.setText(Double.toString(Data.getGaji_pokokl()));
    }

    protected boolean IsSaved(){
        PegawaiModel Data = new PegawaiModel(0,txtNik.getText().toString(),
                                             txtNama.getText().toString(),
                                             txtAlamat.getText().toString(),
                                             txtTelpon1.getText().toString(),
                                             txtTelpon2.getText().toString(),
                                             txtEmail.getText().toString(),
                                             Double.parseDouble(txtGajiPokok.getText().toString()),
                                             "T"
                                             );
        TPegawai.Insert(Data);
        PegawaiInput.this.onBackPressed();
        return true;
    }

    protected boolean IsSavedEdit(){
        PegawaiModel Data = new PegawaiModel(IdMst,txtNik.getText().toString(),
                                             txtNama.getText().toString(),
                                             txtAlamat.getText().toString(),
                                             txtTelpon1.getText().toString(),
                                             txtTelpon2.getText().toString(),
                                             txtEmail.getText().toString(),
                                             Double.parseDouble(txtGajiPokok.getText().toString()),
                                             "T");
        TPegawai.Update(Data);
        PegawaiInput.this.onBackPressed();
        return true;
    }
}
