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
import android.widget.Filterable;
import android.widget.TextView;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.models.LaporanPenggajianModel;
import java.util.ArrayList;
import java.util.List;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getTglFormatCustom;

public class LaporanPenggajianAdapter extends ArrayAdapter<LaporanPenggajianModel> implements Filterable {
    private ProgressDialog Loading;
    private Context ctx;
    private List<LaporanPenggajianModel> objects;
    private List<LaporanPenggajianModel> filteredData;
    private LaporanPenggajianAdapter.ItemFilter mFilter = new LaporanPenggajianAdapter.ItemFilter();

    public LaporanPenggajianAdapter(Context context, int resource, List<LaporanPenggajianModel> object) {
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
        v = inflater.inflate(R.layout.list_laporan_penggajian, null);
        final LaporanPenggajianModel Data = getItem(position);

        TextView lblPeriode    = (TextView) v.findViewById(R.id.lblPeriode);
        TextView lblGajiPokok = (TextView) v.findViewById(R.id.lblGajiPokok);
        TextView lblTotTunjangan = (TextView) v.findViewById(R.id.lblTotTunjangan);
        TextView lblTotPotongan = (TextView) v.findViewById(R.id.lblTotPotongan);
        TextView lblTotKasbon = (TextView) v.findViewById(R.id.lblTotKasbon);
        TextView lblTotLembur = (TextView) v.findViewById(R.id.lblTotLembur);
        TextView lblTotal = (TextView) v.findViewById(R.id.lblTotal);


        lblPeriode.setText(getTglFormatCustom(Data.getPeriode(), "MMMM yyyy"));
        lblGajiPokok.setText(fmt.format(Data.getGaji_pokok()));
        lblTotTunjangan.setText(fmt.format(Data.getTunjangan()));
        lblTotPotongan.setText(fmt.format(Data.getPotongan()));
        lblTotKasbon.setText(fmt.format(Data.getKasbon()));
        lblTotLembur.setText(fmt.format(Data.getLembur()));
        lblTotal.setText(fmt.format(Data.getTotal()));

        return v;
    }

    public int getCount() {
        return filteredData.size();
    }

    public LaporanPenggajianModel getItem(int position) {
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

            final List<LaporanPenggajianModel> list = objects;

            int count = list.size();
            final ArrayList<LaporanPenggajianModel> nlist = new ArrayList<LaporanPenggajianModel>(count);

            String periode, gaji_pokok, tunjangan, potongan, kasbon, lembur, total;

            for (int i = 0; i < count; i++) {
                periode    = getTglFormatCustom(list.get(i).getPeriode(), "MMMM yyyy");
                gaji_pokok = Double.toString(list.get(i).getGaji_pokok()) ;
                tunjangan  = Double.toString(list.get(i).getTunjangan()) ;
                potongan   = Double.toString(list.get(i).getPotongan()) ;
                kasbon     = Double.toString(list.get(i).getKasbon()) ;
                lembur     = Double.toString(list.get(i).getLembur()) ;
                total      = Double.toString(list.get(i).getTotal()) ;
                if ( (periode.toLowerCase().contains(filterString)) || (gaji_pokok.toLowerCase().contains(filterString)) ||
                     (tunjangan.toLowerCase().contains(filterString)) ||(potongan.toLowerCase().contains(filterString)) ||
                     (kasbon.toLowerCase().contains(filterString)) ||(lembur.toLowerCase().contains(filterString)) ||
                     (total.toLowerCase().contains(filterString)))  {
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
            filteredData = (ArrayList<LaporanPenggajianModel>) results.values;
            notifyDataSetChanged();
        }

    }

}
