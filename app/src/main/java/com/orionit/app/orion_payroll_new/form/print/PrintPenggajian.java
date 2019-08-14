package com.orionit.app.orion_payroll_new.form.print;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.orionit.app.orion_payroll_new.R;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

        public String Execute(final int IdMst){
            Loading.setMessage("Loading...");
            Loading.setCancelable(false);
            Loading.show();
            LoadData(IdMst);
            String Hasil = "";

            try {
                Hasil = CreatePrdfDenganHEader();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            Loading.dismiss();
            return Hasil;
        }

    private String CreatePrdfDenganHEader() throws FileNotFoundException, DocumentException {
        String Hasil = "";
        Font fontKecil = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);
        Font fontNormal = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
        Font fontBold   = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
        Font fontBoldHeader = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);

        this.NamaPrint = Get_Nama_Master_Pegawai(DtPenggajian.getId_pegawai())+"-"+getTglFormatCustom(DtPenggajian.getPeriode(),"MMMM-yyyy");

        pdfFile = new File(CreateGetDir(DtPenggajian.getPeriode()),NamaPrint+".pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        PdfWriter.getInstance(document, output);
        document.setPageSize(PageSize.A6);
        document.setMargins(17,17,10,10);
        document.open();

        Chunk glue = new Chunk(new VerticalPositionMark());

        LineSeparator line = new LineSeparator(1,100,null, Element.ALIGN_CENTER,-4);

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_orion);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image img = null;
        byte[] byteArray = stream.toByteArray();
        try {
            img = Image.getInstance(byteArray);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1,4,1});

        PdfPCell c1 = new PdfPCell(img);
        Phrase pr = new Phrase("ORION IT SOLUTION\n", fontBoldHeader);
        pr.add( new Phrase("JL. TMN Mekar Abadi 1/76, Komp Mekar Wangi, Bandung\n", fontKecil));
        pr.add( new Phrase("Telp. 022-88886450 Email : Orionbdg@gmail.com", fontKecil));
        PdfPCell c2 = new PdfPCell(pr);

        PdfPCell c3 = new PdfPCell(new Phrase("SLIP GAJI", fontBold));
        c3.setVerticalAlignment(Element.ALIGN_RIGHT);
        c3.setHorizontalAlignment(Element.ALIGN_RIGHT);

        c1.setBorder(Rectangle.NO_BORDER);
        c2.setBorder(Rectangle.NO_BORDER);
        c3.setBorder(Rectangle.NO_BORDER);

        table.addCell(c1);
        table.addCell(c2);
        table.addCell(c3);
        document.add(table);
        document.add(line);

        Paragraph pKosong = new Paragraph(" ");
        document.add(pKosong);

        PdfPTable TableMaster = new PdfPTable(3);
        TableMaster.setWidthPercentage(100);
        TableMaster.setWidths(new float[]{3, 1, 16});

        PdfPCell cm11 = new PdfPCell(new Phrase("Nomor", fontNormal));
        PdfPCell cm12 = new PdfPCell(new Phrase("Tanggal", fontNormal));
        PdfPCell cm13 = new PdfPCell(new Phrase("Periode", fontNormal));
        PdfPCell cm14 = new PdfPCell(new Phrase("Nik/Nama", fontNormal));

        cm11.setHorizontalAlignment(Element.ALIGN_LEFT);
        cm12.setHorizontalAlignment(Element.ALIGN_LEFT);
        cm13.setHorizontalAlignment(Element.ALIGN_LEFT);
        cm14.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell cm21 = new PdfPCell(new Phrase(":", fontNormal));
        PdfPCell cm22 = new PdfPCell(new Phrase(":", fontNormal));
        PdfPCell cm23 = new PdfPCell(new Phrase(":", fontNormal));
        PdfPCell cm24 = new PdfPCell(new Phrase(":", fontNormal));

        cm21.setHorizontalAlignment(Element.ALIGN_CENTER);
        cm22.setHorizontalAlignment(Element.ALIGN_CENTER);
        cm23.setHorizontalAlignment(Element.ALIGN_CENTER);
        cm24.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cm31 = new PdfPCell(new Phrase(DtPenggajian.getNomor(), fontNormal));
        PdfPCell cm32 = new PdfPCell(new Phrase(getTglFormat(DtPenggajian.getTanggal()), fontNormal));
        PdfPCell cm33 = new PdfPCell(new Phrase(getTglFormatCustom(DtPenggajian.getPeriode(), "MMMM yyyy"), fontNormal));
        PdfPCell cm34 = new PdfPCell(new Phrase(Get_Nik_Master_Pegawai(DtPenggajian.getId_pegawai())+"|"+Get_Nama_Master_Pegawai(DtPenggajian.getId_pegawai()), fontNormal));

        cm11.setBorder(Rectangle.NO_BORDER); cm21.setBorder(Rectangle.NO_BORDER); cm31.setBorder(Rectangle.NO_BORDER);
        cm12.setBorder(Rectangle.NO_BORDER); cm22.setBorder(Rectangle.NO_BORDER); cm32.setBorder(Rectangle.NO_BORDER);
        cm13.setBorder(Rectangle.NO_BORDER); cm23.setBorder(Rectangle.NO_BORDER); cm33.setBorder(Rectangle.NO_BORDER);
        cm14.setBorder(Rectangle.NO_BORDER); cm24.setBorder(Rectangle.NO_BORDER); cm34.setBorder(Rectangle.NO_BORDER);

        TableMaster.addCell(cm11);
        TableMaster.addCell(cm21);
        TableMaster.addCell(cm31);

        TableMaster.addCell(cm12);
        TableMaster.addCell(cm22);
        TableMaster.addCell(cm32);

        TableMaster.addCell(cm13);
        TableMaster.addCell(cm23);
        TableMaster.addCell(cm33);

        TableMaster.addCell(cm14);
        TableMaster.addCell(cm24);
        TableMaster.addCell(cm34);

        document.add(TableMaster);

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
        if (SisaKasbon > 0) {
            if (keterangan.equals("")){
                keterangan += "Sisa kasbon : "+fmt.format(SisaKasbon);
            }else{
                keterangan += "\n"+"                      Sisa kasbon : "+fmt.format(SisaKasbon);
            }
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
        document.close();
        return pdfFile.getPath();
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

            }
        }
    }


    protected int CekKasbonIdxKasbon (int id) {
        for (int i = 0; i < ArListKasbon.size(); i++) {
            if (ArListKasbon.get(i).getId_tjg_pot_kas() == id) {
                return i;
            }
        }
        return -1;
    }

    private String CreateGetDir(long periode) {

        String path_periode = getTglFormatCustom(periode, "MMMM yyyy");

        File direct1 = new File(Environment.getExternalStorageDirectory() + "/orion_payroll");
        if (!direct1.exists()) {
            direct1.mkdirs();
        }

        File direct2 = new File(Environment.getExternalStorageDirectory() + "/orion_payroll/print_penggajian");
        if (!direct2.exists()) {
            direct2.mkdirs();
        }

        File direct3 = new File(Environment.getExternalStorageDirectory() + "/orion_payroll/print_penggajian/"+path_periode);
        if (!direct3.exists()) {
            direct3.mkdirs();
        }

        return direct3.getPath();
    }
}
