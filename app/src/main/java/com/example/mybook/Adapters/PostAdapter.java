package com.example.mybook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mybook.Models.DataModel;
import com.example.mybook.Models.UploadModel;
import com.example.mybook.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    ArrayList<DataModel> uploadModelArrayList;
    Context context;

    public PostAdapter(ArrayList<DataModel> uploadModelArrayList, Context context) {
        this.uploadModelArrayList = uploadModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.postlayout,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        DataModel uploadModel = uploadModelArrayList.get(position);
        holder.title.setText(uploadModel.getTitle());
        holder.description.setText(uploadModel.getDescription());
        Glide.with(context).load(uploadModel.getUrlToImage()).into(holder.postImage);
    }

    @Override
    public int getItemCount() {
        return uploadModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName,title,description;
        ImageView postImage,userImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.UserName);
            title = itemView.findViewById(R.id.postTitle);
            postImage = itemView.findViewById(R.id.postImage);
            description = itemView.findViewById(R.id.postTextView);
            userImage = itemView.findViewById(R.id.UserProfileImg);
        }
    }
}
