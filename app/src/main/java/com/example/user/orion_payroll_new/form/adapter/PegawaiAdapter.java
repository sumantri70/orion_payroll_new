package com.example.user.orion_payroll_new.form.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.form.master.PegawaiInput;
import com.example.user.orion_payroll_new.form.master.PegawaiRekap;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.PegawaiModel;
import com.example.user.orion_payroll_new.OrionPayrollApplication;

import org.w3c.dom.Text;

import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_ACTIVE;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;

public class PegawaiAdapter extends ArrayAdapter<PegawaiModel> {
    PegawaiTable Tkaryawan;


    public PegawaiAdapter(Context context, int resource, List<PegawaiModel> object) {
        super(context, resource, object);
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

        lblNik.setText(Data.getNik());
        lblNama.setText(Data.getNama());
        lblNoTelpon.setText(Data.getTelpon1());
        lblGaji.setText(fmt.format(Data.getgaji_pokok()));

        btnAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu po = new PopupMenu(getContext(), btnAction);
                po.getMenuInflater().inflate(R.menu.menu_action_master, po.getMenu());

                po.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Detail")){
                            Intent s = new Intent(getContext(), PegawaiInput.class);
                            s.putExtra("MODE", JCons.DETAIL_MODE);
                            s.putExtra("POSITION",pos);
                            getContext().startActivity(s);
                        } else if (item.getTitle().equals("Edit")){
                            Intent s = new Intent(getContext(), PegawaiInput.class);
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
                            Toast.makeText(getContext(),MSG_SUCCESS_ACTIVE, Toast.LENGTH_SHORT).show();
                            PegawaiRekap.Data.aktivasi(Data.getId(), StatusAktivasi);
                            PegawaiRekap.Data.ReloadList(PegawaiRekap.Fstatus, PegawaiRekap.OrderBy);
                            PegawaiRekap.Adapter.notifyDataSetChanged();
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
