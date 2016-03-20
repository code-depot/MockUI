package com.easecred.android.mockui;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by android on 3/19/16.
 */
public class LoginManager {



    public interface LoginListener {

        public void onLogin();
        public void onLogout();

    }

    private class LoginState {

        public static final int FB = 1;
        public static final int GS = 2;
        public static final int UNKNOWN =3;

        private boolean mIsLogged;
        private int  mAccountType;

        LoginState(boolean isLogged,int accountType) {
            mIsLogged = isLogged;
            mAccountType = accountType;
        }
    }


    private SharedPreferences mPreferences;
    private AccessTokenTracker mFBTokenTracker;
    private AccessToken mFBToken;
    private CallbackManager mCallbackManager;
    private LoginState mLoginState;

    private static final String TAG = "MOCK_UI_STATE";
    private static final String LOGGED_STATE_KEY =  "IS_USER_LOGGED";
    private static final String LOGGED_ACCOUNT_TYPE = "ACCOUNT_TYPE";

    private UserAccountInfo mUserAccountInfo;
    private Context mContext;
    private LoginListener mLoginListener;


    public static final String LOGIN_NOTIFCATION = "com.easecred.android.mockui.login";
    public static final String MOCK_UI_EVENTS = "MOCK_UI_EVENTS";
    public static int PROFILE_UPDATE_SUCCESS = 100;
    public static int PROFILE_UPDATE_FAILED = 101;


    LoginManager(Context ctx) {

        //  mOnMockUIAppStateChangeListener = new ArrayList<>()''
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean isLogged = mPreferences.getBoolean(LOGGED_STATE_KEY, false);
        int accountType = mPreferences.getInt(LOGGED_ACCOUNT_TYPE, LoginState.UNKNOWN);
        mUserAccountInfo = UserAccountInfo.getInstance();
        mLoginState = new LoginState(isLogged, accountType);
        FacebookSdkIntialize(ctx);

    }



    public void setLoginListener(LoginListener listener) {
        if(listener != null) {
            mLoginListener = listener;
        }

    }
    public  void FacebookSdkIntialize(Context ctx) {

        mContext = ctx;
        FacebookSdk.sdkInitialize(mContext);
        mFBTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                Log.d(TAG, "current access token");
                mFBToken = currentAccessToken == null ? oldAccessToken :currentAccessToken;
                if(currentAccessToken == null && oldAccessToken != null ) {
                    LoginManager.this.setLoginInfo(false, LoginState.UNKNOWN);

                }
                else if(mFBToken != null) {
                    LoginManager.this.setLoginInfo(true, LoginState.FB);
                    queryFacebook();
                }
            }
        };

        mCallbackManager = CallbackManager.Factory.create();
        com.facebook.login.LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook login success");
                        setLoginInfo(true, LoginState.FB);
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook login cancelled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "facebook login error" + exception.getMessage());
                        // App code
                    }
                });


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
                        }                       // Application code
                    }


                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,birthday,gender,email");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public void preserveMockUIAppState() {
        mPreferences.edit().putBoolean(LOGGED_STATE_KEY,isLogged()).commit();
        mPreferences.edit().putInt(LOGGED_ACCOUNT_TYPE, getAccountType()).commit();

    }

    public void onDestroy() {
        mFBTokenTracker.stopTracking();
    }

    protected void readFromFBProfileData(JSONObject data) {

        try {
            if(data.has("first_name")) {
                mUserAccountInfo.setFirstName(data.getString("first_name"));
            }
            if(data.has("last_name")) {
                mUserAccountInfo.setLastName(data.getString("last_name"));
            }
            if(data.has("gender")) {
                String gender = data.getString("gender");
                switch (gender.toLowerCase()) {
                    case "male":
                        mUserAccountInfo.setGender(UserAccountInfo.Gender.MALE);
                        break;
                    default:
                        mUserAccountInfo.setGender(UserAccountInfo.Gender.FEMALE);
                }
            }
            if (data.has("email")) {
                mUserAccountInfo.setEmail(data.getString("email"));
            }
        }
        catch (JSONException e) {
            Log.d(TAG,e.getMessage());
        }


        Intent intent = new Intent();
        intent.setAction(LOGIN_NOTIFCATION);
        intent.putExtra(MOCK_UI_EVENTS,PROFILE_UPDATE_SUCCESS);
        mContext.sendBroadcast(intent);

    }

    public UserAccountInfo getUserAccountInfo() {
        return  mUserAccountInfo;
    }

    public boolean isLogged() {
        return mLoginState.mIsLogged;
    }


    public void  setLoginInfo(boolean state,int accountType ) {
        mLoginState.mIsLogged = state;
        mLoginState.mAccountType = accountType;
        preserveMockUIAppState();

        if(mLoginListener != null) {
            if (state) {
                mLoginListener.onLogin();
            } else {
                mLoginListener.onLogout();
            }
        }
    }

    public int getAccountType() {
        return mLoginState.mAccountType;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void logout() {
        if(!mLoginState.mIsLogged) {


        }
        if(mLoginState.mAccountType == LoginState.FB){
            com.facebook.login.LoginManager.getInstance().logOut();
            setLoginInfo(false,LoginState.UNKNOWN);
            if(mLoginListener != null ){
                mLoginListener.onLogout();
            }
        }
    }
}
