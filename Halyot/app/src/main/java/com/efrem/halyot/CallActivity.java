package com.efrem.halyot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import java.util.Map;
import java.util.concurrent.Executors;

public class CallActivity extends AppCompatActivity {

    private IClient iClient;
    private ICall mManagedCall;
    Button mute, speaker, endCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        iClient= Voximplant.getClientInstance(Executors.newSingleThreadExecutor(),
                this, new ClientConfig());//4Mvhw08k
        new VoxClientManager(iClient);

        endCall= findViewById(R.id.call_hangup);
        mute= findViewById(R.id.mute);
        speaker= findViewById(R.id.speaker);

        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (endCall.getText().toString().toLowerCase().equals("end call")) {
                    mManagedCall.hangup(null);
                    endCall.setText("call");
                }
                else {
                    new VoxCallManager(iClient);
                    endCall.setText("end call");
                }
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        new VoxCallManager( iClient);

    }
    private class VoxClientManager implements IClientSessionListener, IClientLoginListener {
        private IClient client;

        // ctor and other logic here
        // Note: you also need to implement other methods from
        // IClientLoginListener and IClientSessionListener

        @Override
        public void onConnectionEstablished() {
            Log.i("inside", "Connection established");
            // For other login methods please see the article:
            // https://goo.gl/ZVxwcT
            client.login("anonymous@jogabonito.efrem.n2.voximplant.com", "4Mvhw08k");
        }

        @Override
        public void onConnectionFailed(String s) {

        }

        @Override
        public void onConnectionClosed() {

        }

        @Override
        public void onLoginSuccessful(String displayName,
                                      AuthParams authParams) {
            Log.i("inside", "Login succeeded");
        }

        @Override
        public void onLoginFailed(LoginError loginError) {
            Log.d("inside", "login failed");
        }

        @Override
        public void onRefreshTokenFailed(LoginError loginError) {

        }

        @Override
        public void onRefreshTokenSuccess(AuthParams authParams) {

        }

        @Override
        public void onOneTimeKeyGenerated(String s) { }

        public VoxClientManager( IClient iClient) {
            client= iClient;
            client.setClientSessionListener(this);
            client.setClientLoginListener(this);
            client.connect();
        }
    }

    private class VoxCallManager implements ICallListener {

        public VoxCallManager( IClient mClient){
            CallSettings callSettings = new CallSettings();
            callSettings.videoFlags = new VideoFlags(false, false);
            mManagedCall = mClient.call(getIntent().getStringExtra("number"), callSettings);

            if (mManagedCall == null) {
                Log.d("inside", "managed call is null");
                return;
            }
            else {
                try {
                    mManagedCall.start();
                    mManagedCall.addCallListener(this);
                } catch (CallException e) {
                    Log.e("inside", "VoxCallManager: startCallException: " + e.getMessage());
                    mManagedCall = null;
                }
            }
        }

        @Override
        public void onCallConnected(ICall call, Map<String, String> headers) {

        }

        @Override
        public void onCallDisconnected(ICall call, Map<String, String> headers, boolean answeredElsewhere) {

        }

        @Override
        public void onCallRinging(ICall call, Map<String, String> headers) {

        }

        @Override
        public void onCallFailed(ICall call, int code, String description, Map<String, String> headers) {

        }

        @Override
        public void onCallAudioStarted(ICall call) {

        }

        @Override
        public void onSIPInfoReceived(ICall call, String type, String content, Map<String, String> headers) {

        }

        @Override
        public void onMessageReceived(ICall call, String text) {

        }

        @Override
        public void onLocalVideoStreamAdded(ICall call, IVideoStream videoStream) {

        }

        @Override
        public void onLocalVideoStreamRemoved(ICall call, IVideoStream videoStream) {

        }

        @Override
        public void onICETimeout(ICall call) {

        }

        @Override
        public void onICECompleted(ICall call) {

        }

        @Override
        public void onEndpointAdded(ICall call, IEndpoint endpoint) {

        }

        @Override
        public void onCallStatsReceived(ICall call, CallStats callStats) {

        }
    }
}
