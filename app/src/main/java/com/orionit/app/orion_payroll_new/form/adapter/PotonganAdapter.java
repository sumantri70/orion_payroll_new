package com.orionit.app.orion_payroll_new.form.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.database.master.PotonganTable;
import com.orionit.app.orion_payroll_new.form.master.PotonganInput;
import com.orionit.app.orion_payroll_new.form.master.PotonganRekap;
import com.orionit.app.orion_payroll_new.models.JCons;
import com.orionit.app.orion_payroll_new.models.PotonganModel;

import java.util.ArrayList;
import java.util.List;

import static com.orionit.app.orion_payroll_new.models.JCons.EDIT_MODE;
import static com.orionit.app.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_AKTIVASI_CONFIRMATION;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_CANT_AKTIVASI;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_CANT_EDIT;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_NEGATIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_POSITIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SAVE_CONFIRMATION;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SUCCESS_SAVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SUCCESS_UPDATE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_UNSUCCESS_SAVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_UNSUCCESS_UPDATE;
import static com.orionit.app.orion_payroll_new.models.JCons.TRUE_STRING;

public class PotonganAdapter extends ArrayAdapter<PotonganModel> {
    private ProgressDialog Loading;
    private Context ctx;
    private List<PotonganModel> objects;
    private List<PotonganModel> filteredData;
    private PotonganAdapter.ItemFilter mFilter = new PotonganAdapter.ItemFilter();

    public PotonganAdapter(Context context, int resource, List<PotonganModel> object) {
        super(context, resource, object);
        this.ctx = context;
        this.objects = object;
        this.filteredData = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        v = inflater.inflate(R.layout.list_potongan_rekap, null);
        final PotonganModel Data = getItem(position);

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
                                Intent s = new Intent(getContext(), PotonganInput.class);
                                s.putExtra("MODE", JCons.DETAIL_MODE);
                                s.putExtra("ID",IdMSt);
                                getContext().startActivity(s);
                            } else if (item.getTitle().equals("Edit")){
                                if (Data.getCan_delete().equals(FALSE_STRING)){
                                    Toast.makeText(ctx, MSG_CANT_EDIT, Toast.LENGTH_SHORT).show();
                                    return true;
                                }

                                Intent s = new Intent(getContext(), PotonganInput.class);
                                s.putExtra("MODE", JCons.EDIT_MODE);
                                s.putExtra("ID",IdMSt);
                                ((PotonganRekap)ctx).startActivityForResult(s, 1);
                            } else if (item.getTitle().equals("Aktivasi")){
                                if (Data.getCan_delete().equals(FALSE_STRING)){
                                    Toast.makeText(ctx, MSG_CANT_AKTIVASI, Toast.LENGTH_SHORT).show();
                                    return true;
                                }

                                AlertDialog.Builder bld = new AlertDialog.Builder(ctx);
                                bld.setTitle("Konfirmasi");
                                bld.setCancelable(true);
                                bld.setMessage(MSG_AKTIVASI_CONFIRMATION);

                                bld.setPositiveButton(MSG_POSITIVE,  new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PotonganTable TData = new PotonganTable(ctx);
                                        if (TData.GetData(IdMSt).getStatus().equals(FALSE_STRING)){
                                            TData.aktivasi(IdMSt, TRUE_STRING);
                                        }else{
                                            TData.aktivasi(IdMSt, FALSE_STRING);
                                        }
                                        PotonganAdapter.this.notifyDataSetChanged();
                                        ((PotonganRekap)ctx).LoadData();
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

    public PotonganModel getItem(int position) {
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

            final List<PotonganModel> list = objects;

            int count = list.size();
            final ArrayList<PotonganModel> nlist = new ArrayList<PotonganModel>(count);

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
            filteredData = (ArrayList<PotonganModel>) results.values;
            notifyDataSetChanged();
        }

    }
}
