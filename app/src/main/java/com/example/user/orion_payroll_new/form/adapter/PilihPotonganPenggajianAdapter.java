package com.example.user.orion_payroll_new.form.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.form.transaksi.PilihPotonganPenggajian;
import com.example.user.orion_payroll_new.models.PenggajianDetailModel;
import com.example.user.orion_payroll_new.utility.FormatNumber;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;

import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Kode_Master_Potongan;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Nama_Master_Potongan;

public class PilihPotonganPenggajianAdapter extends ArrayAdapter<PenggajianDetailModel> {
    private ProgressDialog Loading;
    private Context ctx;
    private List<PenggajianDetailModel> objects;
    private Dialog DialogInput;

    public PilihPotonganPenggajianAdapter(Context context, int resource, List<PenggajianDetailModel> object) {
        super(context, resource, object);
        this.ctx = context;
        this.objects = object;
        this.DialogInput = new Dialog(ctx);
        this.DialogInput.setContentView(R.layout.input_angka);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PenggajianDetailModel Data = getItem(position);
        final PilihPotonganPenggajianAdapter.ViewHolder holder;
        View v = convertView;

        boolean Enabled = !((PilihPotonganPenggajian)ctx).Mode.equals(DETAIL_MODE);
        if (v == null) {
            holder = new PilihPotonganPenggajianAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.list_pilih_potongan_penggajian, null);


            holder.Hkode      = (TextView) v.findViewById(R.id.lblItem1);
            holder.Hnama      = (TextView) v.findViewById(R.id.lblItem2);
            holder.Hjumlah    = (EditText) v.findViewById(R.id.txtJumlah);
            holder.HbtnHapus  = (ImageButton)v.findViewById(R.id.btnAction);
            holder.Hjumlah.addTextChangedListener(new FormatNumber(holder.Hjumlah));

            if (((PilihPotonganPenggajian)ctx).Mode.equals(DETAIL_MODE)){
                holder.HbtnHapus.setVisibility(View.INVISIBLE);
                holder.HchbKasbon.setVisibility(View.INVISIBLE);
            }

            //holder.Hjumlah.setEnabled(Enabled);
            v.setTag(holder);
        }else{
            holder = (PilihPotonganPenggajianAdapter.ViewHolder)v.getTag();
        }

        holder.Hkode.setText(Get_Kode_Master_Potongan(Data.getId_tjg_pot_kas()));
        holder.Hnama.setText(Get_Nama_Master_Potongan(Data.getId_tjg_pot_kas()));
        holder.Hjumlah.setText(fmt.format(Data.getJumlah()));
        holder.Hjumlah.setId(position);

//        holder.Hjumlah.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                final EditText Caption = (EditText) v;
//                if (!hasFocus){
//                    //final EditText Caption = (EditText) v;
//                    if (!Caption.getText().toString().equals("")) {
//                        Data.setJumlah(StrFmtToDouble(Caption.getText().toString()));
//                    }else{
//                        Data.setJumlah(StrFmtToDouble("0"));
//                    }
//                    holder.Hjumlah.setText(FungsiGeneral.FloatToStrFmt(Data.getJumlah()));
//                    holder.Hjumlah.setText(fmt.format(Data.getJumlah()));
//                }
//            }
//        });


        final int idx = position;

        holder.HbtnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LostFocus();
                ((PilihPotonganPenggajian)ctx).ListPotongan.remove(idx);
                ((PilihPotonganPenggajian)ctx).Adapter.notifyDataSetChanged();
            }
        });

        if (Enabled){
            holder.Hjumlah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("Masukan jumlah");

                    final View input = View.inflate(ctx, R.layout.input_angka,null);
                    final EditText txtInput = (EditText) input.findViewById(R.id.txtInput);
                    txtInput.addTextChangedListener(new FormatNumber(txtInput));
                    txtInput.setText(fmt.format(Data.getJumlah()));
                    builder.setView(input);

                    builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            holder.Hjumlah.setText(txtInput.getText());
                            Data.setJumlah(StrFmtToDouble(txtInput.getText().toString()));
                            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        }
                    });
                    builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            });
        }
        return v;
    }

    protected void LostFocus(){
        ((PilihPotonganPenggajian)ctx).txtTmp.setVisibility(View.VISIBLE);
        ((PilihPotonganPenggajian)ctx).txtTmp.requestFocus();
        ((PilihPotonganPenggajian)ctx).txtTmp.setVisibility(View.INVISIBLE);
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

