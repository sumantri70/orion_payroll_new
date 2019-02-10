package com.example.user.orion_payroll_new.form.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.TunjanganTable;
import com.example.user.orion_payroll_new.form.master.TunjanganRekap;
import com.example.user.orion_payroll_new.form.master.TunjanganInput;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_ACTIVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_ACTIVE;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.route.URL_AKTIVASI_TUNJANGAN;

public class TunjanganAdapter extends ArrayAdapter<TunjanganModel> {
    private ProgressDialog Loading;
    private Context ctx;
    private List<TunjanganModel> objects;
    private List<TunjanganModel> filteredData;
    private TunjanganAdapter.ItemFilter mFilter = new TunjanganAdapter.ItemFilter();

    public TunjanganAdapter(Context context, int resource, List<TunjanganModel> object) {
        super(context, resource, object);
        this.ctx = context;
        this.objects = object;
        this.filteredData = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        v = inflater.inflate(R.layout.list_tunjangan_rekap, null);
        final TunjanganModel Data = getItem(position);

        TextView lblNik  = (TextView) v.findViewById(R.id.lblKode);
        TextView lblNama = (TextView) v.findViewById(R.id.lblNama);

        final ImageButton btnAction = (ImageButton) v.findViewById(R.id.btnAction);

        if (Data.getStatus() == "HIDE"){
            btnAction.setVisibility(View.GONE);
        }
        final int IdMSt = Data.getId();
        lblNik.setText(Data.getKode());
        lblNama.setText(Data.getNama());

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
                                Intent s = new Intent(getContext(), TunjanganInput.class);
                                s.putExtra("MODE", JCons.DETAIL_MODE);
                                s.putExtra("ID",IdMSt);
                                getContext().startActivity(s);
                            } else if (item.getTitle().equals("Edit")){
                                Intent s = new Intent(getContext(), TunjanganInput.class);
                                s.putExtra("MODE", JCons.EDIT_MODE);
                                s.putExtra("ID",IdMSt);
                                ((TunjanganRekap)ctx).startActivityForResult(s, 1);
                            } else if (item.getTitle().equals("Aktivasi")){
                                TunjanganTable TData = new TunjanganTable(ctx);
                                TData.aktivasi(IdMSt, FALSE_STRING);
                                ((TunjanganRekap)ctx).LoadData();
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

    public TunjanganModel getItem(int position) {
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

            final List<TunjanganModel> list = objects;

            int count = list.size();
            final ArrayList<TunjanganModel> nlist = new ArrayList<TunjanganModel>(count);

            String kode ;
            String nama ;
            for (int i = 0; i < count; i++) {
                kode = list.get(i).getKode();
                nama = list.get(i).getNama();
                if ((kode.toLowerCase().contains(filterString)) || (nama.toLowerCase().contains(filterString))) {
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
            filteredData = (ArrayList<TunjanganModel>) results.values;
            notifyDataSetChanged();
        }

    }

}
