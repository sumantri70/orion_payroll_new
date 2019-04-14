package com.orionit.app.orion_payroll_new.form.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import android.widget.TextView;

import com.orionit.app.orion_payroll_new.OrionPayrollApplication;
import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.models.PenggajianModel;

import java.util.ArrayList;
import static com.orionit.app.orion_payroll_new.utility.FormatNumber.fmt;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormat;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;

public class PilihKirimEmailAdapter extends ArrayAdapter<PenggajianModel> implements Filterable {

    private ProgressDialog Loading;
    public Context ctx;
    private java.util.List<PenggajianModel> objects;
    private java.util.List<PenggajianModel> filteredData;
    private PilihKirimEmailAdapter.ItemFilter mFilter = new PilihKirimEmailAdapter.ItemFilter();


    public PilihKirimEmailAdapter(Context context, int resource, java.util.List<PenggajianModel> object) {
        super(context, resource, object);
        this.ctx = context;
        this.objects = object;
        this.filteredData = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PenggajianModel Data = getItem(position);
        final PilihKirimEmailAdapter.ViewHolder holder;
        View v = convertView;

        if (v == null) {
            holder = new PilihKirimEmailAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.list_item_kirim_email, null);

            holder.HlblNomor = (TextView) v.findViewById(R.id.lblNomor);
            holder.HlblTanggal = (TextView) v.findViewById(R.id.lblTanggal);
            holder.HlblPegawai = (TextView) v.findViewById(R.id.lblPegawai);
            holder.HlblJumlah = (TextView) v.findViewById(R.id.lblJumlah);
            holder.HchbPilih = (CheckBox) v.findViewById(R.id.chbPilih);
            holder.Hvisible = Data.getUser_id();

            holder.HlblNomor.setText(Data.getNomor());
            holder.HlblTanggal.setText(getTglFormat(Data.getTanggal()));
            holder.HlblJumlah.setText(fmt.format(Data.getTotal()));
            holder.HchbPilih.setChecked(Data.isPilih());
            holder.Hvisible = Data.getUser_id();

            holder.HchbPilih.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                    Data.setPilih(isChecked);
                    holder.HchbPilih.setChecked(isChecked);
                }
            });


            if (Data.getUser_id().equals("HIDE")){
                holder.HlblTanggal.setText("");
                holder.HlblJumlah.setText("");
                holder.HlblPegawai.setText("");
                holder.HchbPilih.setVisibility(View.GONE);
            }

            if (Data.getId_pegawai() > 0) {
                holder.HlblPegawai.setText(OrionPayrollApplication.getInstance().ListHashPegawaiGlobal.get(Integer.toString(Data.getId_pegawai())).getNama());
            }
            v.setTag(holder);
        }else{
            holder = (PilihKirimEmailAdapter.ViewHolder)v.getTag();
            v.setTag(holder);
        }

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

    public class ViewHolder {
        private TextView HlblNomor;
        private TextView HlblTanggal;
        private TextView HlblPegawai;
        public TextView HlblJumlah;
        public CheckBox HchbPilih;
        public String Hvisible;

    }

}