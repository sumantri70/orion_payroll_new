package com.orionit.app.orion_payroll_new.utility;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class FungsiGeneral {
    public final static NumberFormat fmt = NumberFormat.getInstance();
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    public static String getTglFormatCustom(long date, String format){
        DateFormat formatter = new SimpleDateFormat(format);
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

    public static String FormatMySqlDate(String tanggal){
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(tanggal);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String FormatMySqlDatePeriode(String tanggal){
        String inputPattern = "MMMM yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(tanggal);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String FormatDateFromSql(String tanggal){
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(tanggal);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String serverNowFormated(){
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calender = Calendar.getInstance();
        return formatter.format(calender.getTimeInMillis());
    }

    public static long serverNowLong(){
        //DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calender = Calendar.getInstance();
        return calender.getTimeInMillis();
    }

    public static long serverNowStartOfTheMonthLong(){
        Calendar calender = Calendar.getInstance();
        calender.set(Calendar.DAY_OF_MONTH, 1);
        calender.set(Calendar.HOUR_OF_DAY, 0);
        calender.set(Calendar.MINUTE, 0);
        calender.set(Calendar.SECOND, 0);
        calender.set(Calendar.MILLISECOND, 0);
        return calender.getTimeInMillis();
    }

    public static long StartOfTheMonthLong(Long date){
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(date);
        calender.set(Calendar.DAY_OF_MONTH, 1);
        calender.set(Calendar.HOUR_OF_DAY, 0);
        calender.set(Calendar.MINUTE, 0);
        calender.set(Calendar.SECOND, 0);
        calender.set(Calendar.MILLISECOND, 0);
        return calender.getTimeInMillis();
    }

    public static long EndOfTheMonthLong(Long date){
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(date);
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH));
        calender.set(Calendar.HOUR_OF_DAY, calender.getActualMaximum(Calendar.HOUR_OF_DAY));
        calender.set(Calendar.MINUTE, calender.getActualMaximum(Calendar.MINUTE));
        calender.set(Calendar.SECOND, calender.getActualMaximum(Calendar.SECOND));
        calender.set(Calendar.MILLISECOND, calender.getActualMaximum(Calendar.MILLISECOND));
        return calender.getTimeInMillis();
    }

//    public static long serverNowStartOfTheMonthLongNew(){
//        Calendar calender = Calendar.getInstance();
//        calender.set(Calendar.DAY_OF_YEAR, 1);
//        return calender.getTimeInMillis();
//    }



    public static String StartOfTheMonth(long date){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(date);
        calender.set(Calendar.DAY_OF_MONTH, 1);
        return formatter.format(calender.getTime());
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

    public static long getMillisDateFmt(String input, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
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

    public static String FloatToStrFmt(double input){

        double koma, isi;
        isi = Math.floor(input);
        koma = input - isi;
        String hasil1, hasil2;
        NumberFormat format = NumberFormat.getInstance();
        hasil1 = format.format(isi);
        hasil2 = String.format("%.2f", koma);

        return hasil1+hasil2.substring(1);
    }

    public static float StrFmtToFloatInput(String input){

        final NumberFormat formatter = NumberFormat.getInstance();
        String koma = formatter.format(0.1);
        if (koma.contains(",")){
            if (!input.contains(",")) {
                input = input.replace(".", ",");
            }
        }
        try {
            Number n = formatter.parse(input);
            return n.floatValue();
        }catch(ParseException e){
            return 0;
        }
    }

    public static int StrToIntDef(String value, int Def) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException nfe) {
            return Def;
        }
    }

    public static Double StrToDeobleDef(String value, Double Def) {
        try {
            return Double.parseDouble(value);
        } catch(NumberFormatException nfe) {
            return Def;
        }
    }

    public static double RoundTwoDecimal(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public static String FmtSqlStr(String str){
        return "'"+str+"'";
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    public static String DoubleToStr(Double Nilai){
        return fmt.format(Nilai);
    }

    public static double Round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String NumberToRomawi(int  number){
        String Hasil = "";
        switch (number) {
            case 1 :
                Hasil = "I";
                break;
            case 2 :
                Hasil = "II";
                break;
            case 3 :
                Hasil = "III";
                break;
            case 4 :
                Hasil = "VI";
                break;
            case 5 :
                Hasil = "V";
                break;
            case 6 :
                Hasil = "VI";
                break;
            case 7 :
                Hasil = "VII";
                break;
            case 8 :
                Hasil = "VIII";
                break;
            case 9 :
                Hasil = "IX";
                break;
            case 10 :
                Hasil = "X";
                break;
            case 11 :
                Hasil = "XI";
                break;
            case 12 :
                Hasil = "XII";
                break;
            case 13 :
                Hasil = "XIII";
                break;
            case 14 :
                Hasil = "XIV";
                break;
            case 15 :
                Hasil = "XV";
                break;
            case 16 :
                Hasil = "XVI";
                break;
            case 17 :
                Hasil = "XVII";
                break;
            case 18 :
                Hasil = "XVIII";
                break;
            case 19 :
                Hasil = "XIV";
                break;
            case 20 :
                Hasil = "XX";
                break;
            default:
                Hasil = "";
        }
        return Hasil    ;
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n, char karakter) {
        return String.format("%" + n + "s", s).replace(' ', karakter);
    }

}
