package com.example.mybook.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mybook.Models.BookUploadModel;
import com.example.mybook.Models.Likes;
import com.example.mybook.Models.Users;
import com.example.mybook.R;
import com.example.mybook.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FragmentProfileBinding binding;
    FirebaseStorage storage;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        //    View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        database.getReference().child("Users").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                assert users != null;
                binding.UserName.setText(users.getUserName());
                binding.UserEmailId.setText(users.getEmailId());
                Glide.with(getContext()).load(users.getProfilePic()).placeholder(R.drawable.avatar).into(binding.UserProfileImg);
                binding.UserDiscription.setText(users.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.UserProfileImg.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });

        binding.EditProfile.setOnClickListener(v -> {
            String desc = binding.UserDiscription.getText().toString();
            database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("Description").setValue(desc);
            Toast.makeText(getContext(), "Status Updated Successfully !✔", Toast.LENGTH_SHORT).show();
        });


        database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("likes").addValueEventListener(new ValueEventListener() {
            int Likes = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Likes++;
                }
                int NO_OF_LIKES = Likes;
                binding.LikeView.setText(NO_OF_LIKES+" LIKES");
                Likes = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            Uri profilePic = data.getData();
            binding.UserProfileImg.setImageURI(profilePic);

            final StorageReference reference = storage.getReference().child("ProfilePics").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            reference.putFile(profilePic).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(getContext(), "Prolife Pic Uploaded ✔👍", Toast.LENGTH_SHORT).show();
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        database.getReference().child("Users").child(auth.getCurrentUser().getUid())
                                .child("ProfilePic").setValue(uri.toString());
                    }
                });
            });
        }
    }
}