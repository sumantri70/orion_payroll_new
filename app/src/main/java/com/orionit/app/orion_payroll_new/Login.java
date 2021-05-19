package com.orionit.app.orion_payroll_new;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;
import com.orionit.app.orion_payroll_new.database.DBConection;
import com.orionit.app.orion_payroll_new.database.master.PasswordAplikasiTable;
import com.orionit.app.orion_payroll_new.form.setting.SettingDatabase;

import java.io.File;
import java.io.FileNotFoundException;

import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.inform;

public class Login extends AppCompatActivity {
    AnimationDrawable animationDrawable;
    ConstraintLayout Layouts;
    Button BtnLogin;
    EditText txtUserId, txtPassword;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private PasswordAplikasiTable TData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        TData = new PasswordAplikasiTable(getApplicationContext());

        Layouts     = (ConstraintLayout)findViewById(R.id.Layouts);
        txtUserId   = (EditText) findViewById(R.id.txtUserId);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        BtnLogin    = (Button) findViewById(R.id.BtnLogin);
        txtUserId.setVisibility(View.GONE);
        txtPassword.setText("");

        if (TData.IsAdaPassword() == false){
            txtPassword.setVisibility(View.GONE);
            BtnLogin.setText("Masuk");
        }

        try {
            createPdfWrapper();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtPassword.getVisibility() == View.VISIBLE ){
                    if(txtPassword.getText().toString().equals("")){
                        Toast.makeText(Login.this, "Password harus diisi", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TData.CekPassword(txtPassword.getText().toString())){
                        Intent s = new Intent(Login.this, MainMenu.class);
                        startActivity(s);
                    }else{
                        Toast.makeText(Login.this, "Password yang anda masukan salah", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Intent s = new Intent(Login.this, MainMenu.class);
                    startActivity(s);
                }
            }
        });
    }

    private void createPdfWrapper() throws FileNotFoundException,DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            },

                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    System.exit(0);
                                }
                            }
                    );
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", cancelListener)
                .create()
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (animationDrawable != null && animationDrawable.isRunning()){
            animationDrawable.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (animationDrawable != null && !animationDrawable.isRunning()){
            animationDrawable.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK && null != data) {
            Uri dt = data.getData();
            File backupDB = new File(dt.getPath());
            String fileExt = MimeTypeMap.getFileExtensionFromUrl(dt.toString());
            Toast.makeText(Login.this, fileExt, Toast.LENGTH_SHORT).show();
        }
    }
}
