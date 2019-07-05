package com.orionit.app.orion_payroll_new.form.transaksi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.database.master.EmailTable;
import com.orionit.app.orion_payroll_new.database.master.PenggajianTable;
import com.orionit.app.orion_payroll_new.email.SendMail;
import com.orionit.app.orion_payroll_new.form.adapter.PiliKirimEmailAdapterNew;
import com.orionit.app.orion_payroll_new.form.adapter.PiliKirimEmailAdapterNew;
import com.orionit.app.orion_payroll_new.form.print.PrintPenggajian;
import com.orionit.app.orion_payroll_new.models.EmailModel;
import com.orionit.app.orion_payroll_new.models.KirimEmailModel;
import com.orionit.app.orion_payroll_new.models.PenggajianModel;
import com.orionit.app.orion_payroll_new.utility.FungsiGeneral;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.orionit.app.orion_payroll_new.models.JCons.MSG_NEGATIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_POSITIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SAVE_CONFIRMATION;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.EndOfTheMonthLong;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.StartOfTheMonthLong;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getMillisDateFmt;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormatCustom;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.hideSoftKeyboard;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.inform;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowFormated;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowStartOfTheMonthLong;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Email_Master_Pegawai;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;

public class PilihKirimEmailNew extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private TextInputEditText txtPeriode;

    private FloatingActionButton btnTambah;
    private SwipeRefreshLayout swipe;

    private ArrayList<PenggajianModel> ListData;

    public static String OrderBy;

    private int IdPegawai;
    private PenggajianTable DbMaster;
    private ProgressDialog progressDialog;

    public RecyclerView ListRekap;
    public PiliKirimEmailAdapterNew mAdapter;
    private LinearLayoutManager linearLayoutManager;

    private void CreateVew(){
        this.ListRekap  = (RecyclerView) findViewById(R.id.ListRekap);
        this.btnTambah  = (FloatingActionButton) findViewById(R.id.btnTambah);
        this.swipe      = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(Color.DKGRAY, Color.GREEN, Color.BLUE, Color.CYAN);
        txtPeriode    = (TextInputEditText) findViewById(R.id.txtPeriode);
    }

    private void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Kirim Email Penggajian");

        OrderBy = "tanggal";
        ListData = new ArrayList<PenggajianModel>();

        IdPegawai  = 0;
        txtPeriode.setText(getTglFormatCustom(serverNowStartOfTheMonthLong(), "MMMM yyyy"));

        mAdapter = new PiliKirimEmailAdapterNew(PilihKirimEmailNew.this, ListData);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ListRekap.getContext(), GridLayoutManager.VERTICAL);
        ListRekap.addItemDecoration(dividerItemDecoration);
        ListRekap.setAdapter(mAdapter);

        DbMaster = new PenggajianTable(this);
        DbMaster.SetRecords(ListData);
    }

    protected void EventClass(){

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           LoadData();
                       }
                   }
        );

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder bld = new AlertDialog.Builder(PilihKirimEmailNew.this);
                bld.setTitle("Konfirmasi");
                bld.setCancelable(true);
                bld.setMessage(MSG_SAVE_CONFIRMATION);

                bld.setPositiveButton(MSG_POSITIVE,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EmailTable TEmail = new EmailTable(PilihKirimEmailNew.this);
                        EmailModel SettingEemail = TEmail.GetData(1);

                        if (SettingEemail.getAlamat_email().equals("")){
                            Toast.makeText(PilihKirimEmailNew.this, "Email pengirim belum diisi", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ArrayList<KirimEmailModel> ListKirim = new ArrayList<KirimEmailModel>();
                        PenggajianModel Data;
                        KirimEmailModel DataKirm;

                        progressDialog = ProgressDialog.show(PilihKirimEmailNew.this,"Menyiapkan data","Please wait...",false,false);

                        boolean empty = true;

                        for (int i = 0; i < ListData.size() ; i++) {
                            Data = new PenggajianModel(ListData.get(i));
                            if ((Data.isPilih() == true) && (Data.getId_pegawai() > 0)) {
                                empty = false;
                                progressDialog.setMessage(Get_Nama_Master_Pegawai(Data.getId_pegawai()));
                                PrintPenggajian print = new PrintPenggajian(PilihKirimEmailNew.this);
                                String Email = Get_Email_Master_Pegawai(Data.getId_pegawai());
                                String Path = print.Execute(Data.getId());
                                DataKirm = new KirimEmailModel(Email, "", "", Path);
                                ListKirim.add(DataKirm);
                            }
                        }

                        if(empty == true){
                            progressDialog.dismiss();
                            inform(PilihKirimEmailNew.this, "Tidak ada penggajian yang dipilih");
                            return;
                        }

                        progressDialog.dismiss();
                        SendMail sm = new SendMail(PilihKirimEmailNew.this);
                        sm.execute(ListKirim);
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

        txtPeriode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(PilihKirimEmailNew.this);
                if (txtPeriode.getText().toString().equals("")){
                    txtPeriode.setText(serverNowFormated());
                }
                Long tgl = getMillisDateFmt(txtPeriode.getText().toString(), "MMMM yyyy");
                int mYear = (Integer.parseInt(FungsiGeneral.getTahun(tgl)));
                int mMonth = (Integer.parseInt(FungsiGeneral.getBulan(tgl)))-1;
                int mDay = (Integer.parseInt(FungsiGeneral.getHari(tgl)));

                DatePickerDialog datePickerDialog = new DatePickerDialog(PilihKirimEmailNew.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("MMyy");
                                format = new SimpleDateFormat("MMMM yyyy");
                                txtPeriode.setText(format.format(calendar.getTime()));
                                LoadData();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                txtPeriode.setError(null);
            }
        });
    }


    public void LoadData(){
        swipe.setRefreshing(true);
        long periode_dari   = StartOfTheMonthLong(getMillisDateFmt(txtPeriode.getText().toString(), "MMMM yyyy"));
        long periode_sampai = EndOfTheMonthLong(getMillisDateFmt(txtPeriode.getText().toString(), "MMMM yyyy"));

        this.DbMaster.ReloadList(Long.valueOf(0), Long.valueOf(0), periode_dari, periode_sampai,"nomor");
        for (int i = 0; i < ListData.size(); i++){
            ListData.get(i).setPilih(true);
        }
        ListData.get(ListData.size()-1).setPilih(false);

        mAdapter = new PiliKirimEmailAdapterNew(PilihKirimEmailNew.this, ListData);
        ListRekap.setLayoutManager(new GridLayoutManager(PilihKirimEmailNew.this, 1, GridLayoutManager.VERTICAL, false));
        linearLayoutManager = (LinearLayoutManager)ListRekap.getLayoutManager();
        ListRekap.setAdapter(mAdapter);

        swipe.setRefreshing(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_kirim_email_new);
        CreateVew();
        InitClass();
        EventClass();
        LoadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onRefresh() {
        LoadData();
    }

//    protected boolean KirimEmail(String email, String subjek, String pathFile){
//
//        SendMail sm = new SendMail(this, email, subjek, "", pathFile);
//
//        //Executing sendmail to send email
//        sm.execute();p
//        return true;
//    }


//    protected void sendEmail() {
//
//        String[] TO = {"someone@gmail.com"};
//        String[] CC = {"xyz@gmail.com"};
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//
//        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("text/plain");
//
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
//        emailIntent.putExtra(Intent.EXTRA_STREAM, CC);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
//
//        try {
//            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//            finish();
//
//        } catch (android.content.ActivityNotFoundException ex) {
//
//        }
//    }
}

