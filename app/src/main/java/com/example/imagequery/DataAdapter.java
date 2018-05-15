package com.example.imagequery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.Serializable;
import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements Serializable {
    private ArrayList<ReqImage> image;
    static Context context;

    private boolean isLoadingAdded = false;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView tv_name;

        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            tv_name = (ImageView) view.findViewById(R.id.image);

            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //String s = image.get(getLayoutPosition()).getUrl_o();
                    String s = image.get(getLayoutPosition()).getImageUrl();
                    Intent mainIntent = new Intent(context, fullImage.class);
                    mainIntent.putExtra("BUNDLE", s);
                    context.startActivity(mainIntent);
                }
            });
        }
    }

    public DataAdapter(){
        image = new ArrayList<>();
    }

    public DataAdapter(ArrayList<ReqImage> i){
        this.image = i;
    }

    public void setMovies(ArrayList<ReqImage> image) {
        this.image = image;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_list, viewGroup, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        Glide.with(context)
                //.load(image.get(i).getUrl_o())
                .load(image.get(i).getImageUrl())
                .apply(new RequestOptions().override(400,400))
                .into(viewHolder.tv_name);
    }

    @Override
    public int getItemCount() {
        return image.size();
    }

    public void add(ReqImage r) {
        image.add(r);
        notifyItemInserted(image.size() - 1);
    }

    public void addAll(ArrayList<ReqImage> moveResults) {
        for (ReqImage result : moveResults) {
            add(result);
        }
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = image.size() - 1;
        ReqImage result = getItem(position);
        if (result != null) {
            image.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ReqImage getItem(int position) {
        return image.get(position);
    }
}