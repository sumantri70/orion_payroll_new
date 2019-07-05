package com.orionit.app.orion_payroll_new.email;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.orionit.app.orion_payroll_new.database.master.EmailTable;
import com.orionit.app.orion_payroll_new.models.EmailModel;
import com.orionit.app.orion_payroll_new.models.KirimEmailModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataSource;

import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.inform;

public class SendMail extends AsyncTask<ArrayList<KirimEmailModel>, String, Long> {

    //Declaring Variables
    private Context context;
    private Session session;
    private KirimEmailModel DataKirim;
    private int berjalan, jumlah;
    private ProgressDialog progressDialog;

    //Class Constructor
    public SendMail(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,"Mengirim email","Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
        progressDialog.dismiss();
        inform(context, "Email terkirim");
    }


    @Override
    protected Long doInBackground(ArrayList<KirimEmailModel>... params) {
        //Creating properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {
            //Authenticating the password
            protected PasswordAuthentication getPasswordAuthentication() {
                EmailTable TEmail = new EmailTable(context);
                EmailModel Data = TEmail.GetData(1);
                return new PasswordAuthentication(Data.getAlamat_email(), Data.getPassword());
            }
        });

        berjalan = 0;
        jumlah   = params[0].size();
        for (int i =0; i < params[0].size(); i++){
            if (isCancelled()) break;

            try {
                //Creating MimeMessage object
                MimeMessage mm = new MimeMessage(session);
                mm.setFrom(new InternetAddress(Config.EMAIL));

                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(params[0].get(i).getEmail()));
                mm.setSubject(params[0].get(i).getSubject());
                mm.setText(params[0].get(i).getMessage());

                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();
                // Create a multipar message
                Multipart multipart = new MimeMultipart();

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                java.io.File file = new File(params[0].get(i).getFile());
                if(file.exists()) {
                    DataSource source = new FileDataSource(params[0].get(i).getFile());
                    mm.setDataHandler(new DataHandler(source));
                    mm.setFileName(params[0].get(i).getFile());
                }else{

                }

                berjalan++;
                publishProgress("Mengirim email ke \n"+params[0].get(i).getEmail());

                //Sending email
                Transport.send(mm);
            } catch (MessagingException e) {
                cancel(true);
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onCancelled(Long aLong) {
        super.onCancelled(aLong);
        progressDialog.dismiss();
        inform(context, "Email gagal dikirm\n"+"Pastikan email dan password pengirim terisi dengan benar");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setTitle("Mengirim email "+String.valueOf(berjalan)+" dari "+String.valueOf(jumlah));
        progressDialog.setMessage(values[0]);
    }
}

