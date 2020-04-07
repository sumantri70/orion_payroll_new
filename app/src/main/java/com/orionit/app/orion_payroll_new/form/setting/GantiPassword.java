package com.orionit.app.orion_payroll_new.form.setting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.database.master.PasswordAplikasiTable;

public class GantiPassword extends AppCompatActivity {
    private TextInputEditText txtPassworLama, txtPasswordBaru, txtUlangiPassworBaru;
    private Button btnUbah;
    private ProgressDialog Loading;
    private PasswordAplikasiTable TData;
    private boolean PasswordAwal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        CreateView();
        InitClass();
        EventClass();
    }

    protected void CreateView(){
        txtPassworLama       = (TextInputEditText) findViewById(R.id.txtPassworLama);
        txtPasswordBaru      = (TextInputEditText) findViewById(R.id.txtPasswordBaru);
        txtUlangiPassworBaru = (TextInputEditText) findViewById(R.id.txtUlangiPassworBaru);
        btnUbah              = (Button) findViewById(R.id.btnUbah);
    }

    protected void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Ganti password");
        Loading = new ProgressDialog(GantiPassword.this);
        TData = new PasswordAplikasiTable(getApplicationContext());
        PasswordAwal = !TData.IsAdaPassword();
        if (PasswordAwal){
            txtPassworLama.setVisibility(View.GONE);
        }
    }

    protected void EventClass(){
        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsValid() == true){
                    if (IsSavedEdit()){
                        Toast.makeText(GantiPassword.this, "Password Berhasil diubah", Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }else{
                        Toast.makeText(GantiPassword.this, "Password gagal diubah", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    protected boolean IsValid(){
        if (!PasswordAwal){
            if (this.txtPassworLama.getText().toString().equals("")) {
                txtPassworLama.requestFocus();
                txtPassworLama.setError("Belum diisi");
                return false;
            }
        }

        if (this.txtPasswordBaru.getText().toString().equals("")) {
            txtPasswordBaru.requestFocus();
            txtPasswordBaru.setError("Belum diisi");
            return false;
        }

        if (this.txtUlangiPassworBaru.getText().toString().equals("")) {
            txtUlangiPassworBaru.requestFocus();
            txtUlangiPassworBaru.setError("Belum diisi");
            return false;
        }

        if (!this.txtPasswordBaru.getText().toString().equals(txtUlangiPassworBaru.getText().toString())) {
            txtUlangiPassworBaru.requestFocus();
            txtUlangiPassworBaru.setError("Tidak sama dengan password baru");
            return false;
        }

        if (!PasswordAwal){
            if (!TData.CekPassword(this.txtPassworLama.getText().toString())){
                txtPassworLama.requestFocus();
                txtPassworLama.setError("Passwor lama salah");
                return false;
            }
        }
        return true;
    }

    protected boolean IsSavedEdit(){
        try {
            TData.Update(this.txtPasswordBaru.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
