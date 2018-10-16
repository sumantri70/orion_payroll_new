package com.example.user.orion_payroll_new.form.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.user.orion_payroll_new.form.master.PegawaiInput;
import com.example.user.orion_payroll_new.form.master.PegawaiRekap;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.KasbonPegawaiModel;
import com.example.user.orion_payroll_new.models.KasbonPegawaiModel;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_ACTIVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_ACTIVE;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FormatDateFromSql;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getTglFormat;
import static com.example.user.orion_payroll_new.utility.route.URL_AKTIVASI_PEGAWAI;

public class KasbonPegawaiAdapter extends ArrayAdapter<KasbonPegawaiModel> implements Filterable {

    private ProgressDialog Loading;
    private Context ctx;
    private List<KasbonPegawaiModel> objects;
    private List<KasbonPegawaiModel> filteredData;
    private KasbonPegawaiAdapter.ItemFilter mFilter = new KasbonPegawaiAdapter.ItemFilter();

    public KasbonPegawaiAdapter(Context context, int resource, List<KasbonPegawaiModel> object) {
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
        v = inflater.inflate(R.layout.list_kasbon_pegawai_rekap, null);
        final KasbonPegawaiModel Data = getItem(position);

        TextView lblNomor      = (TextView) v.findViewById(R.id.lblNomor);
        TextView lblTanggal     = (TextView) v.findViewById(R.id.lblTanggal);
        TextView lblPegawai    = (TextView) v.findViewById(R.id.lblPegawai);
        TextView lblJumlah     = (TextView) v.findViewById(R.id.lblJumlah);

        final ImageButton btnAction = (ImageButton) v.findViewById(R.id.btnAction);

        if (Data.getUser_id() == "HIDE"){
            btnAction.setVisibility(View.GONE);
            lblJumlah.setVisibility(View.GONE);
        }
        final int IdMSt = Data.getId();
        lblNomor.setText(Data.getNomor());
        lblTanggal.setText(getTglFormat(Data.getTanggal()));
        //lblPegawai.setText(Data.getTelpon1());
        lblJumlah.setText(fmt.format(Data.getJumlah()));

        btnAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu po = new PopupMenu(getContext(), btnAction);
                po.getMenuInflater().inflate(R.menu.menu_action_transaksi, po.getMenu());
                po.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (IdMSt > 0){
                            if (item.getTitle().equals("Detail")){
                                Intent s = new Intent(getContext(), PegawaiInput.class);
                                s.putExtra("MODE", JCons.DETAIL_MODE);
                                s.putExtra("ID",IdMSt);
                                getContext().startActivity(s);
                            } else if (item.getTitle().equals("Edit")){
                                Intent s = new Intent(getContext(), PegawaiInput.class);
                                s.putExtra("MODE", JCons.EDIT_MODE);
                                s.putExtra("ID",IdMSt);
                                ((PegawaiRekap)ctx).startActivityForResult(s, 1);
                            } else if (item.getTitle().equals("Hapus")){
//                                StringRequest strReq = new StringRequest(Request.Method.POST, URL_AKTIVASI_PEGAWAI, new Response.Listener<String>() {
//                                    @Override
//                                    public void onResponse(String response) {
//                                        try {
//                                            JSONObject jObj = new JSONObject(response);
//                                            ((PegawaiRekap)ctx).LoadData();
//                                            Toast.makeText(getContext(),MSG_SUCCESS_ACTIVE, Toast.LENGTH_SHORT).show();
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }, new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
//                                        Toast.makeText(getContext(),MSG_UNSUCCESS_ACTIVE, Toast.LENGTH_SHORT).show();
//                                    }
//                                }) {
//                                    @Override
//                                    protected Map<String, String> getParams() {
//                                        Map<String, String> params = new HashMap<String, String>();
//                                        params.put("id", String.valueOf(IdMSt));
//                                        return params;
//                                    }
//                                };
//                                OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
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

    public KasbonPegawaiModel getItem(int position) {
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

            final List<KasbonPegawaiModel> list = objects;

            int count = list.size();
            final ArrayList<KasbonPegawaiModel> nlist = new ArrayList<KasbonPegawaiModel>(count);

            String nomor ;
            String nama ;
            for (int i = 0; i < count; i++) {
                nomor = list.get(i).getNomor();
                //nama = list.get(i).getNama();
//                if ((nomor.toLowerCase().contains(filterString)) || (nama.toLowerCase().contains(filterString))) {
//                    nlist.add(list.get(i));
//                }
                if ((nomor.toLowerCase().contains(filterString))) {
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
            filteredData = (ArrayList<KasbonPegawaiModel>) results.values;
            notifyDataSetChanged();
        }

    }

}
