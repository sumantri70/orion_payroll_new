package com.example.user.orion_payroll_new.form.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.form.transaksi.PenggajianInputNew;
import com.example.user.orion_payroll_new.models.PegawaiModel;
import com.example.user.orion_payroll_new.models.PenggajianDetailModel;
import com.example.user.orion_payroll_new.utility.FormatNumber;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Kode_Master_Tunjangan;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Nama_Master_Tunjangan;

public class PilihTunjanganPenggajianAdapter extends ArrayAdapter<PenggajianDetailModel> {
    private ProgressDialog Loading;
    private Context ctx;
    private List<PenggajianDetailModel> objects;

    public PilihTunjanganPenggajianAdapter(Context context, int resource, List<PenggajianDetailModel> object) {
        super(context, resource, object);
        this.ctx = context;
        this.objects = object;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PenggajianDetailModel Data = getItem(position);
        final PilihTunjanganPenggajianAdapter.ViewHolder holder;
        View v = convertView;

        if (v == null) {
            holder = new PilihTunjanganPenggajianAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.list_pilih_tunjangan_penggajian, null);


            holder.Hkode      = (TextView) v.findViewById(R.id.lblItem1);
            holder.Hnama      = (TextView) v.findViewById(R.id.lblItem2);
            holder.Hjumlah    = (EditText) v.findViewById(R.id.txtJumlah);
            holder.HbtnHapus  = (ImageButton)v.findViewById(R.id.btnAction);
            holder.Hjumlah.addTextChangedListener(new FormatNumber(holder.Hjumlah));

            boolean Enabled = !((PenggajianInputNew)ctx).Mode.equals(DETAIL_MODE);

            if (((PenggajianInputNew)ctx).Mode.equals(DETAIL_MODE)){
                holder.HbtnHapus.setVisibility(View.INVISIBLE);
                holder.HchbKasbon.setVisibility(View.INVISIBLE);
            }

            holder.Hjumlah.setEnabled(Enabled);
            v.setTag(holder);
        }else{
            holder = (PilihTunjanganPenggajianAdapter.ViewHolder)v.getTag();
        }

        holder.Hkode.setText(Get_Kode_Master_Tunjangan(Data.getId_tjg_pot_kas()));
        holder.Hnama.setText(Get_Nama_Master_Tunjangan(Data.getId_tjg_pot_kas()));
        holder.Hjumlah.setText(fmt.format(Data.getJumlah()));
        holder.Hjumlah.setId(position);

        holder.Hjumlah.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus){
                final EditText Caption = (EditText) v;
                if (!Caption.getText().toString().equals("")) {
                    Data.setJumlah(StrFmtToDouble(Caption.getText().toString()));
                }else{
                    Data.setJumlah(StrFmtToDouble("0"));
                }
                holder.Hjumlah.setText(FungsiGeneral.FloatToStrFmt(Data.getJumlah()));
                holder.Hjumlah.setText(fmt.format(Data.getJumlah()));
            }
            }
        });

        final int idx = position;

        holder.HbtnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LostFocus();
                ((PenggajianInputNew)ctx).ArListTunjangan.remove(idx);
            }
        });
        return v;
    }

    protected void LostFocus(){
        ((PenggajianInputNew)ctx).txtTmp.setVisibility(View.VISIBLE);
        ((PenggajianInputNew)ctx).txtTmp.requestFocus();
        ((PenggajianInputNew)ctx).txtTmp.setVisibility(View.INVISIBLE);
    }

    public int getCount() {
        return objects.size();
    }

    public PenggajianDetailModel getItem(int position) {
        return objects.get(position);
    }


    public class ViewHolder {
        private TextView Hkode;
        private TextView Hnama;
        public EditText Hjumlah;
        public ImageButton HbtnHapus;
        public CheckBox HchbKasbon;
        public TextView HlblSisa;
    }

}

