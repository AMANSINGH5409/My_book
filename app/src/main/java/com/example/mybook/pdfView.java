package com.example.mybook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class pdfView extends AppCompatActivity {
    PDFView pdfView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("LOADING PDF......");
        progressDialog.setCancelable(false);
        progressDialog.show();

        pdfView = findViewById(R.id.pdfView);
        String Name = getIntent().getStringExtra("pdfName");
        getSupportActionBar().setTitle(Name);
        String Url = getIntent().getStringExtra("PDFurl");
        new OpenPDF().execute(Url);
    }

    class OpenPDF extends AsyncTask<String,Void, InputStream>{

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL Url = new URL(strings[0]);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) Url.openConnection();
                if(httpsURLConnection.getResponseCode()==200){
                    inputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                return  null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
            progressDialog.dismiss();
        }
    }
}