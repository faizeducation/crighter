package com.crighter.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crighter.Activities.SingleImageActivity;
import com.crighter.Activities.SingleMap;
import com.crighter.R;
import com.crighter.URLS.APIURLS;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gold on 10/12/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private ArrayList<HashMap<String, String>> dataArray;
    private Activity mContext;
    ImageView imageImage;

    MediaPlayer mediaPlayer;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    public static final int REQUEST_PERMISSION_CODE = 300;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_main_image;
        RelativeLayout rl_images, rl_voice, rl_map;
        TextView tv_date_time;
        ImageView iv_audio;
        ProgressBar progressbar;

        ProgressDialog pd;

        public MyViewHolder(final View view) {
            super(view);

            iv_main_image = (ImageView) view.findViewById(R.id.iv_main_image);
            rl_images = (RelativeLayout) view.findViewById(R.id.rl_images);
            rl_voice = (RelativeLayout) view.findViewById(R.id.rl_voice);
            rl_map = (RelativeLayout) view.findViewById(R.id.rl_map);
            tv_date_time = (TextView) view.findViewById(R.id.tv_date_time);
            iv_audio = (ImageView) view.findViewById(R.id.iv_audio);
            progressbar = (ProgressBar) view.findViewById(R.id.progressbar);

        }
    }

    public HistoryAdapter(Activity context, ArrayList<HashMap<String, String>> appealList) {
        this.mContext = context;
        this.dataArray = appealList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.e("TAg", "the view type : " + viewType);

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_layout_vertical_items, parent, false);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_hisotyr_layout, parent, false);
        viewHolder = new MyViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {

            MediaPlayer mPlayer = new MediaPlayer();

            String danger_id = dataArray.get(position).get("danger_id");
            String img1 = dataArray.get(position).get("img1");
            String img2 = dataArray.get(position).get("img2");
            String img3 = dataArray.get(position).get("img3");
            String img4 = dataArray.get(position).get("img4");
            String img5 = dataArray.get(position).get("img5");
            String audio = dataArray.get(position).get("audio");
            String post_lat = dataArray.get(position).get("post_lat");
            String post_lng = dataArray.get(position).get("post_lng");
            String post_created = dataArray.get(position).get("post_created");
            String user_id = dataArray.get(position).get("user_id");

            holder.tv_date_time.setText(post_created);

            String url = APIURLS.IMAGE_BACK_URL + img1;
            Log.e("TAG", "the image url are " + url);

            Glide.with(mContext)
                    .load(url)
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
                    .into(holder.iv_main_image);
//            Picasso.with(mContext)
//                    .load(url)
//                    .placeholder(R.drawable.logo)
//                    .fit()
//                    .into(holder.iv_main_image, new Callback() {
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

            final String audioURL = APIURLS.IMAGE_BACK_URL + audio;
            holder.rl_voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TAG", "the audioURL url are " + audioURL);

                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    imageImage = holder.iv_audio;

                    new Player().execute(audioURL);

                    //playAudio(audioURL);

                }
            });

            holder.rl_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (post_lat != null && post_lng != null) {
                        Intent i = new Intent(mContext, SingleMap.class);
                        i.putExtra("lat", post_lat);
                        i.putExtra("lng", post_lng);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(i);
                    }
                }
            });

            holder.rl_images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, SingleImageActivity.class);
                    i.putExtra("img1", img1);
                    i.putExtra("img2", img2);
                    i.putExtra("img3", img3);
                    i.putExtra("img4", img4);
                    i.putExtra("img5", img5);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(i);


                }
            });
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


    private void playAudio(String url) {
        try {
            Uri uri = Uri.parse(url);
            MediaPlayer player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(mContext, uri);
            player.prepareAsync();
            player.prepare();

            ProgressDialog progressDialog = ProgressDialog.show(mContext,
                    "Loading Title", "Loading Message");
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    mp.start();
                }
            });

            //player.start();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {

                mediaPlayer.setDataSource(params[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        imageImage.setImageResource(R.drawable.play_128);
                    }
                });
                mediaPlayer.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progress.isShowing()) {
                progress.cancel();
            }
            Log.d("Prepared", "//" + result);
            mediaPlayer.start();
            imageImage.setImageResource(R.drawable.pause_128);

        }

        public Player() {
            progress = new ProgressDialog(mContext);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            this.progress.setMessage("Please wait...");
            this.progress.show();

        }
    }


}
