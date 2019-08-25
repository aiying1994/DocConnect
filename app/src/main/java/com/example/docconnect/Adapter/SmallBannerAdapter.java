package com.example.docconnect.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.docconnect.Model.SmallBanner;
import com.example.docconnect.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SmallBannerAdapter extends RecyclerView.Adapter<SmallBannerAdapter.MyViewHolder> {
    Context context;
    List<SmallBanner> smallBannerList;  // img url string

    public SmallBannerAdapter(Context context, List<SmallBanner> smallBannerList) {
        this.context = context;
        this.smallBannerList = smallBannerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_small_banner, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
//        Glide.with(context)
//                .asBitmap()
//                .load(smallBannerList.get(i))
//                .into(myViewHolder.imageView);
        myViewHolder.tv_title.setText(smallBannerList.get(i).getName());
        myViewHolder.tv_description.setText(smallBannerList.get(i).getDescription());
        Picasso.get().load(smallBannerList.get(i).getImage()).into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return smallBannerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tv_title, tv_description;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.image_small_banner);
            tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            tv_description = (TextView)itemView.findViewById(R.id.tv_description);
        }
    }
}
