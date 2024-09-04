package com.crighter.Services;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.RequiresApi;
import android.util.Log;

import com.crighter.Preferences.SPref;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class RecordingAudio extends Service {
    private int timer = 5;
    Context mContext;
    private MediaRecorder mediaRecorder = null;
    String voiceStoragePath;
    static final String AB = "abcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();
    private Handler mHandler = new Handler();
    private String fileName = null;
    private int lastProgress = 0;

    public RecordingAudio() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAg", "Audio Recorder Called");
       /* if(!externalMemoryAvailable()){

        }*/
        File file = getOutputFile();


        File root = android.os.Environment.getExternalStorageDirectory();
        File file2 = new File(root.getAbsolutePath() + "/Crighter/");
        if (!file2.exists()) {
            file2.mkdirs();
        }
       /* fileName =  root.getAbsolutePath() + "/Crighter/" +
                String.valueOf(System.currentTimeMillis() + ".mp3");*/
        fileName = getStorage(getApplicationContext());
        Log.e("TAg", "Audio Recorder Called file path " + fileName);
        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PRE_AUDIO, 0);
        SPref.storingAudioFilePath(sharedPreferences, fileName);
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP){
            loloPopstartRecording(fileName);
        }else {
            startRecording(fileName);
        }
        //final SoundRecorder soundRecorder = new SoundRecorder(getApplicationContext(), fileName.toString());
        //soundRecorder.startRecording();
        //newStartRecording(file.toString());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "Stop Recording Call");
                //soundRecorder.startRecording();
                stopRecording();
                //newStopRecording();
                stopSelf();
            }
        }, 5000);

        //useHandler();
        return START_NOT_STICKY;
    }

    //Thread for starting mainActivity
    private Runnable mRunnableStartMainActivity = new Runnable() {
        @Override
        public void run() {
            Log.d("Handler", " Calling hadler timer");
            timer--;
            mHandler = new Handler();
            mHandler.postDelayed(this, 1000);

      /*      if(mediaRecorder == null){
                initializeMediaRecord();
            }
            startAudioRecording();*/

            if (timer == 0) {

                stopRecording();
                //newStopRecording();
                stopSelf();
                //saveAudioRecording();
                mHandler.removeCallbacks(mRunnableStartMainActivity);
            }
        }
    };


    //handler for the starign activity
    Handler newHandler;
    public void useHandler(){
        newHandler = new Handler();
        newHandler.postDelayed(mRunnableStartMainActivity, 1000);
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private File getOutputFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);

        //
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                + "/CRighterVoice/RECORDING_"
                + dateFormat.format(new Date())
                + ".m4a");
    }

    private void startRecording(String file) {

        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           // mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        }else {
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        }

        //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        //mediaRecorder.setOutputFile(file.getAbsolutePath());
        // mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            mediaRecorder.setAudioEncodingBitRate(48000);
        } else {
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setAudioEncodingBitRate(64000);
        }
        mediaRecorder.setAudioSamplingRate(16000);
        mediaRecorder.setOutputFile(file);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            Log.e("giftlist", "io problems while preparing [" +
                    file + "]: " + e.getMessage());
        }
    }

    private void newStartRecording(String fileName)
    {
        //we use the MediaRecorder class to record
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
        }
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        Log.e("filename",fileName);
        mediaRecorder.setOutputFile(fileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;

    }

    private void newStopRecording() {

        try{
            mediaRecorder.stop();
            mediaRecorder.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mediaRecorder = null;
        Log.e("TAG", "Recording Saved Successfully");
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public boolean externalMemoryAvailable() {
        if (Environment.isExternalStorageRemovable()) {
            //device support sd card. We need to check sd card availability.
            String state = Environment.getExternalStorageState();
            return state.equals(Environment.MEDIA_MOUNTED) || state.equals(
                    Environment.MEDIA_MOUNTED_READ_ONLY);
        } else {
            //device not support sd card.
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeMediaRecord(String file){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(file);
    }
    private void loliPopstartAudioRecording(){
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("xxx", "IOException at startAudioRecording");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loloPopstartRecording(String file) {
        if(mediaRecorder == null){
            initializeMediaRecord(file);
        }
        loliPopstartAudioRecording();

    }

    private String getStorage(Context context) {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            fileName = context.getExternalCacheDir().getAbsolutePath() + "/CrighterAudoio";
        } else {
            fileName = context.getFilesDir() + "/CrighterAudoio";
        }
        File dir = new File(fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        fileName=fileName+"/"+System.currentTimeMillis()+".3gp";
        return fileName;
    }
}