//    public void LoadData(){
//        swipe.setRefreshing(true);
//        String filter;
//        long periode;
//        periode = getMillisDateFmt(txtPeriode.getText().toString(), "MMMM yyyy");
//
//        filter = "?tgl_dari="+getTglFormatMySql(periode)+ "&periode="+getTglFormatMySql(periode)+ "&id_pegawai="+Integer.toString(IdPegawai)+"&order_by="+OrderBy;
//        String url = route.URL_SELECT_PENGGAJIAN_KIRIM_EMAIL + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                PenggajianModel Data;
//                ListData.clear();
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject obj = jsonArray.getJSONObject(i);
//                        Data = new PenggajianModel(
//                                obj.getInt("id"),
//                                obj.getInt("id_pegawai"),
//                                obj.getInt("telat_satu"),
//                                obj.getInt("telat_dua"),
//                                obj.getInt("dokter"),
//                                obj.getInt("izin_stgh_hari"),
//                                obj.getInt("izin_cuti"),
//                                obj.getInt("izin_non_cuti"),
//                                obj.getString("nomor"),
//                                obj.getString("keterangan"),
//                                obj.getString("user_id"),
//                                obj.getString("user_edit"),
//                                obj.getDouble("gaji_pokok"),
//                                obj.getDouble("uang_ikatan"),
//                                obj.getDouble("uang_kehadiran"),
//                                obj.getDouble("premi_harian"),
//                                obj.getDouble("premi_perjam"),
//                                obj.getDouble("jam_lembur"),
//                                obj.getDouble("total_tunjangan"),
//                                obj.getDouble("total_potongan"),
//                                obj.getDouble("total_lembur"),
//                                obj.getDouble("total_kasbon"),
//                                obj.getDouble("total"),
//                                getMillisDate(FormatDateFromSql(obj.getString("tanggal"))),
//                                getMillisDate(FormatDateFromSql(obj.getString("tgl_input"))),
//                                getMillisDate(FormatDateFromSql(obj.getString("tgl_edit"))),
//                                getMillisDate(FormatDateFromSql(obj.getString("periode")))
//                        );
//                        Data.setPilih(true);
//                        Data.setNama_pegawai(Get_Nama_Master_Pegawai(Data.getId_pegawai()));
//                        ListData.add(Data);
//                    }
//                    //Satu baris kosong di akhir
////                    Data = new PenggajianModel();
////                    Data.setUser_id("HIDE");
////                    ListData.add(Data);
//
//                    Adapter = new PilihKirimEmailAdapter(PilihKirimEmail.this, R.layout.list_penggajian_rekap_new, ListData);
//                    Adapter.notifyDataSetChanged();
//                    ListRekap.setAdapter(Adapter);
//                    swipe.setRefreshing(false);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PilihKirimEmail.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//                    swipe.setRefreshing(false);
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                ListData.clear();
//                swipe.setRefreshing(false);
//                Toast.makeText(PilihKirimEmail.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//            }
//        });
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//    }