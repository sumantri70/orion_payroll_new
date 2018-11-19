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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.form.transaksi.PilihKasbonPenggajian;
import com.example.user.orion_payroll_new.models.PenggajianDetailModel;
import com.example.user.orion_payroll_new.utility.FormatNumber;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;

import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getTglFormat;

public class PilihKasbonPenggajianAdapter extends ArrayAdapter<PenggajianDetailModel> {
    private ProgressDialog Loading;
    private Context ctx;
    private List<PenggajianDetailModel> objects;

    public PilihKasbonPenggajianAdapter(Context context, int resource, List<PenggajianDetailModel> object) {
        super(context, resource, object);
        this.ctx = context;
        this.objects = object;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PenggajianDetailModel Data = getItem(position);
        final PilihKasbonPenggajianAdapter.ViewHolder holder;
        View v = convertView;

        if (v == null) {
            holder = new PilihKasbonPenggajianAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.list_pilih_kasbon_penggajian, null);


            holder.Hkode      = (TextView) v.findViewById(R.id.lblItem1);
            holder.Hnama      = (TextView) v.findViewById(R.id.lblItem2);
            holder.Hjumlah    = (EditText) v.findViewById(R.id.txtJumlah);
            holder.HchbKasbon = (CheckBox) v.findViewById(R.id.chbKasbon);
            holder.HlblSisa   = (TextView) v.findViewById(R.id.lblSisa);
            holder.Hjumlah.addTextChangedListener(new FormatNumber(holder.Hjumlah));

            boolean Enabled = !((PilihKasbonPenggajian)ctx).Mode.equals(DETAIL_MODE);

            if (((PilihKasbonPenggajian)ctx).Mode.equals(DETAIL_MODE)){
                holder.HchbKasbon.setVisibility(View.INVISIBLE);
            }

            holder.Hkode.setText(Data.getNomor());
            holder.Hnama.setText(getTglFormat(Data.getTanggal()));
            holder.HlblSisa.setText(fmt.format(Data.getSisa()));
            holder.Hjumlah.setText(fmt.format(Data.getJumlah()));
            holder.HchbKasbon.setChecked(Data.isCheck());
            holder.Hjumlah.setId(position);
            holder.HchbKasbon.setId(position);

            holder.Hjumlah.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText Caption = (EditText) v;
                    if (!hasFocus){
                        //final EditText Caption = (EditText) v;
                        if (!Caption.getText().toString().equals("")) {
                            Data.setJumlah(StrFmtToDouble(Caption.getText().toString()));
                        }else{
                            Data.setJumlah(StrFmtToDouble("0"));
                        }
                        holder.Hjumlah.setText(fmt.format(Data.getJumlah()));
                    }
                }
            });

            holder.HchbKasbon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                    Data.setCheck(isChecked);
                    holder.HchbKasbon.setChecked(isChecked);

                    if (isChecked){
                        Double CicilanPerBln = Data.getTotal() / Data.getLama_cicilan();
                        if ( (Data.getSisa() - CicilanPerBln) < 0 ){
                            Data.setJumlah(Data.getSisa());
                        }else{
                            Data.setJumlah(CicilanPerBln);
                        }
                    }else{
                        Data.setJumlah(0.0);
                    }
                    holder.Hjumlah.setText(fmt.format(Data.getJumlah()));
                }
            });

            holder.Hjumlah.setEnabled(Enabled);
            v.setTag(holder);
        }else{
            holder = (PilihKasbonPenggajianAdapter.ViewHolder)v.getTag();
            //v.setTag(holder);
        }

        return v;
    }

    protected void LostFocus(){
        ((PilihKasbonPenggajian)ctx).txtTmp.setVisibility(View.VISIBLE);
        ((PilihKasbonPenggajian)ctx).txtTmp.requestFocus();
        ((PilihKasbonPenggajian)ctx).txtTmp.setVisibility(View.INVISIBLE);
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
        public CheckBox HchbKasbon;
        public TextView HlblSisa;
    }

}

