package com.enernet.eg.building;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class CaHttpPost extends CaHttp{

    String attachmentName = "bitmap";
    String attachmentFileName = "";
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    public class TrivialTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // do some checks on the chain here
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public class TrivialHostVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String host, SSLSession session) {
            // check host and return true if verified
            return true;
        }

    }

    public CaHttpPost() {

    }

    public CaHttpPost(final String U) {
        setURI(U);
    }

    public String execute() {
//////////////////////
        if (BuildConfig.DEBUG) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new TrivialTrustManager()}, null);
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new TrivialHostVerifier());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        String str="";

        try {
            JSONObject jo = new JSONObject();
            for (Pair<String, String> A : entities) {
                jo.put(A.first, A.second);
            }

            URL url=new URL(m_strUri);

            HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();

            Log.i("CaHttpPost ", "conn="+conn.toString());

            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os=conn.getOutputStream();

            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            String strOutput=getPostDataString(jo);
            writer.write(strOutput);

            writer.flush();
            writer.close();
            os.close();

            int nResponseCode=conn.getResponseCode();

            Log.i("CaHttpPost ", "reponse_code="+nResponseCode);

            if (nResponseCode== HttpsURLConnection.HTTP_OK) {
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb=new StringBuilder("");
                String line="";

                while ((line=reader.readLine())!=null) {
                    sb.append(line);
                }

                reader.close();
                return sb.toString();
            }
            else {
                Log.i("CaHttpPost ", "reponse_code is not HTTP_OK");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // catch (IOException e) {
        //
        // }


        return str;
    }

    public String execute(Bitmap bitmap) {

        Log.i("HttpPost", "CreateMms run");

        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date dt = new Date();
        String date = df.format(dt);

        this.attachmentFileName = date+".png";

        String str="";

        if (BuildConfig.DEBUG) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new TrivialTrustManager()}, null);
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new TrivialHostVerifier());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        try {
            JSONObject jo = new JSONObject();
            String ask = "?";
            for (Pair<String, String> A : entities) {
                jo.put(A.first, A.second);
                ask = ask + A.first + "=" + A.second +"&";
            }

            ask = ask.substring(0, ask.length() - 1);

            URL url=new URL(m_strUri+ask);

            HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();

            Log.i("CaHttpPost ", "conn="+conn.toString());

            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);

            DataOutputStream request = new DataOutputStream(conn.getOutputStream());

            request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" + this.attachmentName + "\";filename=\"" + this.attachmentFileName+ "\"" + this.crlf);
            request.writeBytes(this.crlf);

            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100 , blob);
            byte[] bytes= blob.toByteArray();
            request.write(bytes);

            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);
            request.flush();
            request.close();

            int nResponseCode=conn.getResponseCode();

            Log.i("CaHttpPost ", "reponse_code="+nResponseCode);

            if (nResponseCode== HttpsURLConnection.HTTP_OK) {
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb=new StringBuilder("");
                String line="";

                while ((line=reader.readLine())!=null) {
                    sb.append(line);
                }

                reader.close();
                return sb.toString();
            }
            else {
                Log.i("CaHttpPost ", "reponse_code is not HTTP_OK");
            }



        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // catch (IOException e) {
        //
        // }


        return str;
    }

    public String execute(ArrayList<Bitmap> bitmapList) {

        Log.i("HttpPost", "CreateReport Multiple Image run");

        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date dt = new Date();
        String date = df.format(dt);

        String str="";

        if (BuildConfig.DEBUG) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new TrivialTrustManager()}, null);
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new TrivialHostVerifier());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        try {
            JSONObject jo = new JSONObject();
            String ask = "?";
            for (Pair<String, String> A : entities) {
                jo.put(A.first, A.second);
                ask = ask + A.first + "=" + A.second +"&";
            }

            ask = ask.substring(0, ask.length() - 1);

            URL url=new URL(m_strUri+ask);

            HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();

            Log.i("CaHttpPost ", "conn="+conn.toString());

            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);

            DataOutputStream request = new DataOutputStream(conn.getOutputStream());

            for(int i=0; i<bitmapList.size(); i++) {

                this.attachmentFileName = date + "_" + i +".png";

                request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" + this.attachmentName + "\";filename=\"" + this.attachmentFileName + "\"" + this.crlf);
                request.writeBytes(this.crlf);

                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                bitmapList.get(i).compress(Bitmap.CompressFormat.PNG, 100, blob);
                byte[] bytes = blob.toByteArray();
                request.write(bytes);

                request.writeBytes(this.crlf);
            }
            request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);
            request.flush();
            request.close();

            int nResponseCode=conn.getResponseCode();

            Log.i("CaHttpPost ", "reponse_code="+nResponseCode);

            if (nResponseCode== HttpsURLConnection.HTTP_OK) {
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb=new StringBuilder("");
                String line="";

                while ((line=reader.readLine())!=null) {
                    sb.append(line);
                }

                reader.close();
                return sb.toString();
            }
            else {
                Log.i("CaHttpPost ", "reponse_code is not HTTP_OK");
            }



        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // catch (IOException e) {
        //
        // }


        return str;
    }
}
