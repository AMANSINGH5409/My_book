package com.example.mybook.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mybook.Adapters.BooksAdapter;
import com.example.mybook.BooksUploadActivity;
import com.example.mybook.MainActivity;
import com.example.mybook.Models.BookUploadModel;
import com.example.mybook.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BooksFragment extends Fragment {

    FirebaseStorage storage;
    FirebaseDatabase database;
    RecyclerView recyclerView;
    BooksAdapter booksAdapter;
    ArrayList<BookUploadModel> bookUploadModelArrayList;

    SpinKitView spinKitView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_books, container, false);

        spinKitView = view.findViewById(R.id.spin_kitBook);

        bookUploadModelArrayList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.bookRecyclerView);
        booksAdapter = new BooksAdapter(bookUploadModelArrayList,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(booksAdapter);

        Comparator<BookUploadModel> comparator = new Comparator<BookUploadModel>() {
            @Override
            public int compare(BookUploadModel lhs, BookUploadModel rhs) {
                String  left = lhs.getTime();
                String right = rhs.getTime();
                return right.compareTo(left);
            }
        };

        database.getReference().child("Books").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookUploadModelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    BookUploadModel bookUploadModel = dataSnapshot.getValue(BookUploadModel.class);
                    bookUploadModel.setBookDBKey(dataSnapshot.getKey());
                    bookUploadModelArrayList.add(bookUploadModel);
                    Collections.reverse(bookUploadModelArrayList);
                }
                spinKitView.setVisibility(View.GONE);
                booksAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        view.findViewById(R.id.fabAddBooks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BooksUploadActivity.class);
                startActivity(intent);
                bookUploadModelArrayList.clear();
            }
        });

        return view;
    }
}