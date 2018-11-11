package com.example.user.orion_payroll_new.form.print;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.form.transaksi.PenggajianInputNew;
import com.example.user.orion_payroll_new.models.PenggajianDetailModel;
import com.example.user.orion_payroll_new.models.PenggajianModel;
import com.example.user.orion_payroll_new.utility.route;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
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
import java.util.ArrayList;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.TIPE_DET_KASBON;
import static com.example.user.orion_payroll_new.models.JCons.TIPE_DET_POTONGAN;
import static com.example.user.orion_payroll_new.models.JCons.TIPE_DET_TUNJANGAN;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FormatDateFromSql;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getMillisDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getSimpleDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getTglFormat;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;

public class PrintPenggajian {
        private ProgressDialog Loading;
        private PenggajianModel DtPenggajian;
        private List<PenggajianDetailModel> ArListTunjangan;
        private List<PenggajianDetailModel> ArListPotongan;
        private List<PenggajianDetailModel> ArListKasbon;
        private List<PenggajianDetailModel> ArListKasbonTmp;

        Context context;
        private static final String TAG = "PdfCreatorActivity";
        private File pdfFile;

        public PrintPenggajian(Context ctx) {
            Loading = new ProgressDialog(ctx);
            context = ctx;
            ArListTunjangan = new ArrayList<>();
            ArListPotongan  = new ArrayList<>();
            ArListKasbon    = new ArrayList<>();
            ArListKasbonTmp = new ArrayList<>();
        }

        public void Execute(final int IdMst){
            LoadData(IdMst);
        }

        private void CreatePrdf() throws FileNotFoundException, DocumentException {
            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/orion_payroll/documents");
            if (!docsFolder.exists()) {
                docsFolder.mkdir();
                Log.i(TAG, "Created a new directory for PDF");
            }

            pdfFile = new File(docsFolder.getAbsolutePath(),"Penggajian.pdf");
            OutputStream output = new FileOutputStream(pdfFile);
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            PdfWriter.getInstance(document, output);
            document.setPageSize(PageSize.A6);
            document.open();

            document.add(new Paragraph("Nomor         : " + DtPenggajian.getNomor()));
            document.add(new Paragraph("Tanggal       : " + getTglFormat(DtPenggajian.getTanggal())));
            document.add(new Paragraph("Periode        : " + getTglFormat(DtPenggajian.getTanggal())));
            document.add(new Paragraph("Nik/Nama    : " + Get_Nama_Master_Pegawai(DtPenggajian.getId_pegawai())));

//            Chunk glue = new Chunk(new VerticalPositionMark());
//            Paragraph p = new Paragraph("Text to the left");
//            p.add(new Chunk(glue));
//            p.add("Text to the right");
//            document.add(p);
//
//            ZapfDingbatsList zapfDingbatsList1 = new ZapfDingbatsList(40, 15);
//            zapfDingbatsList1.add(new ListItem("Item 1"));
//            zapfDingbatsList1.add(new ListItem("Item 2"));
//            zapfDingbatsList1.add(new ListItem("Item 3"));
//
//            document.add(zapfDingbatsList1);
            document.close();
        }

