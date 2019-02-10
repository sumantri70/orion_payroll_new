package com.example.user.orion_payroll_new.form.transaksi;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.email.SendMail;
import com.example.user.orion_payroll_new.form.adapter.PilihKirimEmailAdapter;
import com.example.user.orion_payroll_new.form.filter.FilterPenggajian;
import com.example.user.orion_payroll_new.form.print.PrintPenggajian;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.PenggajianModel;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;
import com.example.user.orion_payroll_new.utility.route;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.ZapfDingbatsList;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.orion_payroll_new.models.JCons.EDIT_MODE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_NEGATIVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_POSITIVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SAVE_CONFIRMATION;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_DELETE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_DELETE;
import static com.example.user.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FormatDateFromSql;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getMillisDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getMillisDateFmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getSimpleDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getTglFormatCustom;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getTglFormatMySql;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.hideSoftKeyboard;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.serverNowFormated;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.serverNowLong;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.serverNowStartOfTheMonthLong;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Email_Master_Pegawai;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;
import static com.example.user.orion_payroll_new.utility.route.URL_DELETE_PENGGAJIAN;

public class PilihKirimEmail extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private TextInputEditText txtPeriode;

    private FloatingActionButton btnTambah;
    private SwipeRefreshLayout swipe;

    private List<PenggajianModel> ListData;
    private ListView ListRekap;
    public static PilihKirimEmailAdapter Adapter;

    public static String OrderBy;

    private int IdPegawai;

    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekap);
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
        this.ListRekap.setDividerHeight(1);
        IdPegawai  = 0;
        txtPeriode.setText(getTglFormatCustom(serverNowStartOfTheMonthLong(), "MMMM yyyy"));
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
                AlertDialog.Builder bld = new AlertDialog.Builder(PilihKirimEmail.this);
                bld.setTitle("Konfirmasi");
                bld.setCancelable(true);
                bld.setMessage(MSG_SAVE_CONFIRMATION);

                bld.setPositiveButton(MSG_POSITIVE,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        PenggajianModel Data;
                        for (int i = 0; i < ListData.size() ; i++) {
                            Data = new PenggajianModel(ListData.get(i));
                            if (Data.isPilih() == true){
                                PrintPenggajian print = new PrintPenggajian(PilihKirimEmail.this);
                                print.Execute(Data.getId());

                                String haha = Get_Nama_Master_Pegawai(Data.getId_pegawai())+"-"+getTglFormatCustom(Data.getPeriode(),"MMMM-yyyy")+".pdf";

                                //                                KirimEmail(Get_Email_Master_Pegawai(Data.getId_pegawai()), print.getNamaPrint(),
//                                        Environment.getExternalStorageDirectory() + "/orion_payroll/documents/"+print.getNamaPrint());

//                                haha = Environment.getExternalStorageDirectory() + "/orion_payroll/documents/Test.png";
//
//                                KirimEmail(Get_Email_Master_Pegawai(Data.getId_pegawai()), print.getNamaPrint(),haha);

                            }
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

        txtPeriode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(PilihKirimEmail.this);
                if (txtPeriode.getText().toString().equals("")){
                    txtPeriode.setText(serverNowFormated());
                }
                Long tgl = getMillisDateFmt(txtPeriode.getText().toString(), "MMMM yyyy");
                int mYear = (Integer.parseInt(FungsiGeneral.getTahun(tgl)));
                int mMonth = (Integer.parseInt(FungsiGeneral.getBulan(tgl)))-1;
                int mDay = (Integer.parseInt(FungsiGeneral.getHari(tgl)));

                DatePickerDialog datePickerDialog = new DatePickerDialog(PilihKirimEmail.this,
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
        String filter;
        long periode;
        periode = getMillisDateFmt(txtPeriode.getText().toString(), "MMMM yyyy");

        filter = "?tgl_dari="+getTglFormatMySql(periode)+ "&periode="+getTglFormatMySql(periode)+ "&id_pegawai="+Integer.toString(IdPegawai)+"&order_by="+OrderBy;
        String url = route.URL_SELECT_PENGGAJIAN_KIRIM_EMAIL + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PenggajianModel Data;
                ListData.clear();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Data = new PenggajianModel(
                                obj.getInt("id"),
                                obj.getInt("id_pegawai"),
                                obj.getInt("telat_satu"),
                                obj.getInt("telat_dua"),
                                obj.getInt("dokter"),
                                obj.getInt("izin_stgh_hari"),
                                obj.getInt("izin_cuti"),
                                obj.getInt("izin_non_cuti"),
                                obj.getString("nomor"),
                                obj.getString("keterangan"),
                                obj.getString("user_id"),
                                obj.getString("user_edit"),
                                obj.getDouble("gaji_pokok"),
                                obj.getDouble("uang_ikatan"),
                                obj.getDouble("uang_kehadiran"),
                                obj.getDouble("premi_harian"),
                                obj.getDouble("premi_perjam"),
                                obj.getDouble("jam_lembur"),
                                obj.getDouble("total_tunjangan"),
                                obj.getDouble("total_potongan"),
                                obj.getDouble("total_lembur"),
                                obj.getDouble("total_kasbon"),
                                obj.getDouble("total"),
                                getMillisDate(FormatDateFromSql(obj.getString("tanggal"))),
                                getMillisDate(FormatDateFromSql(obj.getString("tgl_input"))),
                                getMillisDate(FormatDateFromSql(obj.getString("tgl_edit"))),
                                getMillisDate(FormatDateFromSql(obj.getString("periode")))
                        );
                        Data.setPilih(true);
                        Data.setNama_pegawai(Get_Nama_Master_Pegawai(Data.getId_pegawai()));
                        ListData.add(Data);
                    }
                    //Satu baris kosong di akhir
//                    Data = new PenggajianModel();
//                    Data.setUser_id("HIDE");
//                    ListData.add(Data);

                    Adapter = new PilihKirimEmailAdapter(PilihKirimEmail.this, R.layout.list_penggajian_rekap_new, ListData);
                    Adapter.notifyDataSetChanged();
                    ListRekap.setAdapter(Adapter);
                    swipe.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PilihKirimEmail.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
                    swipe.setRefreshing(false);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ListData.clear();
                swipe.setRefreshing(false);
                Toast.makeText(PilihKirimEmail.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
            }
        });
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_kirim_email);
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

    protected boolean KirimEmail(String email, String subjek, String pathFile){

        SendMail sm = new SendMail(this, email, subjek, "", pathFile);

        //Executing sendmail to send email
        sm.execute();
        return true;
    }



}

