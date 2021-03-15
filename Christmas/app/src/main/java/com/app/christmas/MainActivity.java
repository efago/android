package com.app.christmas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageView mic;
    TextView info;
    Intent speechIntent;
    TextToSpeech tTS;
    UtteranceProgressListener utteranceListener;
    HashMap<String, String> ttsMap;

    private Boolean inConfirmation;
    private String speechInput;

    public static final int INITIAL_INPUT= 1;
    public static final int INPUT_CONFIRMATION= 2;
    public static final String TTS_ID= "ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mic= findViewById(R.id.mic);
        info= findViewById(R.id.info);
        speechIntent=  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        if ( speechIntent.resolveActivity( getPackageManager()) != null) {

            tTS= new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    if (i == TextToSpeech.SUCCESS) {
                        tTS.setOnUtteranceProgressListener( utteranceListener);
                        tTS.setLanguage(Locale.getDefault());
                        ttsMap= new HashMap<>();
                        ttsMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, TTS_ID);


                        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                        mic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivityForResult(speechIntent, INITIAL_INPUT);
                            }
                        });
                    }
                    else {
                        mic.setVisibility( View.INVISIBLE);
                        info.setText(R.string.tts_not_supported);
                    }
                }
            });
        }
        else {
            mic.setVisibility( View.INVISIBLE);
            info.setText(R.string.not_supported);
        }

        utteranceListener= new UtteranceProgressListener() {
            @Override
            public void onStart(String s) { Log.d("inside", "on start");}
            @Override
            public void onDone(String s) {
                if ( inConfirmation){
                    inConfirmation= false;
                    Log.d("inside", "on done");
                    startActivityForResult( speechIntent, INPUT_CONFIRMATION);
                }
            }
            @Override
            public void onError(String s) { }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tTS.shutdown();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
                switch ( requestCode) {
                    case INITIAL_INPUT:
                        ArrayList<String> initialResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if ( initialResult != null && !initialResult.get(0).isEmpty()) {
                            inConfirmation= true;
                            speechInput= initialResult.get(0);
                            tTS.speak( "Do you want me to search for " + speechInput, TextToSpeech.QUEUE_FLUSH, ttsMap);
                        }
                        break;
                    case INPUT_CONFIRMATION:
                        ArrayList<String> confirmationResult= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if ( confirmationResult != null && !confirmationResult.get(0).isEmpty()) {
                            if ( confirmationResult.get(0).equals("yes")) {
                                Intent intent= new Intent( this, SearchResultActivity.class);
                                intent.putExtra("tag", speechInput);
                                startActivity( intent);
                            }
                        }
                        break;
                    default:
                        break;
            }
        }
    }
}
