package com.example.mybook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mybook.Models.DataModel;
import com.example.mybook.R;
import com.example.mybook.webView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<DataModel> dataModelArrayList;
    Context context;


    public RecyclerAdapter(ArrayList<DataModel> dataModelArrayList, Context context) {
        this.dataModelArrayList = dataModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.newslayout,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        DataModel dataModel = dataModelArrayList.get(position);
        holder.title.setText(dataModel.getTitle());
        holder.description.setText(dataModel.getDescription());
        Glide.with(context).load(dataModel.getUrlToImage()).placeholder(R.drawable.noimage).into(holder.imageView);
        Glide.with(context).load(dataModel.getIconPic()).placeholder(R.drawable.splashpic).into(holder.Icon);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, webView.class);
                intent.putExtra("url",dataModel.getUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title , description;
        ImageView imageView,Icon;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.NewsTitle);
            description = itemView.findViewById(R.id.NewsTextView);
            imageView = itemView.findViewById(R.id.NewsImage);
            cardView = itemView.findViewById(R.id.NewsCardView);
            Icon = itemView.findViewById(R.id.Icon);
        }
    }
}
