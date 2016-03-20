package com.easecred.android.mockui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private LoginManager mLoginManager;
    private  ImageButton mFBLoginButton;
    private AccessTokenTracker mFBTokenTracker;
    private AccessToken mFBToken;
    private ProgressDialog mProgressDialog;



    private static final String TAG = "LOGIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginManager = new LoginManager(getApplicationContext());
        mLoginManager.setLoginListener(new LoginManager.LoginListener() {
            @Override
            public void onLogin() {
                mProgressDialog =  ProgressDialog.show(LoginActivity.this,null,null,true,false);
                mProgressDialog.setContentView(R.layout.progress);
            }

            @Override
            public void onLogout() {

            }
        });

        setContentView(R.layout.activity_login);

        mFBLoginButton = (ImageButton) findViewById(R.id.fb_login_btn);



        // If using in a fragment
        //loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        mFBLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                com.facebook.login.LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));


            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "data:" + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        mLoginManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.d(TAG, "login data paused");
    }


    @Override
    public void onDestroy() {
       mLoginManager.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(LoginManager.LOGIN_NOTIFCATION);
        registerReceiver(mOnLoginComplete, filter);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mOnLoginComplete);
        super.onStop();
    }

    private BroadcastReceiver mOnLoginComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent filter) {
            int loginStatus = filter.getIntExtra(LoginManager.MOCK_UI_EVENTS,LoginManager.PROFILE_UPDATE_FAILED);

            if(loginStatus == LoginManager.PROFILE_UPDATE_SUCCESS) {
                Intent intent = new Intent(LoginActivity.this,CreditApplicationActivity.class);
                startActivity(intent);

            }
            if(mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            LoginActivity.this.finish();
        }
    };

    /*

    public  void FacebookSdkIntialize() {


        mFBTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                Log.d(TAG, "current access token");
                mFBToken = currentAccessToken == null ? oldAccessToken :currentAccessToken;
                if(currentAccessToken == null && oldAccessToken != null ) {
                    mLoginManager.getUserAccountInfo().setLogged(false);
                    mProgressDialog.dismiss();

                }
                else if(mFBToken != null) {
                    mLoginManager.getUserAccountInfo().setLogged(true);
                    queryFacebook();
                }
            }
        };

    }

   protected void queryFacebook  () {
        GraphRequest request = GraphRequest.newMeRequest(
                mFBToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        Log.d(TAG, response.toString());

                        if(object != null){
                            readFromFBProfileData(object);

                            mLoginManager.getUserAccountInfo().setLogged(true);
                            Intent intent = new Intent(getApplicationContext(),CreditApplicationActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }                       // Application code
                        mProgressDialog.dismiss();
                    }


                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,birthday,gender,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    protected void readFromFBProfileData(JSONObject data) {

        if(data == null) {
            return;
        }
        try {
            if(data.has("first_name")) {
                mLoginManager.getUserAccountInfo().getProfile().setFirstName(data.getString("first_name"));
            }
            if(data.has("last_name")) {
                mLoginManager.getUserAccountInfo().getProfile().setLastName(data.getString("last_name"));
            }
            if(data.has("gender")) {
                String gender = data.getString("gender");
                switch (gender.toLowerCase()) {
                    case  "male" :
                        mLoginManager.getUserAccountInfo().getProfile().setGender(UserAccountInfo.Gender.MALE);
                        break;
                    default:
                        mLoginManager.getUserAccountInfo().getProfile().setGender(UserAccountInfo.Gender.FEMALE);
                }
            }
            if(data.has("email")) {
                mLoginManager.getUserAccountInfo().getProfile().setEmail(data.getString("email"));
            }
        }
        catch (JSONException e) {
            Log.d(TAG,e.getMessage());
        }


    }*/
}

