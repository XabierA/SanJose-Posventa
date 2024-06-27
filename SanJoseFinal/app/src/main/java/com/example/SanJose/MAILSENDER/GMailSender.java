package com.example.SanJose.MAILSENDER;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.SanJose.Models.Email;
import com.example.SanJose.Revision_Ins_Fragment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GMailSender extends AsyncTask<Void, Void, Void> {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private String especialidad;

    private Properties props;
    private Session session;
    private Session session2;
    public List<Email> emails = new ArrayList<>();

    static {
        Security.addProvider(new JSSEProvider());
    }

    public GMailSender(String user, String password, List<Email> emails) {
        this.user = user;
        this.password = password;
        this.emails = emails;

        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");


        session2 = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);  ///"cbvk gkyh dzir ycof"
                    }
                });
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String result = emails.stream().map(Email::getEmail).collect(Collectors.joining(","));

        Log.v("test", result);
        try {
            String stringSenderEmail = "testemailsenderapp@gmail.com";
            String stringReceiverEmail = "xabier.angulo@bexreal.com, xabieranguloaramburu@gmail.com";
            String stringPasswordSenderEmail = "jmdpwgebudgzfiom";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.socketFactory.port", "465");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "465");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            //mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(stringReceiverEmail));

            mimeMessage.setSubject("Subject: test");
            mimeMessage.setText("Se a generado una nueva incidencia, \n\n puede acceder al portal web para revisarla.");

            Transport.send(mimeMessage);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}