package com.orionit.app.orion_payroll_new.form.setting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.orionit.app.orion_payroll_new.OrionPayrollApplication;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static com.orionit.app.orion_payroll_new.models.JCons.MSG_NEGATIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_POSITIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SAVE_CONFIRMATION;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormatCustom;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.inform;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowFormated4Ekspor;

public class SettingDatabase extends AppCompatActivity {
    private Button btnBackupLocal, btnRestoreLocal, btnLogin, btnUploadCloud, btnDownloadCloud;

    private SettingDBTable TData;
    private SettingDBModel DataModel;
    private String password, AccessKey, AccessSecret;
    private String lokasi_db;
    ProgressDialog dialog;

    private final static String FILE_DIR = "/backup_orion_payroll/";
    private final static String DROPBOX_NAME = "orion payroll";
    private final static String ACCESS_KEY = "37e2umr2z0kt91d";
    private final static String ACCESS_SECRET = "u5nc2m1eru7diy9";

    private DropboxAPI<AndroidAuthSession> dropbox;

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
        password = DataModel.getPassword();
        this.lokasi_db = ((OrionPayrollApplication)getApplicationContext()).lokasi_db;

        AndroidAuthSession session;
        String key = DataModel.getAccessKey();
        String secret = DataModel.getAccessSecret();
        AccessKey = key;
        AccessSecret = secret;
        AppKeyPair pair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);

        if (key != null) {
            if (key.equals("")) {
                key = null;
            }
        }
        if (secret != null) {
            if (secret.equals("")){
                secret = null;
            }
        }

        if (key == null || secret == null) {
            session = new AndroidAuthSession(pair, Session.AccessType.APP_FOLDER);
            dropbox = new DropboxAPI<AndroidAuthSession>(session);
        } else {
            loggedIn(true);
        }
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
                                String lokasi = BackupDBNew(file, lokasi_db);
                                if (!lokasi.equals("")){
                                    inform(SettingDatabase.this, "Database berhasil dibackup ke "+lokasi);
                                }
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
                                if (!chosenDir.contains(".db")){
                                    Toast.makeText(SettingDatabase.this, "File yang dipilih bukan file database", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                //TData.CreateShmWal();//buat 2 file shm wal

                                //Backup terlebih dahulu data sebelummnya
                                String namaFile = "/orion_payroll_backup_"+serverNowFormated4Ekspor()+".db";
                                String file = CreateGetDirBefforeRestore()+namaFile;
                                String lokasi = BackupDBNew(file, lokasi_db);
                                if (lokasi.equals("")){
                                    return;
                                }

                                m_chosen = chosenDir;
                                SettingDatabase.this.DeleteShmWal();
                                if (SettingDatabase.this.restoreDBNew(m_chosen)){
                                    new AlertDialog.Builder(SettingDatabase.this)
                                            .setMessage("Database sebelumnya dibackup ke "+lokasi+"\nDan database terbaru berhasil direstore\nAplikasi akan ditutup\nSilahkan buka ulang")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    SettingDatabase.this.finishAffinity();
                                                    System.exit(0);
                                                }
                                            }).show();
                                };
                            }
                        });

                FileOpenDialog.Default_File_Name = "";
                FileOpenDialog.chooseFile_or_Dir(CreateGetDir());
                OrionPayrollApplication.getInstance().GetHashMaster();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn) {
                    SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, MODE_PRIVATE);
                    prefs.edit().remove("access-token").commit();
                    loggedIn(false);
                    TData.deleteAll();
                }else{
                    Auth.startOAuth2Authentication(SettingDatabase.this, ACCESS_KEY);
                }
            }
        });

