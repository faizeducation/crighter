package com.crighter.Adapter;

import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crighter.R;
import com.crighter.URLS.APIURLS;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gold on 10/13/2018.
 */

public class SingleImageAdapter extends RecyclerView.Adapter<SingleImageAdapter.MyViewHolder>{

    private ArrayList<String> dataArray;
    private Activity mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    public static final int REQUEST_PERMISSION_CODE = 300;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_img;
        ProgressBar progressbar;

        public MyViewHolder(final View view) {
            super(view);

            iv_img = (ImageView) view.findViewById(R.id.iv_img);
            progressbar = (ProgressBar) view.findViewById(R.id.progressbar);


        }
    }

    public SingleImageAdapter(Activity context , ArrayList<String> appealList) {
        this.mContext = context;
        this.dataArray = appealList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.e("TAg", "the view type : " + viewType);

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_layout_vertical_items, parent, false);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_single_image_layout, parent, false);
        viewHolder = new MyViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {

            String img = dataArray.get(position);
            String URL = APIURLS.IMAGE_BACK_URL+img;
            Log.e("TAg", "the single img url is " + URL);
            Glide.with(mContext)
                    .load(URL)
                    .centerCrop()
                    .placeholder(R.drawable.logo)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable final GlideException e,
                                                    final Object model, final Target<Drawable> target,
                                                    final boolean isFirstResource) {



                            return false;
                        }

                        @Override
                        public boolean onResourceReady(final Drawable resource,
                                                       final Object model,
                                                       final Target<Drawable> target,
                                                       final DataSource dataSource,
                                                       final boolean isFirstResource) {
                            holder.progressbar.setVisibility(View.GONE);


                            return false;
                        }
                    })
                    .into(holder.iv_img);

//            Picasso.with(mContext)
//                    .load(URL)
//                    .placeholder(R.drawable.logo)
//                    .fit()
//                    .into(holder.iv_img, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            holder.progressbar.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onError() {
//
//                        }
//                    });
    }
    }

    @Override
    public int getItemCount() {
        return dataArray.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}