package com.example.user.orion_payroll_new.form.adapter;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.TunjanganTable;
import com.example.user.orion_payroll_new.form.master.TunjanganInput;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.TunjanganModel;

import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_ACTIVE;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;

public class TunjanganAdapter extends ArrayAdapter<TunjanganModel> {
    TunjanganTable TTunjangan;


    public TunjanganAdapter(Context context, int resource, List<TunjanganModel> object) {
        super(context, resource, object);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        final int pos = position;
        TTunjangan = new TunjanganTable(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        v = inflater.inflate(R.layout.list_tunjangan_rekap, null);
        final TunjanganModel Data = getItem(position);

        final int id = Data.getId();
        TextView lblKode   = (TextView) v.findViewById(R.id.lblNik);
        TextView lblNama  = (TextView) v.findViewById(R.id.lblNama);
        final ImageButton btnAction = (ImageButton) v.findViewById(R.id.btnAction);

        lblKode.setText(Data.getKode());
        lblNama.setText(Data.getNama());

        btnAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu po = new PopupMenu(getContext(), btnAction);
                po.getMenuInflater().inflate(R.menu.menu_action_master, po.getMenu());

                po.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Edit")){
                            Intent s = new Intent(getContext(), TunjanganInput.class);
                            s.putExtra("MODE", JCons.EDIT_MODE);
                            s.putExtra("POSITION",pos);
                            getContext().startActivity(s);
                        } else if (item.getTitle().equals("Aktivasi")){
                            String StatusAktivasi = "";
                            if (Data.getStatus().equals(TRUE_STRING)){
                                StatusAktivasi = FALSE_STRING;
                            } else if (Data.getStatus().equals(FALSE_STRING)){
                                StatusAktivasi = TRUE_STRING;
                            }
                            TTunjangan.aktivasi(Data.getId(), StatusAktivasi);
                            Toast.makeText(getContext(),MSG_SUCCESS_ACTIVE, Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                        return false;
                    }
                });
                po.show();
            }
        });
        return v;
    }
}
