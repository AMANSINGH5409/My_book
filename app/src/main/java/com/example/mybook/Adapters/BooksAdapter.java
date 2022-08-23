package com.example.mybook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mybook.Models.BookUploadModel;
import com.example.mybook.Models.Likes;
import com.example.mybook.Models.Users;
import com.example.mybook.R;
import com.example.mybook.fragments.BooksFragment;
import com.example.mybook.pdfView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    ArrayList<BookUploadModel> arrayList;
    Context context;

    public BooksAdapter(ArrayList<BookUploadModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.books_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksAdapter.ViewHolder holder, int position) {
        BookUploadModel bookUploadModel = arrayList.get(position);
        holder.BookName.setText(bookUploadModel.getBook_Name());
        String pdfURL = bookUploadModel.getPdfDownloadlink();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, pdfView.class);
                intent.putExtra("pdfName",bookUploadModel.getBook_Name());
                intent.putExtra("PDFurl",pdfURL);
                context.startActivity(intent);
            }
        });
        holder.UploadTime.setText(bookUploadModel.getTime());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        database.getReference().child("Users").child(bookUploadModel.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if(auth.getCurrentUser().getUid().equals(bookUploadModel.getUserID())){
                    holder.SharedBy.setText("This PDF is shared by you");
                }else {
                    holder.SharedBy.setText("This PDF is shared by "+users.getUserName());
                }
                Glide.with(context).load(users.getProfilePic()).placeholder(R.drawable.user).into(holder.imageView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.likeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Books").child("likes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            if(dataSnapshot.getKey().equals(auth.getCurrentUser().getUid())){
                                database.getReference().child("Books").child(bookUploadModel.getBookDBKey()).child("like").child(auth.getCurrentUser().getUid())
                                        .setValue("");
                                holder.likeImg.setImageResource(R.drawable.ic_unliked);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                database.getReference().child("Books").child(bookUploadModel.getBookDBKey()).child("like").child(auth.getCurrentUser().getUid())
                        .setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("Users").child(bookUploadModel.getUserID()).child("likesOnBooks")
                                .push().setValue(auth.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                holder.likeImg.setImageResource(R.drawable.ic_liked);
                            }
                        });
                    }
                });
            }
        });

//        holder.likeImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                holder.likeImg.setImageResource(R.drawable.ic_liked);
//                Likes likes = new Likes(auth.getCurrentUser().getUid());
//                database.getReference().child("likes").child(bookUploadModel.getBookId()).push().setValue(likes)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                HashMap<String,String> likes = new HashMap<>();
//                                likes.put(bookUploadModel.getBook_Name(),auth.getCurrentUser().getUid());
//                                database.getReference().child("Users").child(bookUploadModel.getUserID()).child("likes").push().setValue(likes);
//                                Toast.makeText(context, "You liked "+bookUploadModel.getBook_Name(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//        });
//
//        database.getReference().child("likes").child(bookUploadModel.getBookId()).addValueEventListener(new ValueEventListener() {
//            int NoLikes = 0;
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    NoLikes++;
//                }
//                holder.NoOflikes.setText(NoLikes+"");
//                NoLikes = 0;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView BookName,SharedBy,UploadTime,NoOflikes;
        CardView cardView;
        ImageView imageView,likeImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            BookName = itemView.findViewById(R.id.bookName);
            cardView = itemView.findViewById(R.id.bookItem);
            SharedBy = itemView.findViewById(R.id.SharedBy);
            imageView = itemView.findViewById(R.id.SharedByImage);
            UploadTime = itemView.findViewById(R.id.UploadTime);
            likeImg = itemView.findViewById(R.id.likeImg);
            NoOflikes = itemView.findViewById(R.id.likes);
        }
    }
}
