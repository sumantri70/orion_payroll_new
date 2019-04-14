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
import android.widget.Filterable;
import android.widget.TextView;

import com.orionit.app.orion_payroll_new.R;

import com.orionit.app.orion_payroll_new.models.AbsenPegawaiModel;

import java.util.ArrayList;
import java.util.List;

import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormatCustom;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;

public class AbsenPegawaiAdapter extends ArrayAdapter<AbsenPegawaiModel> implements Filterable {
    private ProgressDialog Loading;
    private Context ctx;
    private List<AbsenPegawaiModel> objects;
    private List<AbsenPegawaiModel> filteredData;
    private AbsenPegawaiAdapter.ItemFilter mFilter = new AbsenPegawaiAdapter.ItemFilter();

    public AbsenPegawaiAdapter(Context context, int resource, List<AbsenPegawaiModel> object) {
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
        v = inflater.inflate(R.layout.list_absen_pegawai, null);
        final AbsenPegawaiModel Data = getItem(position);


        TextView lblPegawai = (TextView) v.findViewById(R.id.lblPegawai);
        TextView lblPeriode = (TextView) v.findViewById(R.id.lblPeriode);
        TextView lblMasuk = (TextView) v.findViewById(R.id.lblMasuk);
        TextView lblKurang15 = (TextView) v.findViewById(R.id.lblKurang15);
        TextView lblLebih15 = (TextView) v.findViewById(R.id.lblLebih15);
        TextView lblStghHari = (TextView) v.findViewById(R.id.lblStghHari);
        TextView lblDokter = (TextView) v.findViewById(R.id.lblDokter);
        TextView lblNonCuti = (TextView) v.findViewById(R.id.lblNonCuti);
        TextView lblCuti = (TextView) v.findViewById(R.id.lblCuti);


        lblPegawai.setText(Get_Nama_Master_Pegawai(Data.getId_pegawai()));
        lblPeriode.setText(getTglFormatCustom(Data.getTanggal(), "MMMM yyyy"));
        lblMasuk.setText(Integer.toString(Data.getMasuk()));
        lblKurang15.setText(Integer.toString(Data.getTelat_satu()));
        lblLebih15.setText(Integer.toString(Data.getTelat_dua()));
        lblStghHari.setText(Integer.toString(Data.getIzin_stgh_hari()));
        lblDokter.setText(Integer.toString(Data.getDokter()));
        lblNonCuti.setText(Integer.toString(Data.getIzin_non_cuti()));
        lblCuti.setText(Integer.toString(Data.getIzin_cuti()));

        return v;
    }

    public int getCount() {
        return filteredData.size();
    }

    public AbsenPegawaiModel getItem(int position) {
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

            final List<AbsenPegawaiModel> list = objects;

            int count = list.size();
            final ArrayList<AbsenPegawaiModel> nlist = new ArrayList<AbsenPegawaiModel>(count);

            String periode, masuk, kurang15, lebih15, stghhari, dokter, nonCuti, cuti, pegawai;

            for (int i = 0; i < count; i++) {
                pegawai = Get_Nama_Master_Pegawai(list.get(i).getId_pegawai());
                periode    = getTglFormatCustom(list.get(i).getTanggal(), "MMMM yyyy");
                masuk = Integer.toString(list.get(i).getMasuk());
                kurang15 = Integer.toString(list.get(i).getTelat_satu());
                lebih15 = Integer.toString(list.get(i).getTelat_dua());
                stghhari = Integer.toString(list.get(i).getIzin_stgh_hari());
                dokter = Integer.toString(list.get(i).getDokter());
                nonCuti = Integer.toString(list.get(i).getIzin_non_cuti());
                cuti = Integer.toString(list.get(i).getIzin_cuti());

                if ( (periode.toLowerCase().contains(filterString)) || (masuk.toLowerCase().contains(filterString)) ||
                        (kurang15.toLowerCase().contains(filterString)) ||(lebih15.toLowerCase().contains(filterString)) ||
                        (stghhari.toLowerCase().contains(filterString)) ||(dokter.toLowerCase().contains(filterString)) ||
                        (nonCuti.toLowerCase().contains(filterString)) || (cuti.toLowerCase().contains(filterString)) ||
                        (pegawai.toLowerCase().contains(filterString))){
                    nlist.add(list.get(i));
                }
            }
            results.values = nlist;
            results.count  = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<AbsenPegawaiModel>) results.values;
            notifyDataSetChanged();
        }

    }

}
