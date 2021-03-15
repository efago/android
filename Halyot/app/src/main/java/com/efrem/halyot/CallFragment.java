package com.efrem.halyot;


import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.voximplant.sdk.Voximplant;
import com.voximplant.sdk.call.CallException;
import com.voximplant.sdk.call.CallSettings;
import com.voximplant.sdk.call.CallStats;
import com.voximplant.sdk.call.ICall;
import com.voximplant.sdk.call.ICallListener;
import com.voximplant.sdk.call.IEndpoint;
import com.voximplant.sdk.call.IVideoStream;
import com.voximplant.sdk.call.VideoFlags;
import com.voximplant.sdk.client.AuthParams;
import com.voximplant.sdk.client.ClientConfig;
import com.voximplant.sdk.client.IClient;
import com.voximplant.sdk.client.IClientIncomingCallListener;
import com.voximplant.sdk.client.IClientLoginListener;
import com.voximplant.sdk.client.IClientSessionListener;
import com.voximplant.sdk.client.LoginError;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CallFragment extends Fragment {

    public SipManager sipManager= null;
    public SipProfile sipProfile = null;

    public CallFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
/*
        SipProfile.Builder builder = null;
        try {
            builder = new SipProfile.Builder("efrem",
                    "efrem.sip.messagebird.com");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        builder.setPassword("efremefrem");
        sipProfile = builder.build();
        if (sipManager==null)
            sipManager= SipManager.newInstance( getContext());

        try {
            sipManager.open( sipProfile);
            sipManager.setRegistrationListener(sipProfile.getUriString(), new SipRegistrationListener() {
                public void onRegistering(String localProfileUri) {
                    Log.d("inside","Registering with SIP Server...");
                }
                public void onRegistrationDone(String localProfileUri, long expiryTime) {
                    Log.d("inside","Ready");
                }
                public void onRegistrationFailed(String localProfileUri, int errorCode,
                                                 String errorMessage) {
                    Log.d("inside","Registration failed.  Please check settings.");
                }
            });
        } catch (SipException e) {
            e.printStackTrace();
        }

        SipAudioCall.Listener listener= new SipAudioCall.Listener(){
            @Override
            public void onCallEnded(SipAudioCall call) {
                call.close();
            }
        };
*/
        SipProfile.Builder builder = null;
        try {
            builder = new SipProfile.Builder("efrem", "sip.messagebird.com");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        builder.setPassword("abc");
        SipProfile profile= builder.build();
        Log.d("inside", profile.getUriString());



        View view= inflater.inflate(R.layout.fragment_call, container, false);
        view.findViewById(R.id.button_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });

        view.findViewById(R.id.button_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent( getContext(), CallActivity.class);
                intent.putExtra("number", "+8618800103087");
                startActivity(intent);
                //new VoxCallManager( iClient);
            }
        });

        return view;
    }
    public void startCall(){
        /*final MessageBirdService wsr = new MessageBirdServiceImpl("jrzhZun3k8RUArewrizeuZ2Sc");
        final MessageBirdClient messageBirdClient = new MessageBirdClient(wsr);
        try {

            System.out.println("Sending voice call:");
            final VoiceCall voiceCall = new VoiceCall();
            voiceCall.setSource("8615629118170");
            voiceCall.setDestination("8618800103087");

            final VoiceCallFlow voiceCallFlow = new VoiceCallFlow();
            voiceCallFlow.setTitle("Test title");
            VoiceStep voiceStep = new VoiceStep();
            voiceStep.setAction("transfer");

            final VoiceStepOption voiceStepOption = new VoiceStepOption();
            voiceStepOption.setDestination("8618800103087");
            voiceStep.setOptions(voiceStepOption);

            voiceCallFlow.setSteps(Collections.singletonList(voiceStep));
            voiceCall.setCallFlow(voiceCallFlow);
            final VoiceCallResponse response = messageBirdClient.sendVoiceCall(voiceCall);
            System.out.println(response.toString());

        }
        catch (UnauthorizedException | GeneralException unauthorized) {
            if (unauthorized.getErrors() != null) {
                System.out.println(unauthorized.getErrors().toString());
            }
            unauthorized.printStackTrace();
        }*/
        Retrofit retrofit= new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();



        LocationGetter locationGetter= retrofit.create(LocationGetter.class);
        Call<ResponseBody> call= locationGetter.getWhatever(2, "id");
        //Call<ResponseBody> call= locationGetter
        //      .getLocation("50,50", "AIzaSyADG4q36vj7ra-JPaQsRERGPuOvfLZrqHc");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if ( response.isSuccessful()){
                    Log.d("inside", "success");
                }
                else {
                    try {
                        Log.d("inside", response.code() + "..."+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("inside", "failed");

            }
        });
    }
    public void sendSMS(){
        Retrofit retrofit= new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://rest.messagebird.com")
                .build();

        LocationGetter locationGetter= retrofit.create(LocationGetter.class);
        Call<ResponseBody> call= locationGetter.sendSMS("+8618800103087",
                "+4917657918122","whats up duck");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if ( response.isSuccessful()){
                    try {
                        //response.body is where the data is

                        Log.d("inside", response.code()+"..."+
                                response.body().string()+"..."+
                                response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        Log.d("inside", response.code() + "..."+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("inside", "failed");

            }
        });
    }
    //public void sendSMS(){
        /*System.out.println("Sending message:");
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                final MessageBirdService wsr = new MessageBirdServiceImpl("h22dk27J6iN7l8gDllBdbPq03");
                final MessageBirdClient messageBirdClient = new MessageBirdClient(wsr);

                try {
                    System.out.println("Sending message:");
                    final List<BigInteger> phones = new ArrayList<>();
                    phones.add(new BigInteger("8615629118170"));

                    final MessageResponse response = messageBirdClient.sendMessage("8615629118170","Hi sanny" , phones);
                    System.out.println(response.toString());
                }
                catch (UnauthorizedException | GeneralException exception) {
                    if (exception.getErrors() != null) {
                        System.out.println(exception.getErrors().toString());
                    }
                    exception.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
    public void sendSMS2(){
*/
    //}
}