//        btnUploadCloud.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder bld = new AlertDialog.Builder(SettingDatabase.this);
//                bld.setTitle("Konfirmasi");
//                bld.setCancelable(true);
//                bld.setMessage("Akan upload data ke dropbox?");
//
//                bld.setPositiveButton(MSG_POSITIVE,  new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        SettingDatabase.this.UploadDBNew("/data/data/com.orionit.app.orion_payroll_new/databases/db_orion_payroll.db");
//                    }
//                });
//
//                bld.setNegativeButton(MSG_NEGATIVE, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                AlertDialog dialog = bld.create();
//                dialog.show();
//            }
//        });

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
                        SettingDatabase.this.UploadDBNew(lokasi_db, "/backup_orion_payroll/Dump.db", false);

                        String lokfileSHM = lokasi_db+"-shm";
                        File fileSHM = new File(lokfileSHM);
                        if (!fileSHM.exists()) {
                            TData.CreateShmWal();
                        }

                        File folderSHM = new File(lokasi_db+"-shm");
                        if (folderSHM.exists()){
                            SettingDatabase.this.UploadDBNew(lokasi_db+"-shm", "/backup_orion_payroll/Dump.db-shm", false);
                        }

                        File folderWAL = new File(lokasi_db+"-wal");
                        if (folderWAL.exists()){
                            SettingDatabase.this.UploadDBNew(lokasi_db+"-wal", "/backup_orion_payroll/Dump.db-wal", true);
                        }

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

                        //TData.CreateShmWal();//buat 2 file shm wal

                        //Backup terlebih dahulu data sebelummnya
                        String namaFile = "/orion_payroll_backup_"+serverNowFormated4Ekspor()+".db";
                        String file = CreateGetDirBefforeRestore()+namaFile;
                        String lokasi = BackupDBNew(file, lokasi_db);
                        if (lokasi.equals("")){
                            return;
                        }

                        SettingDatabase.this.DeleteShmWal();

                        //------------------------------------
                        downloadFileNew("/data/data/com.orionit.app.orion_payroll_new/databases/db_orion_payroll.db", lokasi);
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

    public String BackupDBNew(String fileLocation, String lokasiDB)
    {
        String Hasil = "";
        try
        {
            String Asal   = lokasiDB; //"/data/data/com.orionit.app.orion_payroll_new/databases/db_orion_payroll.db";
            String Tujuan = fileLocation;

            File currentDB = new File(Asal);
            File backupDB  = new File(Tujuan);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Hasil = fileLocation;
            }

            String AsalSHM   = lokasiDB+"-shm";
            String TujuanSHM = fileLocation+"-shm";

            File currentDBSHM = new File(AsalSHM);
            File backupDBSHM  = new File(TujuanSHM);

            if (currentDBSHM.exists()) {
                FileChannel src = new FileInputStream(currentDBSHM).getChannel();
                FileChannel dst = new FileOutputStream(backupDBSHM).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }

            String AsalWAL   = lokasiDB+"-wal";
            String TujuanWAL = fileLocation+"-wal";

            File currentDBWAL = new File(AsalWAL);
            File backupDBWAL  = new File(TujuanWAL);

            if (currentDBWAL.exists()) {
                FileChannel src = new FileInputStream(currentDBWAL).getChannel();
                FileChannel dst = new FileOutputStream(backupDBWAL).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        }
        catch (Exception e) {
            Hasil = "";
            inform(SettingDatabase.this, "Gagal backup database");
            Log.w("Settings Backup", e);
        }
        return Hasil;
    }

    private boolean restoreDBNew(String FileName){
        {
            try
            {
                //RESTORE SHM
                String sumberSHM = FileName+"-shm";
                String tujuanSHM = lokasi_db+"-shm";

                File currentDBSHM = new File(sumberSHM);
                File backupDBSHM  = new File(tujuanSHM);

                if (currentDBSHM.exists()) {
                    FileChannel src = new FileInputStream(currentDBSHM).getChannel();
                    FileChannel dst = new FileOutputStream(backupDBSHM).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }

                //RESTORE WAL
                String sumberWAL = FileName+"-wal";
                String tujuanWAL = lokasi_db+"-wal";

                File currentDBWAL = new File(sumberWAL);
                File backupDBWAL  = new File(tujuanWAL);

                if (currentDBWAL.exists()) {
                    FileChannel src = new FileInputStream(currentDBWAL).getChannel();
                    FileChannel dst = new FileOutputStream(backupDBWAL).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }

                //RESTORE DB
                String sumber = FileName;
                String tujuan = lokasi_db;//"/data/data/com.orionit.app.orion_payroll_new/databases/db_orion_payroll.db";

                File currentDB = new File(sumber);
                File backupDB = new File(tujuan);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    saveSettingDB();
                }
                return true;
            }
            catch (Exception e) {
                inform(SettingDatabase.this, "Gagal restore");
                return false;
            }
        }
    }

