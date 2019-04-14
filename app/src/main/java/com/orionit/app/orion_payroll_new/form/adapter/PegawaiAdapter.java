package com.orionit.app.orion_payroll_new.form.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.database.master.PegawaiTable;
import com.orionit.app.orion_payroll_new.database.master.PotonganTable;
import com.orionit.app.orion_payroll_new.form.master.PegawaiInput;
import com.orionit.app.orion_payroll_new.form.master.PegawaiRekap;
import com.orionit.app.orion_payroll_new.form.master.PotonganRekap;
import com.orionit.app.orion_payroll_new.models.JCons;
import com.orionit.app.orion_payroll_new.models.PegawaiModel;
import com.orionit.app.orion_payroll_new.OrionPayrollApplication;
import com.orionit.app.orion_payroll_new.utility.FungsiGeneral;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.orionit.app.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_AKTIVASI_CONFIRMATION;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_NEGATIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_POSITIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SAVE_CONFIRMATION;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SUCCESS_ACTIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_UNSUCCESS_ACTIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.orionit.app.orion_payroll_new.utility.FormatNumber.fmt;
import static com.orionit.app.orion_payroll_new.utility.route.URL_AKTIVASI_PEGAWAI;

public class PegawaiAdapter extends ArrayAdapter<PegawaiModel> implements Filterable {
    private ProgressDialog Loading;
    private Context ctx;
    private List<PegawaiModel> objects;
    private List<PegawaiModel> filteredData;
    private ItemFilter mFilter = new ItemFilter();

    public PegawaiAdapter(Context context, int resource, List<PegawaiModel> object) {
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
        v = inflater.inflate(R.layout.list_pegawai_rekap, null);
        final PegawaiModel Data = getItem(position);

        TextView lblNik      = (TextView) v.findViewById(R.id.lblNik);
        TextView lblNama     = (TextView) v.findViewById(R.id.lblNama);
        TextView lblNoTelpon = (TextView) v.findViewById(R.id.lblNoTelpon);
        TextView lblGaji     = (TextView) v.findViewById(R.id.lblGaji);

        final ImageButton btnAction = (ImageButton) v.findViewById(R.id.btnAction);

        if (Data.getStatus() == "HIDE"){
            btnAction.setVisibility(View.GONE);
            lblGaji.setVisibility(View.GONE);
        }
        final int IdMSt = Data.getId();
        lblNik.setText(Data.getNik());
        lblNama.setText(Data.getNama());
        lblNoTelpon.setText(Data.getTelpon1());
        lblGaji.setText(fmt.format(Data.getGaji_pokok()));

        btnAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu po = new PopupMenu(getContext(), btnAction);
                po.getMenuInflater().inflate(R.menu.menu_action_master, po.getMenu());
                po.getMenu().getItem(1).setEnabled(Data.getStatus().equals(TRUE_STRING));
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
                            } else if (item.getTitle().equals("Aktivasi")){
                                AlertDialog.Builder bld = new AlertDialog.Builder(ctx);
                                bld.setTitle("Konfirmasi");
                                bld.setCancelable(true);
                                bld.setMessage(MSG_AKTIVASI_CONFIRMATION);

                                bld.setPositiveButton(MSG_POSITIVE,  new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PegawaiTable TData = new PegawaiTable(ctx);
                                        if (TData.GetData(IdMSt).getStatus().equals(FALSE_STRING)){
                                            TData.aktivasi(IdMSt, TRUE_STRING);
                                        }else{
                                            TData.aktivasi(IdMSt, FALSE_STRING);
                                        }
                                        PegawaiAdapter.this.notifyDataSetChanged();
                                        ((PegawaiRekap)ctx).LoadData();
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

    public PegawaiModel getItem(int position) {
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

            final List<PegawaiModel> list = objects;

            int count = list.size();
            final ArrayList<PegawaiModel> nlist = new ArrayList<PegawaiModel>(count);

            String nik ;
            String nama ;
            Log.d("filter", filterString);
            for (int i = 0; i < count; i++) {
                nik = list.get(i).getNik();
                nama = list.get(i).getNama();
                if ((nik.toLowerCase().contains(filterString)) || (nama.toLowerCase().contains(filterString))) {
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
            filteredData = (ArrayList<PegawaiModel>) results.values;
            notifyDataSetChanged();
        }

    }
}
