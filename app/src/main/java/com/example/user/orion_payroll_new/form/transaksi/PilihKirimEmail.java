package com.example.user.orion_payroll_new.form.transaksi;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.user.orion_payroll_new.form.adapter.PilihKirimEmailAdapter;
import com.example.user.orion_payroll_new.form.filter.FilterPenggajian;
import com.example.user.orion_payroll_new.form.print.PrintPenggajian;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.PenggajianModel;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;
import com.example.user.orion_payroll_new.utility.route;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_DELETE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_DELETE;
import static com.example.user.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FormatDateFromSql;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getMillisDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getTglFormatMySql;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.serverNowLong;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.serverNowStartOfTheMonthLong;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;
import static com.example.user.orion_payroll_new.utility.route.URL_DELETE_PENGGAJIAN;

public class PilihKirimEmail extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private FloatingActionButton btnTambah;   

    private SwipeRefreshLayout swipe;

    private List<PenggajianModel> ListData;
    private ListView ListRekap;
    public static PilihKirimEmailAdapter Adapter;

    public static String OrderBy;

    private Long tgl_dari, tgl_Sampai;
    private int IdPegawai;

    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekap);
        this.btnTambah  = (FloatingActionButton) findViewById(R.id.btnTambah);
        this.swipe      = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(Color.DKGRAY, Color.GREEN, Color.BLUE, Color.CYAN);
    }

    private void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Kirim Email Penggajian");


        OrderBy = "tanggal";
        ListData = new ArrayList<PenggajianModel>();
        this.ListRekap.setDividerHeight(1);

        tgl_dari   = serverNowStartOfTheMonthLong();
        tgl_Sampai = serverNowLong();
        IdPegawai  = 0;
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
                Intent s = new Intent(PilihKirimEmail.this, PenggajianInputNew.class);
                s.putExtra("MODE","");
                startActivityForResult(s, 1);
            }
        });


    }

    public void LoadData(){
        swipe.setRefreshing(true);
        String filter;
        filter = "?tgl_dari="+getTglFormatMySql(tgl_dari)+ "&tgl_sampai="+getTglFormatMySql(tgl_Sampai)+ "&id_pegawai="+Integer.toString(IdPegawai)+"&order_by="+OrderBy;
        String url = route.URL_SELECT_PENGGAJIAN + filter;
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
}