//    private String CreateGetDir() {
//        File direct = new File("/storage/emulated/0/Download/orion_payroll/database");
//        if (!direct.exists()) {
//            direct.mkdirs();
//        }
//        return direct.getPath();
//    }

    private String CreateGetDir() {
        File direct1 = new File(Environment.getExternalStorageDirectory() + "/orion_payroll");
        if (!direct1.exists()) {
            direct1.mkdirs();
        }

        File direct2 = new File(Environment.getExternalStorageDirectory() + "/orion_payroll/database");
        if (!direct2.exists()) {
            direct2.mkdirs();
        }

        File direct3 = new File(Environment.getExternalStorageDirectory() + "/orion_payroll/database/backup");
        if (!direct3.exists()) {
            direct3.mkdirs();
        }
        return direct3.getPath();
    }

    private String CreateGetDirBefforeRestore() {
        File direct1 = new File(Environment.getExternalStorageDirectory() + "/orion_payroll");
        if (!direct1.exists()) {
            direct1.mkdirs();
        }

        File direct2 = new File(Environment.getExternalStorageDirectory() + "/orion_payroll/database");
        if (!direct2.exists()) {
            direct2.mkdirs();
        }

        File direct3 = new File(Environment.getExternalStorageDirectory() + "/orion_payroll/database/backup_sebelum_restore");
        if (!direct3.exists()) {
            direct3.mkdirs();
        }
        return direct3.getPath();
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

        if (!isLogged){
            btnUploadCloud.setBackgroundColor(getResources().getColor(R.color.colorgrey));
            btnDownloadCloud.setBackgroundColor(getResources().getColor(R.color.colorgrey));
        }else{
            btnUploadCloud.setBackground(getResources().getDrawable(R.drawable.style_btn_default));
            btnDownloadCloud.setBackground(getResources().getDrawable(R.drawable.style_btn_default));
        }

    }


    private void UploadDBNew(String fileLocation, String fileDropbox, final boolean munculkaninfo){
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
                saveSettingDB();
                if (munculkaninfo){
                    Toast.makeText(SettingDatabase.this,
                            "Upload database berhasil",
                            Toast.LENGTH_SHORT)
                            .show();
                }
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
//        }, "/backup_orion_payroll/Dump.db", fileLocation).execute();
        }, fileDropbox, fileLocation).execute();
    }


//    private void downloadFileNew(String fileLocation, final String fileBackUp){
//        File localFile = new File(fileLocation);

