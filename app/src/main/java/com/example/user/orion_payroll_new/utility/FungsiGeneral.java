package com.example.user.orion_payroll_new.utility;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FungsiGeneral {

    public static String getTahun(long date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String thn = format.format(date);
        return thn;
    }

    public static float StrFmtToFloat(String input){

        final NumberFormat formatter = NumberFormat.getInstance();

        try {
            Number n = formatter.parse(input);
            return n.floatValue();
        }catch(ParseException e){
            return 0;
        }
    }


    public static double StrFmtToDouble(String input){

        final NumberFormat formatter = NumberFormat.getInstance();

        try {
            Number n = formatter.parse(input);
            return n.floatValue();
        }catch(ParseException e){
            return 0;
        }
    }

    public static final String tag_json_obj = "json_obj_req";


    public static String getBulan(long date){
        SimpleDateFormat format = new SimpleDateFormat("MM");
        String bln = format.format(date);
        return bln;
    }

    public static String getHari(long date){
        SimpleDateFormat format = new SimpleDateFormat("dd");
        String hr = format.format(date);
        return hr;
    }

    public static String getTglFormat(long date){
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(date);
        return formatter.format(calender.getTime());
    }

    public static String getTglFormatMySql(long date){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(date);
        return formatter.format(calender.getTime());
    }

    public static String serverNowFormated(){
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calender = Calendar.getInstance();
        return formatter.format(calender.getTimeInMillis());
    }

    public static long serverNowLong(){
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calender = Calendar.getInstance();
        return calender.getTimeInMillis();
    }

    public static String serverNowFormated4Ekspor(){
        DateFormat formatter = new SimpleDateFormat("ddMMyyyy_HHmm");
        Calendar calender = Calendar.getInstance();
        return formatter.format(calender.getTimeInMillis());
    }
    public static String serverNow(){
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Calendar calender = Calendar.getInstance();
        return formatter.format(calender.getTime());
    }
    public static String serverNow4Nomor(){
        DateFormat formatter = new SimpleDateFormat("MMyy");

        Calendar calender = Calendar.getInstance();
        return formatter.format(calender.getTime());
    }

    public static long getMillisDate(String input) {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = f.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long date = 0;
        if (d != null) {
            date = d.getTime();
            return date;
        }
        return 0;
    }


    public static long getMillisDateTime(String input) {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date d = null;
        try {
            d = f.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long date = 0;
        if (d != null) {
            date = d.getTime();
            return date;
        }
        return 0;
    }


    public static void inform(Context context, String pesan){
        new AlertDialog.Builder(context)
                .setMessage(pesan)
                .setCancelable(true)
                .setPositiveButton("Ok",null)
                .show();
    }


    public static long getSimpleDate(String input) {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = f.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long date = 0;
        if (d != null) {
            date = d.getTime();
        }
        return date;
    }

    public static String FmtSqlStr(String str){
        return "'"+str+"'";
    }
}
