package com.orionit.app.orion_payroll_new.form.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.models.TunjanganModel;

import java.util.ArrayList;
import java.util.List;

public class lov_tunjangan_adapter extends ArrayAdapter<TunjanganModel> {
    private ProgressDialog Loading;
    private Context ctx;
    private List<TunjanganModel> objects;
    private List<TunjanganModel> filteredData;
    private lov_tunjangan_adapter.ItemFilter mFilter = new lov_tunjangan_adapter.ItemFilter();

    public lov_tunjangan_adapter(Context context, int resource, List<TunjanganModel> object) {
        super(context, resource, object);
        this.ctx = context;
        this.objects = object;
        this.filteredData = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        v = inflater.inflate(R.layout.list_lov_tunjangan, null);
        final TunjanganModel Data = getItem(position);

        TextView lblNik  = (TextView) v.findViewById(R.id.lblKode);
        TextView lblNama = (TextView) v.findViewById(R.id.lblNama);


        final int IdMSt = Data.getId();
        lblNik.setText(Data.getKode());
        lblNama.setText(Data.getNama());
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
