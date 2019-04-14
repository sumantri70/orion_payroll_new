package com.orionit.app.orion_payroll_new.form.setting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.database.master.PotonganTable;
import com.orionit.app.orion_payroll_new.database.master.SettingDBTable;
import com.orionit.app.orion_payroll_new.dropbox.DownloadDropboxV2;
import com.orionit.app.orion_payroll_new.dropbox.DropboxClientFactory;
import com.orionit.app.orion_payroll_new.dropbox.PicassoClient;
import com.orionit.app.orion_payroll_new.dropbox.UploadDropboxV2;
import com.orionit.app.orion_payroll_new.form.master.PotonganInput;
import com.orionit.app.orion_payroll_new.models.SettingDBModel;
import com.orionit.app.orion_payroll_new.utility.SimpleFileDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import static com.orionit.app.orion_payroll_new.models.JCons.MSG_NEGATIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_POSITIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SAVE_CONFIRMATION;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.inform;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowFormated4Ekspor;

public class SettingDatabase extends AppCompatActivity {
    private Button btnBackupLocal, btnRestoreLocal, btnLogin, btnUploadCloud, btnDownloadCloud;

    private SettingDBTable TData;
    private SettingDBModel DataModel;

    private final static String FILE_DIR = "/backup_orion_payroll/";
    private final static String DROPBOX_NAME = "orion payroll";
    private final static String ACCESS_KEY = "37e2umr2z0kt91d";
    private final static String ACCESS_SECRET = "u5nc2m1eru7diy9";

    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_database);
        CreateView();
        InitClass();
        EventClass();
    }

    protected void CreateView(){
        btnBackupLocal   = (Button) findViewById(R.id.btnBackupLocal);
        btnRestoreLocal  = (Button) findViewById(R.id.btnRestoreLocal);
        btnLogin         = (Button) findViewById(R.id.btnLogin);
        btnUploadCloud   = (Button) findViewById(R.id.btnUploadCloud);
        btnDownloadCloud = (Button) findViewById(R.id.btnDownloadCloud);
    }

    protected void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Database");
        loggedIn(false);

        TData = new SettingDBTable(getApplicationContext());
        DataModel = TData.GetData();

