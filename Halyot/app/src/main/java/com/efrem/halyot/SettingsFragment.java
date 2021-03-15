package com.efrem.halyot;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;


public class SettingsFragment extends Fragment {

    private Button play, stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile;

    public SettingsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_settings, container, false);

            play = (Button) view.findViewById(R.id.play);
            stop = (Button) view.findViewById(R.id.stop);
            record = (Button) view.findViewById(R.id.record);
            stop.setEnabled(false);
            play.setEnabled(false);
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/records/recording.3gp";
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile);

            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    } catch (IllegalStateException ise) {
                        Log.d("inside", ise.toString());
                    } catch (IOException ioe) {
                        Log.d("inside", ioe.toString());
                    }
                    record.setEnabled(false);
                    stop.setEnabled(true);
                    Toast.makeText(getContext(), "Recording started", Toast.LENGTH_LONG).show();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder = null;
                    record.setEnabled(true);
                    stop.setEnabled(false);
                    play.setEnabled(true);
                    Toast.makeText(getContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(outputFile);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        Toast.makeText(getContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.d("inside", e.toString());
                    }
                }
            });
        return view;
        }
    }

