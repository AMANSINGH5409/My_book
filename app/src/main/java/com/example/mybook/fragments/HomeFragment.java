package com.example.mybook.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mybook.Adapters.PostAdapter;
import com.example.mybook.Adapters.RecyclerAdapter;
import com.example.mybook.Models.ApiUtilities;
import com.example.mybook.Models.DataModel;
import com.example.mybook.Models.NewsModel;
import com.example.mybook.Models.UploadModel;
import com.example.mybook.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase database;

    String apikey = "968f52a549e64ba896cadf1fd25b8962";
    String topic = "Google-coding";
    String country = "us";
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    RecyclerAdapter recyclerAdapter;
    PostAdapter postAdapter;
    private ArrayList<DataModel> arrayList;
    private ArrayList<DataModel> modelArrayList;
    SpinKitView spinKitView;
    TextView newTopic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home,container,false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        spinKitView = view.findViewById(R.id.spin_kit);
        newTopic = view.findViewById(R.id.NewTopic);

        topic = FirebaseRemoteConfig.getInstance().getString("Topic");

        arrayList = new ArrayList<>();
        modelArrayList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.NewsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new RecyclerAdapter(arrayList,getContext());
//        postAdapter = new PostAdapter(modelArrayList,getContext());
        recyclerView.setAdapter(recyclerAdapter);
        //recyclerView.setAdapter(postAdapter);

        getData();

        database.getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DataModel uploadModel = dataSnapshot.getValue(DataModel.class);
                    arrayList.add(uploadModel);
                }
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        view.findViewById(R.id.fabsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.fabsearch).setVisibility(View.GONE);
                view.findViewById(R.id.fabSearchCard).setVisibility(View.VISIBLE);
            }
        });

        view.findViewById(R.id.BtnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.fabsearch).setVisibility(View.VISIBLE);
                view.findViewById(R.id.fabSearchCard).setVisibility(View.GONE);
                return;
            }
        });

        view.findViewById(R.id.BtnNewTopic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newTopic.getText().toString().isEmpty()){
                    newTopic.setError("Empty Search is not allowed!");
                    return;
                }
                spinKitView.setVisibility(View.VISIBLE);
               String NewTopic = newTopic.getText().toString();
                ApiUtilities.getApiInterface().getNewsDate(NewTopic,apikey).enqueue(new Callback<NewsModel>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                        if(response.isSuccessful()){
                            arrayList.clear();
                            // progressDialog.dismiss();
                            spinKitView.setVisibility(View.GONE);
                            view.findViewById(R.id.fabSearchCard).setVisibility(View.GONE);
                            view.findViewById(R.id.fabsearch).setVisibility(View.VISIBLE);
                            Log.d("APITESTING", "Nasim "+response.body().getArticles().toString());
//                    arrayList.addAll(response.body().getArticles());
                            arrayList.addAll(response.body().getArticles());
                            recyclerAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsModel> call, Throwable t) {

                    }
                });
            }
        });

        return view;
    }

    private void getData() {
        ApiUtilities.getApiInterface().getNewsDate(topic,apikey).enqueue(new Callback<NewsModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                if(response.isSuccessful()){
                   // progressDialog.dismiss();
                    spinKitView.setVisibility(View.GONE);
                    Log.d("APITESTING", "Nasim "+response.body().getArticles().toString());
//                    arrayList.addAll(response.body().getArticles());
                    arrayList.addAll(response.body().getArticles());
                    recyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

            }
        });

    }

}