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
import com.orionit.app.orion_payroll_new.models.LaporanKasbonModel;

import java.util.ArrayList;
import java.util.List;

import static com.orionit.app.orion_payroll_new.utility.FormatNumber.fmt;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormat;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;

public class LaporanKasbonAdapter extends ArrayAdapter<LaporanKasbonModel> implements Filterable {
    private ProgressDialog Loading;
    private Context ctx;
    private List<LaporanKasbonModel> objects;
    private List<LaporanKasbonModel> filteredData;
    private LaporanKasbonAdapter.ItemFilter mFilter = new LaporanKasbonAdapter.ItemFilter();

    public LaporanKasbonAdapter(Context context, int resource, List<LaporanKasbonModel> object) {
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
        v = inflater.inflate(R.layout.list_laporan_kasbon, null);
        final LaporanKasbonModel Data = getItem(position);

        TextView lblPegawai    = (TextView) v.findViewById(R.id.lblPegawai);
        TextView lblTanggal = (TextView) v.findViewById(R.id.lblTanggal);
        TextView lblNomor = (TextView) v.findViewById(R.id.lblNomor);
        TextView lblJumlah = (TextView) v.findViewById(R.id.lblJumlah);
        TextView lblLamaCicilan = (TextView) v.findViewById(R.id.lblLamaCicilan);
        TextView lblTerbayar = (TextView) v.findViewById(R.id.lblTerbayar);
        TextView lblSisa = (TextView) v.findViewById(R.id.lblSisa);

        lblTanggal.setText(getTglFormat(Data.getTanggal()));
        if (Data.getId_pegawai() > 0){
            lblPegawai.setText(Get_Nama_Master_Pegawai(Data.getId_pegawai()));
        }

        lblNomor.setText(Data.getNomor());
        lblJumlah.setText(fmt.format(Data.getJumlah()));
        lblLamaCicilan.setText(Integer.toString(Data.getCicilan()));
        lblTerbayar.setText(fmt.format(Data.getTerbayar()));
        lblSisa.setText(fmt.format(Data.getSisa()));
        return v;
    }

    public int getCount() {
        return filteredData.size();
    }
    public LaporanKasbonModel getItem(int position) {
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

            final List<LaporanKasbonModel> list = objects;

            int count = list.size();
            final ArrayList<LaporanKasbonModel> nlist = new ArrayList<LaporanKasbonModel>(count);

            String tanggal, pegawai, nomor, jumlah, lamacicilan, terbayar, sisa;



            for (int i = 0; i < count; i++) {
                tanggal     = getTglFormat(list.get(i).getTanggal());
                pegawai     = Get_Nama_Master_Pegawai(list.get(i).getId_pegawai());
                nomor       = list.get(i).getNomor();
                jumlah      = Double.toString(list.get(i).getJumlah());
                lamacicilan = Integer.toString(list.get(i).getCicilan());
                terbayar    = Double.toString(list.get(i).getTerbayar());
                sisa        = Double.toString(list.get(i).getSisa());


                if ( (tanggal.toLowerCase().contains(filterString)) || (pegawai.toLowerCase().contains(filterString)) ||
                        (nomor.toLowerCase().contains(filterString)) ||(jumlah.toLowerCase().contains(filterString)) ||
                        (jumlah.toLowerCase().contains(filterString)) ||(lamacicilan.toLowerCase().contains(filterString)) ||
                        (sisa.toLowerCase().contains(filterString)) || (terbayar.toLowerCase().contains(filterString)))  {
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
            filteredData = (ArrayList<LaporanKasbonModel>) results.values;
            notifyDataSetChanged();
        }

    }

}
