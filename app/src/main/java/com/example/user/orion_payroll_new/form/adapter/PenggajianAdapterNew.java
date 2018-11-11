package com.example.user.orion_payroll_new.form.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.form.print.PrintPenggajian;
import com.example.user.orion_payroll_new.form.transaksi.PenggajianInputNew;
import com.example.user.orion_payroll_new.form.transaksi.PenggajianRekapNew;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.PenggajianModel;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.ZapfDingbatsList;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_DELETE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_DELETE;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getTglFormat;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;
import static com.example.user.orion_payroll_new.utility.route.URL_DELETE_PENGGAJIAN;

public class PenggajianAdapterNew extends ArrayAdapter<PenggajianModel> implements Filterable {

    private ProgressDialog Loading;
    public Context ctx;
    private java.util.List<PenggajianModel> objects;
    private java.util.List<PenggajianModel> filteredData;
    private PenggajianAdapterNew.ItemFilter mFilter = new PenggajianAdapterNew.ItemFilter();

    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;


    public PenggajianAdapterNew(Context context, int resource, java.util.List<PenggajianModel> object) {
        super(context, resource, object);
        this.ctx = context;
        this.objects = object;
        this.filteredData = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        final int pos = position;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        v = inflater.inflate(R.layout.list_penggajian_rekap_new, null);
        final PenggajianModel Data = getItem(position);

        TextView lblNomor = (TextView) v.findViewById(R.id.lblNomor);
        TextView lblTanggal = (TextView) v.findViewById(R.id.lblTanggal);
        TextView lblPegawai = (TextView) v.findViewById(R.id.lblPegawai);
        TextView lblJumlah = (TextView) v.findViewById(R.id.lblJumlah);

        final ImageButton btnAction = (ImageButton) v.findViewById(R.id.btnAction);

        final int IdMSt = Data.getId();
        lblNomor.setText(Data.getNomor());
        lblTanggal.setText(getTglFormat(Data.getTanggal()));
        if (Data.getId_pegawai() > 0) {
            lblPegawai.setText(OrionPayrollApplication.getInstance().ListHashPegawaiGlobal.get(Integer.toString(Data.getId_pegawai())).getNama());
        }
        lblJumlah.setText(fmt.format(Data.getTotal()));

        if (Data.getUser_id() == "HIDE") {
            lblTanggal.setText("");
            lblJumlah.setText("");
            lblPegawai.setText("");
            btnAction.setVisibility(View.GONE);
        }

        btnAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu po = new PopupMenu(getContext(), btnAction);
                po.getMenuInflater().inflate(R.menu.menu_action_transaksi_penggajian, po.getMenu());
                po.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (IdMSt > 0) {
                            if (item.getTitle().equals("Detail")) {
                                Intent s = new Intent(getContext(), PenggajianInputNew.class);
                                s.putExtra("MODE", JCons.DETAIL_MODE);
                                s.putExtra("ID", IdMSt);
                                getContext().startActivity(s);
                            } else if (item.getTitle().equals("Edit")) {
                                Intent s = new Intent(getContext(), PenggajianInputNew.class);
                                s.putExtra("MODE", JCons.EDIT_MODE);
                                s.putExtra("ID", IdMSt);
                                ((PenggajianRekapNew) ctx).startActivityForResult(s, 1);
                            } else if (item.getTitle().equals("Hapus")) {
                                StringRequest strReq = new StringRequest(Request.Method.POST, URL_DELETE_PENGGAJIAN, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jObj = new JSONObject(response);
                                            ((PenggajianRekapNew) ctx).LoadData();
                                            Toast.makeText(getContext(), MSG_SUCCESS_DELETE, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), MSG_UNSUCCESS_DELETE, Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("id", String.valueOf(IdMSt));
                                        return params;
                                    }
                                };
                                OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
                            } else if (item.getTitle().equals("Cetak")) {
                                PrintPenggajian print = new PrintPenggajian(ctx);
                                print.Execute(IdMSt);
//                                try {
//                                    createPdf();
//                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
//                                } catch (DocumentException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        }
                        return false;
                    }
                });
                po.show();
            }
        });
        return v;
    }

    public int getCount() {
        return filteredData.size();
    }

    public PenggajianModel getItem(int position) {
        return filteredData.get(position);
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final java.util.List<PenggajianModel> list = objects;

            int count = list.size();
            final ArrayList<PenggajianModel> nlist = new ArrayList<PenggajianModel>(count);

            String nomor;
            String nama_pegawai;
            String jumlah;
            String tanggal;
            for (int i = 0; i < count; i++) {
                nomor        = list.get(i).getNomor();
                nama_pegawai = "";
                if (list.get(i).getId_pegawai() > 0){
                    nama_pegawai = Get_Nama_Master_Pegawai(list.get(i).getId_pegawai());
                }
                jumlah       = Double.toString(list.get(i).getTotal());
                tanggal      = getTglFormat(list.get(i).getTanggal());
                if ((nomor.toLowerCase().contains(filterString)) || (nama_pegawai.toLowerCase().contains(filterString))
                        || (jumlah.toLowerCase().contains(filterString)) || (tanggal.toLowerCase().contains(filterString))) {
                    nlist.add(list.get(i));
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<PenggajianModel>) results.values;
            notifyDataSetChanged();
        }

    }

    private void createPdf() throws FileNotFoundException, DocumentException {
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
//        document.setMargins(36, 72, 108, 180);
//        document.setMarginMirroring(true);
//        document.setMarginMirroringTopBottom(true);
        document.open();

        document.add(new Paragraph("Nomor         : "));
        document.add(new Paragraph("Tanggal       : "));
        document.add(new Paragraph("Periode      : "));
        document.add(new Paragraph("Nik/Nama   : "));

        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p = new Paragraph("Text to the left");
        p.add(new Chunk(glue));
        p.add("Text to the right");
        document.add(p);



        ZapfDingbatsList zapfDingbatsList1 = new ZapfDingbatsList(40, 15);
        zapfDingbatsList1.add(new ListItem("Item 1"));
        zapfDingbatsList1.add(new ListItem("Item 2"));
        zapfDingbatsList1.add(new ListItem("Item 3"));

        document.add(zapfDingbatsList1);
        document.close();
    }

}