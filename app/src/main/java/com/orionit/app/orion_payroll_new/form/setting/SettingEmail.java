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
import com.orionit.app.orion_payroll_new.database.master.EmailTable;
import com.orionit.app.orion_payroll_new.models.EmailModel;

import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SUCCESS_SAVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SUCCESS_UPDATE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_UNSUCCESS_SAVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_UNSUCCESS_UPDATE;

public class SettingEmail extends AppCompatActivity {
    private TextInputEditText txtEmail, txtPassword;
    private Button btnSimpan;
    private int IdMst;
    private ProgressDialog Loading;
    private EmailTable TData;
    private boolean AdaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_email);
        CreateView();
        InitClass();
        EventClass();
        LoadData();
    }

    protected void CreateView(){
        txtEmail      = (TextInputEditText) findViewById(R.id.txtEmail);
        txtPassword   = (TextInputEditText) findViewById(R.id.txtPassword);
        btnSimpan     = (Button) findViewById(R.id.btnSimpan);
    }

    protected void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Loading = new ProgressDialog(SettingEmail.this);
        TData = new EmailTable(getApplicationContext());
        this.setTitle("Email Pengirim");
    }

    protected void EventClass(){
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsValid() == true){
                    if(AdaData){
                        if (IsSavedEdit()){
                            Toast.makeText(SettingEmail.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }else{
                            Toast.makeText(SettingEmail.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if (IsSaved()){
                            Toast.makeText(SettingEmail.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }else{
                            Toast.makeText(SettingEmail.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                        };
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
        if (this.txtEmail.getText().toString().equals("")) {
            txtEmail.requestFocus();
            txtEmail.setError("Email belum diisi");
            return false;
        }

        if (this.txtPassword.getText().toString().equals("")) {
            txtPassword.requestFocus();
            txtPassword.setError("Password belum diisi");
            return false;
        }
        return true;
    }

    protected boolean IsSaved(){
        try {
            EmailModel Data = new EmailModel(
                    0,txtEmail.getText().toString(),
                    txtPassword.getText().toString());
            TData.Insert(Data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean IsSavedEdit(){
        try {
            EmailModel Data = new EmailModel(
                    IdMst,
                    txtEmail.getText().toString(),
                    txtPassword.getText().toString());
            TData.Update(Data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected void LoadData(){
        EmailModel Data = TData.GetData(1);
        txtEmail.setText(Data.getAlamat_email());
        txtPassword.setText(Data.getPassword());
        IdMst = Data.getId();
        AdaData = IdMst > 0;
    }

}
