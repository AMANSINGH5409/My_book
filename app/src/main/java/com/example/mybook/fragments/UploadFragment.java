package com.example.mybook.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mybook.Models.DataModel;
import com.example.mybook.Models.UploadModel;
import com.example.mybook.Models.Users;
import com.example.mybook.R;
import com.example.mybook.databinding.FragmentUploadBinding;
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;

public class UploadFragment extends Fragment {

    FragmentUploadBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    String urlToImage;
    String UserName;
    String IconImage;
    final int CAMERA_REQUEST_CODE = 102;

    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUploadBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading.....");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        binding.userGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);

            }
        });

        binding.userCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                startActivityForResult(openCamera, 100);
            }
        });

        database.getReference().child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                UserName = users.getUserName();
                IconImage = users.getProfilePic();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String userId = auth.getCurrentUser().getUid();
                Date time = new Date();
                DataModel post = new DataModel(UserName, binding.postContent.getText().toString(),IconImage,urlToImage, time.toString(),"null");
                database.getReference().child("Posts").push().setValue(post);
                binding.postTitle.setText("");
                binding.postContent.setText("");
                binding.uploadImage.setImageResource(R.drawable.uploadphoto);
                progressDialog.dismiss();
                FragmentTransaction Transaction = getParentFragmentManager().beginTransaction();
                Transaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_from_right);
                Transaction.replace(R.id.container,new HomeFragment());
                Transaction.commit();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 33){
            if(data.getData() != null){
                Uri photo = data.getData();
                binding.uploadImage.setImageURI(photo);

                final StorageReference reference = storage.getReference().child("UploadPhotos").child(auth.getCurrentUser().getUid());
                reference.putFile(photo).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlToImage = uri.toString();
//                                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
            }else if(requestCode == 100){
                if(data.getData() != null){
                    Uri CamPhoto = data.getData();
                    Bundle photo = data.getExtras();
                    Bitmap imageBitmap = (Bitmap)photo.get("data");
                    binding.uploadImage.setImageBitmap(imageBitmap);
                    final StorageReference reference = storage.getReference().child("UploadPhotos").child(auth.getCurrentUser().getUid());
                    reference.putFile(CamPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.show();
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlToImage = uri.toString();
//                                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });

                }
            }
        }
    }
}