//
//        DbxClientV2 client;
//        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("examples-v2-demo")
//                .withHttpRequestor(new OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
//                .build();
//
//        SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, MODE_PRIVATE);
//        String accessToken = prefs.getString("access-token", null);
//        client = new DbxClientV2(requestConfig, accessToken);
//
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setCancelable(false);
//        dialog.setMessage("Downloading");
//        dialog.show();
//
//        new DownloadDropboxV2(SettingDatabase.this, client, new DownloadDropboxV2.Callback(){
//            @Override
//            public void onDownloadComplete(File result) {
//                dialog.dismiss();
//                saveSettingDB();
//                OrionPayrollApplication.getInstance().GetHashMaster();
//                inform(SettingDatabase.this, "Database sebelumnya dibackup ke "+fileBackUp+"\nDan database terbaru berhasil direstore");
//            }
//
//
//
//            @Override
//            public void onError(Exception e) {
//                dialog.dismiss();
//                SettingDatabase.this.restoreDBNew(fileBackUp);
//
//                new AlertDialog.Builder(SettingDatabase.this)
//                        .setMessage("Download database tidak berhasil")
//                        .setCancelable(true)
//                        .setPositiveButton("Ok", null)
//                        .show();
//            }
//        }, "/backup_orion_payroll/Dump.db", fileLocation).execute(localFile);
//    }

    private void downloadFileNew(String fileLocation, final String fileBackUp){
        File localFile = new File(fileLocation);
        final File localFileshm = new File(fileLocation+"-shm");
        final File localFilewal = new File(fileLocation+"-wal");

        DbxClientV2 client;
        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("examples-v2-demo")
                .withHttpRequestor(new OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
                .build();

        SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        client = new DbxClientV2(requestConfig, accessToken);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Downloading");
        dialog.show();
        this.downloadWal(client, fileLocation, localFilewal, localFileshm, localFile, fileBackUp);
    }


    private void downloadWal(final DbxClientV2 client, final String fileLocation, final File localFilewal, final File localFileshm, final File localFileDB, final String fileBackUp){

        new DownloadDropboxV2(SettingDatabase.this, client, new DownloadDropboxV2.Callback(){
            @Override
            public void onDownloadComplete(File result) {
                downloadShm(client, fileLocation, localFileshm, localFileDB, fileBackUp);
            }

            @Override
            public void onError(Exception e) {
                downloadfiledb(client, fileLocation, localFileDB, fileBackUp);
            }
        }, "/backup_orion_payroll/Dump.db-wal", fileLocation).execute(localFilewal);
    }


    private void downloadShm(final DbxClientV2 client, final String fileLocation, File localFileshm, final File localFileDB, final String fileBackUp){
        new DownloadDropboxV2(SettingDatabase.this, client, new DownloadDropboxV2.Callback(){
            @Override
            public void onDownloadComplete(File result) {
                downloadfiledb(client, fileLocation, localFileDB, fileBackUp);
            }

            @Override
            public void onError(Exception e) {
                downloadfiledb(client, fileLocation, localFileDB, fileBackUp);
            }
        }, "/backup_orion_payroll/Dump.db-shm", fileLocation+"-shm").execute(localFileshm);
    }

    private void downloadfiledb(final DbxClientV2 client, final String fileLocation, File localFile, final String fileBackUp){
        new DownloadDropboxV2(SettingDatabase.this, client, new DownloadDropboxV2.Callback(){
            @Override
            public void onDownloadComplete(File result) {
                dialog.dismiss();
                try{
                    saveSettingDB();
                    OrionPayrollApplication.getInstance().GetHashMaster();
                    //inform(SettingDatabase.this, "Database sebelumnya dibackup ke "+fileBackUp+"\nDan database terbaru berhasil direstore\nAplikasi akan ditutup\nSilahkan buka ulang");

                    new AlertDialog.Builder(SettingDatabase.this)
                            .setMessage("Database sebelumnya dibackup ke "+fileBackUp+"\nDan database terbaru berhasil direstore\nAplikasi akan ditutup\nSilahkan buka ulang")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SettingDatabase.this.finishAffinity();
                                    System.exit(0);
                                }
                            }).show();
                } catch (Exception e) {
                    new AlertDialog.Builder(SettingDatabase.this)
                            .setMessage("Downloda data gagal\nSilahkan restore ulang file "+fileBackUp+ " untuk mengembalikan data")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SettingDatabase.this.finishAffinity();
                                    System.exit(0);
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();
                SettingDatabase.this.restoreDBNew(fileBackUp);

                new AlertDialog.Builder(SettingDatabase.this)
                        .setMessage("Download database tidak berhasil")
                        .setCancelable(true)
                        .setPositiveButton("Ok", null)
                        .show();
            }
        }, "/backup_orion_payroll/Dump.db", fileLocation).execute(localFile);
    }



    private void saveSettingDB(){
        SettingDBModel newData = new SettingDBModel(this.AccessKey, this.AccessSecret, this.password);
        TData.Insert(newData);
    }

    public void DeleteShmWal(){

        File fileshm = new File(lokasi_db+"-shm");
        fileshm.delete();

        File filewal = new File(lokasi_db+"-wal");
        filewal.delete();
    }
}