//        AndroidAuthSession session;
    }

    protected void EventClass(){
        btnBackupLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingDatabase.this)
                        .setMessage("Apakah database akan dibackup")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String namaFile = "/orion_payroll_backup_"+serverNowFormated4Ekspor()+".db";
                                String file = CreateGetDir()+namaFile;
                                BackupDBNew(file, namaFile);
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            }
        });

        btnRestoreLocal.setOnClickListener(new View.OnClickListener() {
            String m_chosen;
            @Override
            public void onClick(View v) {
                SimpleFileDialog FileOpenDialog =  new SimpleFileDialog(SettingDatabase.this, "FileOpen",
                        new SimpleFileDialog.SimpleFileDialogListener()
                        {
                            @Override
                            public void onChosenDir(String chosenDir)
                            {
                                m_chosen = chosenDir;
                                SettingDatabase.this.restoreDBNew(m_chosen);
                            }
                        });

                FileOpenDialog.Default_File_Name = "Dump.db";
                FileOpenDialog.chooseFile_or_Dir();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn) {
                    SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, MODE_PRIVATE);
                    prefs.edit().remove("access-token").commit();
                    loggedIn(false);
                }else{
                    Auth.startOAuth2Authentication(SettingDatabase.this, ACCESS_KEY);
                }
            }
        });

        btnUploadCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder bld = new AlertDialog.Builder(SettingDatabase.this);
                bld.setTitle("Konfirmasi");
                bld.setCancelable(true);
                bld.setMessage("Akan upload data ke dropbox?");

                bld.setPositiveButton(MSG_POSITIVE,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SettingDatabase.this.UploadDBNew("/data/data/com.orionit.app.orion_payroll_new/databases/db_orion_payroll.db");
                    }
                });

                bld.setNegativeButton(MSG_NEGATIVE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = bld.create();
                dialog.show();
            }
        });

        btnDownloadCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder bld = new AlertDialog.Builder(SettingDatabase.this);
                bld.setTitle("Konfirmasi");
                bld.setCancelable(true);
                bld.setMessage("Akan download database?");

                bld.setPositiveButton(MSG_POSITIVE,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //BAckup dulu datanya sebelum download
                        String namaFile = "/orion_payroll_backup_"+serverNowFormated4Ekspor()+".db";
                        String file = CreateGetDir()+namaFile;
                        BackupDBNew(file, namaFile);
                        //------------------------------------
                        downloadFileNew("/data/data/com.orionit.app.orion_payroll_new/databases/db_orion_payroll.db");
                    }
                });

                bld.setNegativeButton(MSG_NEGATIVE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = bld.create();
                dialog.show();
            }
        });
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void  BackupDBNew(String fileLocation, String namaFile)
    {
        try
        {
            String currentDBPath = "/data/data/com.orionit.app.orion_payroll_new/databases/db_orion_payroll.db";
            String backupDBPath = fileLocation;
            File currentDB = new File(currentDBPath);
            File backupDB = new File(backupDBPath);
            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
            inform(SettingDatabase.this, "Database berhasil dibackup ke "+fileLocation);
        }
        catch (Exception e) {
            Log.w("Settings Backup", e);
        }
    }

    private void restoreDBNew(String FileName){
        {
            try
            {
                String currentDBPath = FileName;
                String backupDBPath = "/data/data/com.orionit.app.orion_payroll_new/databases/db_orion_payroll.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
                Toast.makeText(SettingDatabase.this, "Restore Complete", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Log.w("Settings Backup", e);
            }
        }
    }

    private String CreateGetDir() {
        File direct = new File("/storage/emulated/0/Download/orion_payroll/database");
        if (!direct.exists()) {
            direct.mkdirs();
        }
        return direct.getPath();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoggedIn == false) {
            SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, MODE_PRIVATE);
            String accessToken = prefs.getString("access-token", null);
            if (accessToken == null) {
                accessToken = Auth.getOAuth2Token();
                if (accessToken != null) {
                    prefs.edit().putString("access-token", accessToken).apply();
                    initAndLoadData(accessToken);
                    loggedIn(true);
                }else {
                    isLoggedIn = false;
                }
            } else {
                initAndLoadData(accessToken);
                loggedIn(true);
            }

            String uid = Auth.getUid();
            String storedUid = prefs.getString("user-id", null);
            if (uid != null && !uid.equals(storedUid)) {
                prefs.edit().putString("user-id", uid).apply();
            }
        }
    }

    private void initAndLoadData(String accessToken) {
        DropboxClientFactory.init(accessToken);
        PicassoClient.init(getApplicationContext(), DropboxClientFactory.getClient());
    }

    public void loggedIn(boolean isLogged) {
        isLoggedIn = isLogged;
        btnUploadCloud.setEnabled(isLogged);
        btnDownloadCloud.setEnabled(isLogged);
        btnLogin.setText(isLogged ? "LOG OUT" : "LOG IN");
    }


    private void UploadDBNew(String fileLocation){
        DbxClientV2 client;
        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("examples-v2-demo")
                .withHttpRequestor(new OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
                .build();

        SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        client = new DbxClientV2(requestConfig, accessToken);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Uploading");
        dialog.show();

        new UploadDropboxV2(SettingDatabase.this, client, new UploadDropboxV2.Callback(){
            @Override
            public void onUploadComplete(FileMetadata result) {
                dialog.dismiss();
                Toast.makeText(SettingDatabase.this,
                        "Upload database berhasil",
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();

                new AlertDialog.Builder(SettingDatabase.this)
                        .setMessage("Upload database tidak berhasil")
                        .setCancelable(true)
                        .setPositiveButton("Ok", null)
                        .show();
            }
        }, "/backup_orion_payroll/Dump.DB", fileLocation).execute();
    }


    private void downloadFileNew(String fileLocation){
        File localFile = new File(fileLocation);
        DbxClientV2 client;
        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("examples-v2-demo")
                .withHttpRequestor(new OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
                .build();

        SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        client = new DbxClientV2(requestConfig, accessToken);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Downloading");
        dialog.show();

        new DownloadDropboxV2(SettingDatabase.this, client, new DownloadDropboxV2.Callback(){
            @Override
            public void onDownloadComplete(File result) {
                dialog.dismiss();
                Toast.makeText(SettingDatabase.this,"Download database berhasil",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();

                new AlertDialog.Builder(SettingDatabase.this)
                        .setMessage("Download database tidak berhasil")
                        .setCancelable(true)
                        .setPositiveButton("Ok", null)
                        .show();
            }
        }, "/backup_orion_payroll/Dump.DB", fileLocation).execute(localFile);
    }
}
