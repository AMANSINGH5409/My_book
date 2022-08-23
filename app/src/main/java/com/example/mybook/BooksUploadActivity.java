package com.example.mybook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybook.Models.BookUploadModel;
import com.example.mybook.Models.Notifications;
import com.example.mybook.Models.Users;
import com.example.mybook.databinding.ActivityBooksUploadBinding;
import com.example.mybook.fragments.BooksFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BooksUploadActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseStorage storage;
    ActivityBooksUploadBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    String Token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBooksUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(BooksUploadActivity.this);
        progressDialog.setTitle("Sharing Book..");
        progressDialog.setMessage("Uploading your book on the database...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        binding.BookUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.BookName.getText().toString().isEmpty()){
                    binding.BookName.setError("Enter Name of Book First !");
                    return;
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf/*");
                startActivityForResult(intent,102);
            }
        });

        database.getReference().child("Notification").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Notifications notifications = snapshot.getValue(Notifications.class);
                Token = notifications.getToken();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData() != null){
            progressDialog.show();
            Uri pdfFile = data.getData();
            StorageReference reference = storage.getReference().child("BOOKS_PDFs").child(pdfFile.getEncodedPath());
            reference.putFile(pdfFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            Date time = new Date();
                            String uniqueBookID = UUID.randomUUID().toString();
                            BookUploadModel Book = new BookUploadModel(auth.getCurrentUser().getUid(),
                                    binding.BookName.getText().toString(),uri.toString(),uniqueBookID,time.toString());
                            database.getReference().child("Books").push().setValue(Book);
                        }
                    });
                    Toast.makeText(BooksUploadActivity.this, "Book Uploaded Successfully !👍", Toast.LENGTH_SHORT).show();



                    database.getReference().child("Books").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                BookUploadModel book = snapshot.getValue(BookUploadModel.class);
                                String BookName = book.getBook_Name();
                                String message = "A new book " + book.getBook_Name() + " is uploaded in the app";
                                String token = Token;
                                sendNotification(BookName, message, token);
                            }catch (Exception e){

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }else {
            progressDialog.dismiss();
            Intent intent = new Intent(BooksUploadActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(BooksUploadActivity.this, "File Not Selected !", Toast.LENGTH_SHORT).show();
        }
    }

    void sendNotification(String UploaderName,String message,String token){
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://fcm.googleapis.com/fcm/send";

            JSONObject data = new JSONObject();
            data.put("title", UploaderName);
            data.put("body", message);
            JSONObject notificationData = new JSONObject();
            notificationData.put("Notification",data);

            JsonObjectRequest request = new JsonObjectRequest(url, notificationData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(BooksUploadActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String,String> map = new HashMap<>();
                    String key = "Key=AAAACSwlohE:APA91bEZHVKxQ2IXM8_JoabHIOiNPIUR4CqoZsEGsOrrj4h4mv5ib9REWHGhIUJJkg49pv6J2L40T4ARvtf2JDSadqliT120yh3L54kj7xAVSggEr7_4iQry4-6GbapMmsSsXkoP8HGA";
                    map.put("Authorization",key);
                    map.put("content-type","application/json");
                    return map;
                }
            };

            queue.add(request);
        }catch (Exception e){

        }

    }
}