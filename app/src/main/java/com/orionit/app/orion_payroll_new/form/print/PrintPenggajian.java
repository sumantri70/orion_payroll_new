package com.orionit.app.orion_payroll_new.form.print;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Image;
import com.orionit.app.orion_payroll_new.database.master.KasbonPegawaiTable;
import com.orionit.app.orion_payroll_new.database.master.PenggajianDetailTable;
import com.orionit.app.orion_payroll_new.database.master.PenggajianTable;
import com.orionit.app.orion_payroll_new.email.MainActivity;
import com.orionit.app.orion_payroll_new.email.SendMail;
import com.orionit.app.orion_payroll_new.models.PenggajianDetailModel;
import com.orionit.app.orion_payroll_new.models.PenggajianModel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.orionit.app.orion_payroll_new.models.JCons.TIPE_DET_KASBON;
import static com.orionit.app.orion_payroll_new.models.JCons.TIPE_DET_POTONGAN;
import static com.orionit.app.orion_payroll_new.models.JCons.TIPE_DET_TUNJANGAN;
import static com.orionit.app.orion_payroll_new.utility.FormatNumber.fmt;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getSimpleDate;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormat;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormatCustom;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Email_Master_Pegawai;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Potongan;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Tunjangan;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nik_Master_Pegawai;

public class PrintPenggajian {
        private ProgressDialog Loading;
        private PenggajianModel DtPenggajian;
        private List<PenggajianDetailModel> ArListTunjangan;
        private List<PenggajianDetailModel> ArListPotongan;
        private List<PenggajianDetailModel> ArListKasbon;

        Context context;
        private static final String TAG = "PdfCreatorActivity";
        private File pdfFile;
        private String NamaPrint;
        private PenggajianTable DbMaster;
        private Double SisaKasbon;


        public PrintPenggajian(Context ctx) {
            Loading = new ProgressDialog(ctx);
            context = ctx;
            DbMaster = new PenggajianTable(ctx);
            ArListTunjangan = new ArrayList<>();
            ArListPotongan  = new ArrayList<>();
            ArListKasbon    = new ArrayList<>();
        }

        public void Execute(final int IdMst){
            Loading.setMessage("Loading...");
            Loading.setCancelable(false);
            Loading.show();

            LoadData(IdMst);

            try {
                CreatePrdf();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            Loading.dismiss();
        }

    private void CreatePrdf() throws FileNotFoundException, DocumentException {
            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/orion_payroll/documents");
            if (!docsFolder.exists()) {
                docsFolder.mkdir();
                Log.i(TAG, "Created a new directory for PDF");
            }

            Font fontNormal = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
            Font fontBold   = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font fontBoldHeader = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);

            this.NamaPrint = Get_Nama_Master_Pegawai(DtPenggajian.getId_pegawai())+"-"+getTglFormatCustom(DtPenggajian.getPeriode(),"MMMM-yyyy");


            pdfFile = new File(docsFolder.getAbsolutePath(),NamaPrint+".pdf");
            OutputStream output = new FileOutputStream(pdfFile);
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            PdfWriter.getInstance(document, output);
            document.setPageSize(PageSize.A6);
            document.open();

            Chunk glue = new Chunk(new VerticalPositionMark());


//            InputStream inputStream = context.getAssets().open("launchscreen.png");
//            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            Image signature;
//            signature = Image.getInstance(stream.toByteArray());
//            signature.setAbsolutePosition(400f, 150f);
//            signature.scalePercent(100f);
//            document.add(signature);
//
            LineSeparator line = new LineSeparator(1,100,null, Element.ALIGN_CENTER,-4);

            Paragraph h1 = new Paragraph("Orion IT Solution", fontBoldHeader);
            h1.add(new Chunk(glue));
            h1.add("Slip Gaji");

            document.add(h1);
            document.add(line);

            Paragraph pMaster = new Paragraph("\n",fontNormal);
            pMaster.add("Nomor        : " + DtPenggajian.getNomor()+"\n");
            pMaster.add("Tanggal      : " + getTglFormat(DtPenggajian.getTanggal())+"\n");//sementara
            pMaster.add("Periode       : " + getTglFormatCustom(DtPenggajian.getPeriode(), "MMMM yyyy")+"\n");//sementara
            pMaster.add("Nik/Nama   : " + Get_Nik_Master_Pegawai(DtPenggajian.getId_pegawai())+"|"+Get_Nama_Master_Pegawai(DtPenggajian.getId_pegawai())+"\n\n");
            document.add(pMaster);

            Paragraph pKosong = new Paragraph(" ");

            Paragraph p = new Paragraph("Gaji Pokok", fontBold);
            p.add(new Chunk(glue));
            p.add(fmt.format(DtPenggajian.getGaji_pokok()));
            document.add(p);

            Paragraph p1 = new Paragraph("Total Tunjangan",fontBold);
            p1.add(new Chunk(glue));
            p1.add(fmt.format(DtPenggajian.getTotal_tunjangan() + DtPenggajian.getTotal_lembur()));
            document.add(p1);

            String jarakDetail = "       ";
            String jarakDetailKanan = "          ";

            for (int i = 0; i < ArListTunjangan.size(); i++){
                Paragraph pd = new Paragraph(jarakDetail + Get_Nama_Master_Tunjangan(ArListTunjangan.get(i).getId_tjg_pot_kas()), fontNormal);
                pd.add(new Chunk(glue));
                pd.add(fmt.format(ArListTunjangan.get(i).getJumlah()) + jarakDetailKanan);
                document.add(pd);
            }

            Paragraph p2 = new Paragraph("Total Potongan",fontBold);
            p2.add(new Chunk(glue));
            p2.add(fmt.format(DtPenggajian.getTotal_potongan()));
            document.add(p2);

            for (int i = 0; i < ArListPotongan.size(); i++){
                Paragraph pd = new Paragraph(jarakDetail + Get_Nama_Master_Potongan(ArListPotongan.get(i).getId_tjg_pot_kas()), fontNormal);
                pd.add(new Chunk(glue));
                pd.add(fmt.format(ArListPotongan.get(i).getJumlah()) + jarakDetailKanan);
                document.add(pd);
            }

            Paragraph p7 = new Paragraph("Total Kasbon",fontBold);
            p7.add(new Chunk(glue));
            p7.add(fmt.format(DtPenggajian.getTotal_kasbon()));
            document.add(p7);

            document.add(line);

            Paragraph p3 = new Paragraph("Total",fontBold);
            p3.add(new Chunk(glue));
            p3.add(fmt.format(DtPenggajian.getTotal())+"\n");
            document.add(p3);


            //BAWAH----------------------------------------------------------------
            String keterangan = DtPenggajian.getKeterangan();
            if (keterangan.equals("")){
                keterangan += "Sisa kasbon : "+fmt.format(SisaKasbon);
            }else{
                keterangan += "\n"+"                      Sisa kasbon : "+fmt.format(SisaKasbon);
            }

            Paragraph p4 = new Paragraph("Keterangan : "+keterangan, fontNormal);
            document.add(p4);

            document.add(pKosong);

            Paragraph p5 = new Paragraph("            Keuangan", fontNormal);
            p5.add(new Chunk(glue));
            p5.add("Penerima             ");
            document.add(p5);

            document.add(pKosong);
            document.add(pKosong);

            Paragraph p6 = new Paragraph("      (                          )",fontNormal);
            p6.add(new Chunk(glue));
            p6.add("(                          )      ");
            document.add(p6);
            //BAWAH----------------------------------------------------------------

        if (!Get_Email_Master_Pegawai(DtPenggajian.getId_pegawai()).equals("")) {
            KirimEmail(Get_Email_Master_Pegawai(DtPenggajian.getId_pegawai()), pdfFile.getName(),pdfFile.getPath());
        }
            document.close();
        }

