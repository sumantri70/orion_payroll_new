package com.orionit.app.orion_payroll_new.form.transaksi;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.models.PenggajianDetailModel;
import com.orionit.app.orion_payroll_new.utility.FormatNumber;

import java.util.List;

import static com.orionit.app.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.orionit.app.orion_payroll_new.utility.FormatNumber.fmt;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormat;

public class PilihKasbonPenggajianAdapterNew extends RecyclerView.Adapter {
    private Context ctx;
    private List<PenggajianDetailModel> objects;
    private String Mode;

    public PilihKasbonPenggajianAdapterNew(Context context, List<PenggajianDetailModel> object, String mode ) {
        this.ctx = context;
        this.objects = object;
        this.Mode = mode;
    }

    public void addModels(List<PenggajianDetailModel> Datas) {
        int pos = this.objects.size();
        this.objects.addAll(Datas);
        notifyItemRangeInserted(pos, Datas.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.list_pilih_kasbon_penggajian, parent, false);
        return new ItemHolder(row);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PenggajianDetailModel Data = objects.get(position);
        final ItemHolder itemHolder = (ItemHolder) holder;

        final boolean Enabled = !Mode.equals(DETAIL_MODE);

        itemHolder.Hkode.setText(Data.getNomor());
        itemHolder.Hnama.setText(getTglFormat(Data.getTanggal()));
        itemHolder.HlblSisa.setText(fmt.format(Data.getSisa()));
        itemHolder.Hjumlah.setText(fmt.format(Data.getJumlah()));
        itemHolder.HchbKasbon.setChecked(Data.isCheck());
        itemHolder.Hjumlah.setId(position);
        itemHolder.HchbKasbon.setId(position);

        if (Mode.equals(DETAIL_MODE)){
            itemHolder.HchbKasbon.setVisibility(View.INVISIBLE);
        }

        itemHolder.HchbKasbon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                Data.setCheck(isChecked);
                itemHolder.HchbKasbon.setChecked(isChecked);

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
                itemHolder.Hjumlah.setText(fmt.format(Data.getJumlah()));
            }
        });

        itemHolder.Hjumlah.setOnClickListener(new View.OnClickListener() {
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
                        itemHolder.Hjumlah.setText(txtInput.getText());
                        Data.setJumlah(StrFmtToDouble(txtInput.getText().toString()));
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        Data.setCheck(Data.getJumlah() > 0);
                        itemHolder.HchbKasbon.setChecked(Data.getJumlah() > 0);

                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        Data.setCheck(Data.getJumlah() > 0);
                        itemHolder.HchbKasbon.setChecked(Data.getJumlah() > 0);
                        dialog.cancel();
                    }
                });

                if ((Enabled) && (itemHolder.HchbKasbon.isChecked() == true))   {
                    builder.show();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView Hkode;
        private TextView Hnama;
        public EditText Hjumlah;
        public CheckBox HchbKasbon;
        public TextView HlblSisa;

        public ItemHolder(View itemView) {
            super(itemView);
            Hkode = itemView.findViewById(R.id.lblItem1);
            Hnama = itemView.findViewById(R.id.lblItem2);
            Hjumlah = itemView.findViewById(R.id.txtJumlah);
            HchbKasbon = itemView.findViewById(R.id.chbKasbon);
            HlblSisa = itemView.findViewById(R.id.lblSisa);
        }
    }

}