        protected void LoadData(final int IdMst){
            Loading.setMessage("Loading...");
            Loading.setCancelable(false);
            Loading.show();
            String filter;
            filter = "?id="+IdMst;
            String url = route.URL_GET_PENGGAJIAN  + filter;
            JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        DtPenggajian = new PenggajianModel();
                        DtPenggajian.setId(obj.getInt("id"));
                        DtPenggajian.setId_pegawai(obj.getInt("id_pegawai"));
                        DtPenggajian.setTelat_satu(obj.getInt("telat_satu"));
                        DtPenggajian.setTelat_dua(obj.getInt("telat_dua"));
                        DtPenggajian.setDokter(obj.getInt("dokter"));
                        DtPenggajian.setIzin_stgh_hari(obj.getInt("izin_stgh_hari"));
                        DtPenggajian.setIzin_cuti(obj.getInt("izin_cuti"));
                        DtPenggajian.setIzin_non_cuti(obj.getInt("izin_non_cuti"));
                        DtPenggajian.setNomor(obj.getString("nomor"));
                        DtPenggajian.setKeterangan(obj.getString("keterangan"));
                        DtPenggajian.setUser_id(obj.getString("user_id"));
                        DtPenggajian.setUser_edit(obj.getString("user_edit"));
                        DtPenggajian.setGaji_pokok(obj.getDouble("gaji_pokok"));
                        DtPenggajian.setUang_ikatan(obj.getDouble("uang_ikatan"));
                        DtPenggajian.setUang_kehadiran(obj.getDouble("uang_kehadiran"));
                        DtPenggajian.setPremi_harian(obj.getDouble("premi_harian"));
                        DtPenggajian.setPremi_perjam(obj.getDouble("premi_perjam"));
                        DtPenggajian.setJam_lembur(obj.getDouble("jam_lembur"));
                        DtPenggajian.setTotal_tunjangan(obj.getDouble("total_tunjangan"));
                        DtPenggajian.setTotal_potongan(obj.getDouble("total_potongan"));
                        DtPenggajian.setTotal_lembur(obj.getDouble("total_lembur"));
                        DtPenggajian.setTotal_kasbon(obj.getDouble("total_kasbon"));
                        DtPenggajian.setTotal(obj.getDouble("total"));
                        DtPenggajian.setTanggal(getSimpleDate(obj.getString("tanggal")));
                        DtPenggajian.setTgl_input(getSimpleDate(obj.getString("tgl_input")));
                        DtPenggajian.setTgl_edit(getSimpleDate(obj.getString("tgl_edit")));
                        DtPenggajian.setPeriode(getSimpleDate(obj.getString("periode")));
                        LoadKasbonPegawaiEdit(IdMst);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
                        Loading.dismiss();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Loading.dismiss();
                }
            });
            OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
        }

        public void LoadKasbonPegawaiEdit(final int id_pegawai){
        String filter = "";
        filter = "?status=0&id_pegawai="+Integer.toString(id_pegawai);
        String url = route.URL_SELECT_KASBON_4_PENGGAJIAN + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PenggajianDetailModel Data;
                ArListKasbonTmp.clear();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Data = new PenggajianDetailModel();
                        Data.setId_tjg_pot_kas(obj.getInt("id"));
                        Data.setNomor(obj.getString("nomor"));
                        Data.setTanggal(getMillisDate(FormatDateFromSql(obj.getString("tanggal"))));
                        Data.setSisa(obj.getDouble("sisa"));
                        Data.setLama_cicilan(obj.getInt("cicilan"));
                        Data.setTotal(obj.getDouble("jumlah"));
                        ArListKasbonTmp.add(Data);
                    }
                    LoadDetail(id_pegawai);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
            }
        });
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
        }

        protected void LoadDetail(int IdMst){
        String filter;
        filter = "?id_master="+IdMst;
        String url = route.URL_GET_PENGGAJIAN_DETAIL + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PenggajianDetailModel Data;
                try {
                    JSONArray jsonArrayDetail = response.getJSONArray("data");
                    for (int i = 0; i < jsonArrayDetail.length(); i++) {
                        JSONObject objDetail = jsonArrayDetail.getJSONObject(i);
                        Data = new PenggajianDetailModel();
                        Data.setTipe(objDetail.getString("tipe"));
                        Data.setId_tjg_pot_kas(objDetail.getInt("id_tjg_pot_kas"));
                        Data.setJumlah(objDetail.getDouble("jumlah"));

                        if (objDetail.getString("tipe").equals(TIPE_DET_TUNJANGAN)){
                            ArListTunjangan.add(Data);
                        }else if (objDetail.getString("tipe").equals(TIPE_DET_POTONGAN)){
                            ArListPotongan.add(Data);
                        }else if (objDetail.getString("tipe").equals(TIPE_DET_KASBON)) {
                            Data.setCheck(true);

                            if (CekKasbonTmpExist(Data.getId_tjg_pot_kas())){
                                int idx = GetIdxArListKasbonTmp(Data.getId_tjg_pot_kas());
                                Data.setNomor(ArListKasbonTmp.get(idx).getNomor());
                                Data.setTanggal(ArListKasbonTmp.get(idx).getTanggal());
                                Data.setTotal(ArListKasbonTmp.get(idx).getTotal());
                                Data.setSisa(ArListKasbonTmp.get(idx).getSisa() + Data.getJumlah());
                                Data.setLama_cicilan(ArListKasbonTmp.get(idx).getLama_cicilan());
                            }
                            ArListKasbon.add(Data);
                        }
                    }

                    try {
                        CreatePrdf();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                    Loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
                    Loading.dismiss();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Loading.dismiss();
            }
        });
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
        }

        protected boolean CekKasbonTmpExist (int id){
            for(int i=0; i < ArListKasbonTmp.size(); i++){
                if (ArListKasbonTmp.get(i).getId_tjg_pot_kas() == id){
                    return true;
                }
            }
            return false;
        }

        protected int GetIdxArListKasbonTmp (int id){
            for(int i=0; i < ArListKasbonTmp.size(); i++){
                if (ArListKasbonTmp.get(i).getId_tjg_pot_kas() == id){
                    return i;
                }
            }
            return -1;
        }

}