    protected boolean KirimEmail(String email, String subjek, String pathFile){

        SendMail sm = new SendMail(context, email, subjek, "", pathFile);

        //Executing sendmail to send email
        sm.execute();
        return true;
    }

    protected void LoadData(final int IdMst){
        DtPenggajian = new PenggajianModel();
        DtPenggajian = DbMaster.GetData(IdMst);

        PenggajianDetailTable TDataDet = new PenggajianDetailTable(context);
        List <PenggajianDetailModel> ArrDetail = new ArrayList<>();
        ArrDetail = TDataDet.GetArrList(IdMst);

        SisaKasbon = new KasbonPegawaiTable(context).GetSisaKasbon(DtPenggajian.getId_pegawai());

        PenggajianDetailModel Data;
        for (int i = 0; i < ArrDetail.size(); i++){

            Data = new PenggajianDetailModel();
            Data.setTipe(ArrDetail.get(i).getTipe());
            Data.setId_tjg_pot_kas(ArrDetail.get(i).getId_tjg_pot_kas());
            Data.setJumlah(ArrDetail.get(i).getJumlah());

            if (ArrDetail.get(i).getTipe().equals(TIPE_DET_TUNJANGAN)){
                ArListTunjangan.add(Data);
            }else if (ArrDetail.get(i).getTipe().equals(TIPE_DET_POTONGAN)){
                ArListPotongan.add(Data);
            }else if (ArrDetail.get(i).getTipe().equals(TIPE_DET_KASBON)) {
//                Data.setCheck(true);
//                int idx = CekKasbonIdxKasbon(Data.getId_tjg_pot_kas());
//                if (idx > -1){
//                    ArListKasbon.get(idx).setSisa(ArListKasbon.get(idx).getSisa() + Data.getJumlah());
//                    ArListKasbon.get(idx).setCheck(true);
//                    ArListKasbon.get(idx).setJumlah(Data.getJumlah());
//                }
            }
        }

//        int i = 0;
//        while ( i <= ArListKasbon.size()-1 ) {
//            if (ArListKasbon.get(i).getJumlah() == 0) {
//                ArListKasbon.remove(i);
//                i--;
//            }
//            i++;
//        }
    }


    protected int CekKasbonIdxKasbon (int id) {
        for (int i = 0; i < ArListKasbon.size(); i++) {
            if (ArListKasbon.get(i).getId_tjg_pot_kas() == id) {
                return i;
            }
        }
        return -1;
    }
}
